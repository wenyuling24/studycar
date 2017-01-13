package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/10/24 0024.
 */
public class CoachSingle_Bean {

    private String Uaccount;
    private String Uname;
    private String Seniority;
    private String Pname;
    private String Pic;
    private String ExtenId;
    private int Tication;
    private String httpImg;

    public CoachSingle_Bean() {
    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String uname) {
        Uname = uname;
    }

    public String getUaccount() {
        return Uaccount;
    }

    public void setUaccount(String uaccount) {
        Uaccount = uaccount;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getExtenId() {
        return ExtenId;
    }

    public void setExtenId(String extenId) {
        ExtenId = extenId;
    }

    public int getTication() {
        return Tication;
    }

    public void setTication(int tication) {
        Tication = tication;
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
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
}
