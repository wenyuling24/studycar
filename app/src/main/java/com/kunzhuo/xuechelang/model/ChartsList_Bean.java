package com.kunzhuo.xuechelang.model;

/**
 * Created by waaa on 2016/9/20.
 */
public class ChartsList_Bean {

    private String Unickname; // 昵称
    private String Uaccount; // 账户
    private String PicSrc; // 用户头像
    private int Fraction; // 份数
    private int ExamTime; // 考试花费时间
    private int PxSort; // 排名
    private int Self; // 当前人排行
    private String RanDate; // 考试时间
    private String httpImg;
    private String wx_portrait; // 微信头像
    private String openid; //openid
    private String wx_unionid; //
    private String WeChat; //

    public ChartsList_Bean() {
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getWx_unionid() {
        return wx_unionid;
    }

    public void setWx_unionid(String wx_unionid) {
        this.wx_unionid = wx_unionid;
    }

    public String getWeChat() {
        return WeChat;
    }

    public void setWeChat(String weChat) {
        WeChat = weChat;
    }

    public String getWx_portrait() {
        return wx_portrait;
    }

    public void setWx_portrait(String wx_portrait) {
        this.wx_portrait = wx_portrait;
    }

    public String getUnickname() {
        return Unickname;
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }

    public void setUnickname(String unickname) {
        Unickname = unickname;
    }

    public String getUaccount() {
        return Uaccount;
    }

    public void setUaccount(String uaccount) {
        Uaccount = uaccount;
    }

    public String getPicSrc() {
        return PicSrc;
    }

    public void setPicSrc(String picSrc) {
        PicSrc = picSrc;
    }

    public int getFraction() {
        return Fraction;
    }

    public void setFraction(int fraction) {
        Fraction = fraction;
    }

    public int getExamTime() {
        return ExamTime;
    }

    public void setExamTime(int examTime) {
        ExamTime = examTime;
    }

    public int getPxSort() {
        return PxSort;
    }

    public void setPxSort(int pxSort) {
        PxSort = pxSort;
    }

    public int getSelf() {
        return Self;
    }

    public void setSelf(int self) {
        Self = self;
    }

    public String getRanDate() {
        return RanDate;
    }

    public void setRanDate(String ranDate) {
        RanDate = ranDate;
    }
}
