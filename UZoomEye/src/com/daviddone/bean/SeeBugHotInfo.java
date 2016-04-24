package com.daviddone.bean;

public class SeeBugHotInfo {
	private String title;
	private String content;
	private String detailUrl;
	@Override
	public String toString() {
		return "SeeBugBean [title=" + title + ", content=" + content
				+ ", detailUrlc=" + detailUrl + "]";
	}
	public SeeBugHotInfo(String title, String content, String detailUrl) {
		super();
		this.title = title;
		this.content = content;
		this.detailUrl = detailUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	
}
