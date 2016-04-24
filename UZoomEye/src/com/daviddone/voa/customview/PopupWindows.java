package com.daviddone.voa.customview;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.daviddone.data.R;

	//自定义底部弹出框
	public class PopupWindows extends PopupWindow {

				public PopupWindows(Context mContext, View parent,String text) {

					View view = View
							.inflate(mContext, R.layout.photo_item_popupwindows, null);
					view.startAnimation(AnimationUtils.loadAnimation(mContext,
							R.anim.photo_fade_ins));
					setWidth(LayoutParams.MATCH_PARENT);
					setHeight(LayoutParams.WRAP_CONTENT);//WRAP_CONTENT or MATCH_PARENT
					setBackgroundDrawable(new BitmapDrawable());
					setFocusable(true);
					setOutsideTouchable(true);
					update();
					setContentView(view);
					TextView tv = (TextView) view.findViewById(R.id.tv_translate_result);
					tv.setText(text);
				}
			}