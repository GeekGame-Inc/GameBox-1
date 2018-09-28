package com.tenone.gamebox.view.custom.photo.enty;

import android.text.TextUtils;

/**
 * ͼƬʵ��
 * 
 * @author John Li
 * @data: 2017��2��23�� ����4:45:07
 * @version: V1.0
 */
public class Image {
	public String path;
	public String name;
	public long time;

	public Image(String path, String name, long time) {
		this.path = path;
		this.name = name;
		this.time = time;
	}

	@Override
	public boolean equals(Object o) {
		try {
			Image other = (Image) o;
			return TextUtils.equals(this.path, other.path);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return super.equals(o);
	}
}
