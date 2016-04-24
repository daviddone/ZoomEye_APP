package com.daviddone.voa.api;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.util.Log;

import com.daviddone.bean.SeeBugHotInfo;
import com.daviddone.voa.util.LoggerUtil;

/**
 * 
* @ClassName: SeeBugAPI 
* @Description: 本类作用 Seebug 资讯信息
* @author tangdongqing
* @date 2016-4-23 下午11:44:58 
*
 */
public class SeeBugAPI {
	public static final String HOST="https://www.seebug.org/";
	static List<SeeBugHotInfo> bugHotLists;
	/** 
	* @Title: getPassageList 
	* @Description: TODO(描述这个方法的作用) 
	* @param @param result
	* @param @return    设定参数
	* @return List<SeeBugHotInfo>    返回类型 
	* @throws 
	*/
	public  static List<SeeBugHotInfo> getPassageList(String result){
		bugHotLists= new ArrayList<SeeBugHotInfo>();
		Document doc = Jsoup.parse(result);
		Elements elements = doc.select("ul.list-unstyled.list-selected-vul li");
		Log.e("elements",""+elements.size());
		for (int i = 0; i < elements.size(); i++) {
			String detailUrl = elements.get(i).select("a").attr("href");
			String title = elements.get(i).select("a").text();
			String content = elements.get(i).select("div.des").text();
			LoggerUtil.e("SeeBugHotInfo", new SeeBugHotInfo(title, content, detailUrl).toString());
			bugHotLists.add(new SeeBugHotInfo(title, content, detailUrl));
			}
		return bugHotLists;
		}
}
