package com.daviddone.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daviddone.bean.SeeBugHotInfo;
import com.daviddone.data.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;

public class BugHotAdapter extends BaseAdapter{
	private Context mContext;
	List<SeeBugHotInfo> passageLists;
	public BugHotAdapter(Context mContext,List<SeeBugHotInfo> passageLists) {
		this.mContext = mContext;
		this.passageLists = passageLists;
	}
	

	public int getCount() {
		return passageLists.size();
	}

	public Object getItem(int position) {
		return passageLists.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SeeBugHotInfo eachItem = passageLists.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.listitem_hotbug, null);
			ViewUtils.inject(viewHolder, view);
			viewHolder.tv_hotbug_title = (TextView) view.findViewById(R.id.tv_hotbug_title);
			viewHolder.tv_hotbug_content = (TextView) view.findViewById(R.id.tv_hotbug_content);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_hotbug_title.setText(eachItem.getTitle()); 
		viewHolder.tv_hotbug_content.setText(eachItem.getContent()); 
		return view;
	}
	 class ViewHolder {
		TextView tv_hotbug_title;
		TextView tv_hotbug_content;
	}
}
