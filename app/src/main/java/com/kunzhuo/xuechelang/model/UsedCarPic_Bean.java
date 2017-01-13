package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class UsedCarPic_Bean {

    private String id;
    private String Car_src;
    private String httpImg;

    public UsedCarPic_Bean() {
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar_src() {
        return Car_src;
    }

    public void setCar_src(String car_src) {
        Car_src = car_src;
    }
}
