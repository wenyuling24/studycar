package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
public class CoachList_Bean {

    private String UID;
    private String Uaccount;
    private String Uname;
    private String Seniority;
    private String Pname;
    private String Pic;
    private String httpImg;

    public CoachList_Bean() {
    }

    public String getUaccount() {
        return Uaccount;
    }

    public void setUaccount(String uaccount) {
        Uaccount = uaccount;
    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String uname) {
        Uname = uname;
    }

    public String getSeniority() {
        return Seniority;
    }

    public void setSeniority(String seniority) {
        Seniority = seniority;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
