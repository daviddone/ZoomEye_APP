package com.daviddone.data.ui;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daviddone.data.R;
import com.daviddone.voa.api.SeeBugAPI;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class MyWebViewActivity extends Activity {
	String DEFAULT_URL = "http://cn.bing.com";
	private ProgressDialog proDlg;
	private WebView webView;

	private WebSettings webSet;
	private LinearLayout ll_loading;
	private ProgressBar bar;

	MyWebViewActivity ctx;
	String href = "";
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what== 0){
				showWebView();
			}
		};
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview_common);
		webView = (WebView) findViewById(R.id.webView);
		bar = (ProgressBar) findViewById(R.id.myProgressBar);
		ctx = this;
//		href = getIntent().getStringExtra("href");
		href = "https://www.seebug.org/vuldb/topics/2";
		if(href!=null && !("").equals(href.trim())){
			DEFAULT_URL =href;
//			initData();
			showWebView();
		}
//		Toast.makeText(getApplicationContext(), DEFAULT_URL, 0).show();
	}


	private void showWebView() {
		webView.setWebChromeClient(new WebChromeClient() {

	          @Override
	          public void onProgressChanged(WebView view, int newProgress) {
	              if (newProgress == 100) {
	                  bar.setVisibility(View.GONE);
	              } else {
	                  if (View.GONE == bar.getVisibility()) {
	                      bar.setVisibility(View.VISIBLE);
	                  }
	                  bar.setProgress(newProgress);
	              }
	              super.onProgressChanged(view, newProgress);
	          }
	          
	      });
		ll_loading= (LinearLayout)findViewById(R.id.loadingWebView);
		init();
//		fillData();
		webView.loadUrl(DEFAULT_URL);
//		 webView.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
//       webView.loadData(detailText, "text/html; charset=UTF-8", null);//这种写法可以正确解码
	}
	String detailText="";
	private void initData() {
		RequestParams params = new RequestParams();
//		params.addBodyParameter("userId", "455ba6a7b31248ceba1d431a2fbd2e2e");
		new HttpUtils().send(HttpRequest.HttpMethod.GET,
			DEFAULT_URL,
//		    params,
		    new RequestCallBack<String>() {
		        @Override
		        public void onSuccess(ResponseInfo<String> responseInfo) {
		        	Toast.makeText(getApplicationContext(), "成功", 0).show();
//		        	detailText = SeeBugAPI.getDetailText(responseInfo.result);
		        	handler.sendEmptyMessage(0);
		        }

				@Override
		        public void onFailure(HttpException error, String msg) {
		           Toast.makeText(getApplicationContext(), "fail", 0).show();
		        }
		});
		
	}


	private void init() {
		
		WebSettings webSettings = webView.getSettings();
		webSettings.setSupportZoom(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		
		webSettings.setBuiltInZoomControls(true);// support zoom 
		// webSettings.setPluginsEnabled(true);//support flash
		webSettings.setUseWideViewPort(true);//
		webSettings.setLoadWithOverviewMode(true);//

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int mDensity = metrics.densityDpi;
		if (mDensity == 240) {
			webSettings.setDefaultZoom(ZoomDensity.FAR);
		} else if (mDensity == 160) {
			webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (mDensity == 120) {
			webSettings.setDefaultZoom(ZoomDensity.CLOSE);
		} else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
			webSettings.setDefaultZoom(ZoomDensity.FAR);
		} else if (mDensity == DisplayMetrics.DENSITY_TV) {
			webSettings.setDefaultZoom(ZoomDensity.FAR);
		}
		proDlg =new ProgressDialog(this);
		proDlg.setMessage("数据加载中");
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

}