package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class UserdCar_Bean {

    private String id;
    private String Car_cover;
    private String Car_title;
    private String Pname;
    private String Car_models;
    private String Car_PriceX;
    private String Car_Icontent;
    private String httpImg;

    public UserdCar_Bean() {
    }

    public String getCar_title() {
        return Car_title;
    }

    public void setCar_title(String car_title) {
        Car_title = car_title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar_cover() {
        return Car_cover;
    }

    public void setCar_cover(String car_cover) {
        Car_cover = car_cover;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getCar_models() {
        return Car_models;
    }

    public void setCar_models(String car_models) {
        Car_models = car_models;
    }

    public String getCar_PriceX() {
        return Car_PriceX;
    }

    public void setCar_PriceX(String car_PriceX) {
        Car_PriceX = car_PriceX;
    }

    public String getCar_Icontent() {
        return Car_Icontent;
    }

    public void setCar_Icontent(String car_Icontent) {
        Car_Icontent = car_Icontent;
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }
}
