package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/10/31 0031.
 */
public class Peilian_Bean {

    private String ID;
    private String ProCover;
    private String ProName;
    private String ProMoney;
    private String ProDescwen;
    private String httpImg;

    public Peilian_Bean() {
    }

    public String getProName() {
        return ProName;
    }

    public void setProName(String proName) {
        ProName = proName;
    }

    public String getProMoney() {
        return ProMoney;
    }

    public void setProMoney(String proMoney) {
        ProMoney = proMoney;
    }

    public String getProDescwen() {
        return ProDescwen;
    }

    public void setProDescwen(String proDescwen) {
        ProDescwen = proDescwen;
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }

    public String getProCover() {
        return ProCover;
    }

    public void setProCover(String proCover) {
        ProCover = proCover;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
