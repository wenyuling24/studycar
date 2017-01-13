package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class Novice_Bean {

    private String IID;
    private String Ititle;
    private String Abstract;
    private String Head_portrait;
    private String Icontent;
    private String Itime;
    private String httpImg;

    public Novice_Bean() {
    }

    public String getIcontent() {
        return Icontent;
    }

    public void setIcontent(String icontent) {
        Icontent = icontent;
    }

    public String getIID() {
        return IID;
    }

    public void setIID(String IID) {
        this.IID = IID;
    }

    public String getItitle() {
        return Ititle;
    }

    public void setItitle(String ititle) {
        Ititle = ititle;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    public String getHead_portrait() {
        return Head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        Head_portrait = head_portrait;
    }

    public String getItime() {
        return Itime;
    }

    public void setItime(String itime) {
        Itime = itime;
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }

}
