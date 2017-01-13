package com.kunzhuo.xuechelang.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/12 0012.
 */
public class MapUserPic_Bean implements Serializable {

    private String ID;
    private String PicSrc;
    private String httpImg;

    public MapUserPic_Bean() {
    }

    public String getPicSrc() {
        return PicSrc;
    }

    public void setPicSrc(String picSrc) {
        PicSrc = picSrc;
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
