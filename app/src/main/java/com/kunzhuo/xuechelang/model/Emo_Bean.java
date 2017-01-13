package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/10/9 0009.
 * 表情实体类
 */
public class Emo_Bean {

    private int ID;
    private String imgSrc;
    private String data;
    private String httpImg;

    public Emo_Bean() {
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }
}
