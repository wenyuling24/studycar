package com.kunzhuo.xuechelang.model;

/**
 * Created by waaa on 2016/9/12.
 * 轮播图片类
 */
public class CarouselList_Bean {
    private String banner; // 科目一轮播图
    private String tryTolearn; // 试学
    private String Sparring; // 陪练
    private String http; //图片地址

    public CarouselList_Bean() {
    }

    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http;
    }

    public String getSparring() {
        return Sparring;
    }

    public void setSparring(String sparring) {
        Sparring = sparring;
    }

    public String getTryTolearn() {
        return tryTolearn;
    }

    public void setTryTolearn(String tryTolearn) {
        this.tryTolearn = tryTolearn;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}
