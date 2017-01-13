package com.kunzhuo.xuechelang.model;

import java.io.Serializable;

import android.R.integer;

/**
 * 广告图片
 * 
 * @author Administrator
 * 
 */
public class BannerBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id; // 广告id
	private int Category; // 类型 1000 引导页 1001 banner
	private String title; // 标题
	private String Image; // 图片url
	private String Target; // 目标链接
	private int Duration; // 显示时长
	private String EffectDate; // 广告生效日期
	private String ExpiryDate; // 广告截止日期

	public BannerBean() {

	}

	public BannerBean(int id, int category, String title, String image,
			String target, int duration, String effectDate, String expiryDate) {
		super();
		this.id = id;
		Category = category;
		this.title = title;
		Image = image;
		Target = target;
		Duration = duration;
		EffectDate = effectDate;
		ExpiryDate = expiryDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategory() {
		return Category;
	}

	public void setCategory(int category) {
		Category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public String getTarget() {
		return Target;
	}

	public void setTarget(String target) {
		Target = target;
	}

	public int getDuration() {
		return Duration;
	}

	public void setDuration(int duration) {
		Duration = duration;
	}

	public String getEffectDate() {
		return EffectDate;
	}

	public void setEffectDate(String effectDate) {
		EffectDate = effectDate;
	}

	public String getExpiryDate() {
		return ExpiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		ExpiryDate = expiryDate;
	}

	

}