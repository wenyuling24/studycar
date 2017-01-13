package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class Video_Bean {

    private String Vid; // ID
    private String Vtitle; // 视频标题
    private String Vcontent; // 视频简介
    private String Vpid; // 视频封面
    private int Playnumber; // 视频点击量
    private String Vtime; // 视频添加时间
    private String httpImg; // 视频图片地址
    private int rowNum; // 排序值

    public Video_Bean() {
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getVtitle() {
        return Vtitle;
    }

    public void setVtitle(String vtitle) {
        Vtitle = vtitle;
    }

    public String getVid() {
        return Vid;
    }

    public void setVid(String vid) {
        Vid = vid;
    }

    public String getVpid() {
        return Vpid;
    }

    public void setVpid(String vpid) {
        Vpid = vpid;
    }

    public String getVcontent() {
        return Vcontent;
    }

    public void setVcontent(String vcontent) {
        Vcontent = vcontent;
    }

    public int getPlaynumber() {
        return Playnumber;
    }

    public void setPlaynumber(int playnumber) {
        Playnumber = playnumber;
    }

    public String getVtime() {
        return Vtime;
    }

    public void setVtime(String vtime) {
        Vtime = vtime;
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }
}
