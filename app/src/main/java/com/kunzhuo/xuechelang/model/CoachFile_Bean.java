package com.kunzhuo.xuechelang.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/5 0005.
 */
public class CoachFile_Bean implements Serializable {

    private String Pid;
    private String Pic;
    private String Ptype;
    private String httpImg;

    public CoachFile_Bean() {
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getPtype() {
        return Ptype;
    }

    public void setPtype(String ptype) {
        Ptype = ptype;
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }
}
