package com.tenone.gamebox.share;

import android.graphics.Bitmap;

public class ShareModel {
	private String title;
	private String text;
	private String url;
	private String imageUrl;
	private String site;
	private String siteUrl;
	private String comment;
	private Bitmap imageData;
	private String[] imageArray;
	
	
	public String[] getImageArray() {
		return imageArray;
	}

	public void setImageArray(String[] imageArray) {
		this.imageArray = imageArray;
	}

	public Bitmap getImageData() {
		return imageData;
	}

	public void setImageData(Bitmap imageData) {
		this.imageData = imageData;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSite() {
		return site;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

}
