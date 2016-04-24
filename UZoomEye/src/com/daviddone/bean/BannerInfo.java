package com.daviddone.bean;

public class BannerInfo {
	private String detailUrl;
	private String imageUrl;
	private String title;
	public BannerInfo(String detailUrl, String imageUrl, String title) {
		super();
		this.detailUrl = detailUrl;
		this.imageUrl = imageUrl;
		this.title = title;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	@Override
	public String toString() {
		return "BannerInfo [detailUrl=" + detailUrl + ", imageUrl=" + imageUrl
				+ ", title=" + title + "]";
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
