package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/11/5 0005.
 */
public class TryTolearn_Bean {
    private String ID;
    private String Name;
    private String Phone;
    private String Region;
    private String Detailed;
    private String TryState;
    private String httpImg;

    public TryTolearn_Bean() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getDetailed() {
        return Detailed;
    }

    public void setDetailed(String detailed) {
        Detailed = detailed;
    }

    public String getTryState() {
        return TryState;
    }

    public void setTryState(String tryState) {
        TryState = tryState;
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }
}
