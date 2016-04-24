package com.daviddone.data.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.daviddone.adapter.BugHotAdapter;
import com.daviddone.bean.BannerInfo;
import com.daviddone.bean.SeeBugHotInfo;
import com.daviddone.data.R;
import com.daviddone.voa.api.SeeBugAPI;
import com.daviddone.voa.util.LoggerUtil;
import com.daviddone.voa.util.MyConstants;
import com.daviddone.voa.util.NetworkUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MainActivity extends FragmentActivity implements OnClickListener{
	//顶部
	@ViewInject(R.id.ll_head_back)
	private LinearLayout ll_head_back;
	@ViewInject(R.id.tv_head_title)
	private TextView tv_head_title;
	@ViewInject(R.id.tv_head_title_right)
	private TextView tv_head_title_right;
	@ViewInject(R.id.rl_home_vedio)
	private RelativeLayout rl_home_vedio;
	@ViewInject(R.id.rl_home_broadcast)
	private RelativeLayout rl_home_broadcast;
	@ViewInject(R.id.rl_home_news)
	private RelativeLayout rl_home_news;
	@ViewInject(R.id.ll_home)
	private LinearLayout ll_home;
	@ViewInject(R.id.sc_home)
	private ScrollView sc_home;
	
	@ViewInject(R.id.lv_home_recommand)//首页醒目推荐
	private com.daviddone.voa.customview.NoScrollListView lv_home_recommand;
	
	
	public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径
	Map homeMap = new HashMap<String, Object>();
	List<SeeBugHotInfo> hotBugLists = new ArrayList<SeeBugHotInfo>();
	List<BannerInfo> bannerLists = new ArrayList<BannerInfo>();
	private ViewPager adViewPager;
	private List<ImageView> imageViews;// 滑动的图片集合

	private List<View> dots; // 图片标题正文的那些点
	private List<View> dotList;

	private int currentItem = 0; // 当前图片的索引号
	private ScheduledExecutorService scheduledExecutorService;
	// 轮播banner的数据

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adViewPager.setCurrentItem(currentItem);
		};
	};
	private Handler handler2 = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==0){
				Log.e("hotBugLists.size()",hotBugLists.size()+"");
				if(hotBugLists.size()>0){
					initLv();
				}
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		initTitle();
		initClick();
		if(NetworkUtil.isNetworkAvailable(this)){
			Toast.makeText(getApplicationContext(), "有网", 0).show();
//			initData();
			initBanner();
			initData();
		}else{
			Toast.makeText(getApplicationContext(), "无网", 0).show();
			sc_home.setVisibility(View.GONE);
			final View view = getLayoutInflater().inflate(R.layout.status_empty_error_nonet_layout, null);
			tv_network_reload = (TextView)view.findViewById(R.id.tv_setting);
			setContentView(view);
			tv_network_reload.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "点击", 0).show();
					  if(NetworkUtil.isNetworkAvailable(getApplicationContext())){
						  	setContentView(R.layout.activity_main);
						  	ViewUtils.inject(MainActivity.this);
						  	initTitle();
							initClick();
						  	 sc_home.setVisibility(View.VISIBLE);
							initData();
//						  startActivity(new Intent(MainActivity.this,MainActivity.class));
					  }
				}
			});
		}
		
	}
	TextView  tv_network_reload;
	//初始化数据
	private void initData() {
			RequestParams params = new RequestParams();
			new HttpUtils().send(HttpRequest.HttpMethod.GET,
				SeeBugAPI.HOST,
//				    params,
			    new RequestCallBack<String>() {
			        @Override
			        public void onSuccess(ResponseInfo<String> responseInfo) {
			        	Toast.makeText(getApplicationContext(), "成功", 0).show();
						String html = responseInfo.result;
						hotBugLists = SeeBugAPI.getPassageList(html);
						LoggerUtil.e("hotBugLists", ""+hotBugLists.size());
						handler2.sendEmptyMessage(0);
			        }

					@Override
			        public void onFailure(HttpException error, String msg) {
			           Toast.makeText(getApplicationContext(), "fail", 0).show();
			        }
			});
			
		}
	//初始化列表
	private void initLv() { 
		BugHotAdapter tedAdapter = new BugHotAdapter(getApplicationContext(),hotBugLists);
		lv_home_recommand.setAdapter(tedAdapter);
		lv_home_recommand.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				//查询的id
//					String queryId = initData().get(position).getOrderId();
				Intent intent = new Intent(getApplicationContext(),MyWebViewActivity.class);
				LoggerUtil.e("href", hotBugLists.get(position).getDetailUrl());
				intent.putExtra("href", hotBugLists.get(position).getDetailUrl());
				startActivity(intent);
				
			}
			
		});
	}
	// 异步加载图片
		private ImageLoader mImageLoader;
		private DisplayImageOptions options;
		private void initBanner() {
			bannerLists.add(new BannerInfo("", "https://www.seebug.org/static/dist2/images/carousel-1.jpg", ""));
			bannerLists.add(new BannerInfo("", "https://www.seebug.org/static/dist2/images/carousel-2.jpg", ""));
			bannerLists.add(new BannerInfo("", "https://www.seebug.org/static/dist2/images/carousel-3.jpg", ""));
			bannerLists.add(new BannerInfo("", "https://www.seebug.org/static/dist2/images/carousel-4.jpg", ""));
			// 使用ImageLoader之前初始化
			initImageLoader();

			// 获取图片加载实例
			mImageLoader = ImageLoader.getInstance();
			options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.top_banner_android)
					.showImageForEmptyUri(R.drawable.top_banner_android)
					.showImageOnFail(R.drawable.top_banner_android)
					.cacheInMemory(true).cacheOnDisc(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.EXACTLY).build();

			initAdData();
			startAd();
		}
		private void startAd() {
			scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
			// 当Activity显示出来后，每两秒切换一次图片显示
			scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2,
					TimeUnit.SECONDS);
		}

		private class ScrollTask implements Runnable {

			@Override
			public void run() {
				synchronized (adViewPager) {
					currentItem = (currentItem + 1) % imageViews.size();
					handler.obtainMessage().sendToTarget();
				}
			}
		}
		private void initImageLoader() {
			File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils
					.getOwnCacheDirectory(getApplicationContext(),
							IMAGE_CACHE_PATH);

			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisc(true).build();

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					this).defaultDisplayImageOptions(defaultOptions)
					.memoryCache(new LruMemoryCache(12 * 1024 * 1024))
					.memoryCacheSize(12 * 1024 * 1024)
					.discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
					.discCache(new UnlimitedDiscCache(cacheDir))
					.threadPriority(Thread.NORM_PRIORITY - 2)
					.tasksProcessingOrder(QueueProcessingType.LIFO).build();

			ImageLoader.getInstance().init(config);
		}

		private void initAdData() {
			// 广告数据
			imageViews = new ArrayList<ImageView>();
			// 点
			dots = new ArrayList<View>();
					LinearLayout ll_banner_addView = (LinearLayout) findViewById(R.id.ll_banner_dots);
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_dot);
			        for (int i = 0; i < bannerLists.size(); i++) {
			        	LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
			        			bitmap.getWidth(),
			        			bitmap.getHeight());
			        	//设置每个小圆点距离左边的间距
			        	margin.setMargins(10, 0, 0, 0);
			            Button bt = new Button(this);
			            bt.setBackgroundResource(R.drawable.banner_dot_normal);
			            bt.setLayoutParams(new ViewGroup.LayoutParams(bitmap.getWidth(),bitmap.getHeight()));
//			            bt.setBackgroundResource(R.drawable.icon_dot);
			            dots.add(bt);
			            ll_banner_addView.addView(bt,margin);
			        }
			dotList = new ArrayList<View>();
			addDynamicView();
			//end
			adViewPager = (ViewPager) findViewById(R.id.vp);
			adViewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
			// 设置一个监听器，当ViewPager中的页面改变时调用
			adViewPager.setOnPageChangeListener(new MyPageChangeListener());
		}

		private void addDynamicView() {
			// 动态添加图片和下面指示的圆点
			// 初始化图片资源
			for (int i = 0; i < bannerLists.size(); i++) {
				ImageView imageView = new ImageView(this);
				// 异步加载图片
				mImageLoader.displayImage(bannerLists.get(i).getImageUrl(), imageView,
						options);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				imageViews.add(imageView);
				dots.get(i).setVisibility(View.VISIBLE);
				dotList.add(dots.get(i));
			}
		}

	private void initClick() {
		rl_home_vedio.setOnClickListener(this);
		rl_home_broadcast.setOnClickListener(this);
		rl_home_news.setOnClickListener(this);
	}
		@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.ll_head_back:
					finish();
					break;
				case R.id.tv_head_title_right:
					startActivity(new Intent(this,ZoomEyeActivity.class));
					break;
				}
			}
		//初始化顶部
		private void initTitle() {
			tv_head_title.setText(getResources().getString(R.string.app_name));
			tv_head_title_right.setVisibility(View.VISIBLE);
			ll_head_back.setVisibility(View.GONE);
			tv_head_title_right.setOnClickListener(this);
		}
		private class MyPageChangeListener implements OnPageChangeListener {

			private int oldPosition = 0;

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int position) {
				currentItem = position;
				BannerInfo adDomain = bannerLists.get(position);
				dots.get(oldPosition).setBackgroundResource(R.drawable.banner_dot_normal);
				dots.get(position).setBackgroundResource(R.drawable.banner_dot_focused);
				oldPosition = position;
			}
		}
		private class MyAdapter extends PagerAdapter {

			@Override
			public int getCount() {
				return bannerLists.size();
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				Log.e("imageViews",imageViews.size()+"");
				ImageView iv = imageViews.get(position);
				final int _position  = position;
				((ViewPager) container).addView(iv);
				return iv;
			}

			@Override
			public void destroyItem(View arg0, int arg1, Object arg2) {
				((ViewPager) arg0).removeView((View) arg2);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {

			}

			@Override
			public Parcelable saveState() {
				return null;
			}

			@Override
			public void startUpdate(View arg0) {

			}

			@Override
			public void finishUpdate(View arg0) {

			}

		}
		
		@Override
		protected void onStop() {
			super.onStop();
		}
		
}
