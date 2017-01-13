package com.kunzhuo.xuechelang.model;

import java.io.Serializable;

/**
 * 个人信息Bean
 * Created by waaa on 2016/9/8.
 */
public class User_Bean {

    private String UID;
    private String Uaccount;
    private String UEmail;
    private String Usex;
    private String Uname;
    private String Unickname;
    private String Uqq;
    private String Uaddress;
    private String Ulevel;
    private String PicSrc;
    private String Uregion1;
    private String briefIintroduction;
    private String Seniority;
    private int Utype;
    private int Tication;
    private int WeChat;
    private String httpImg;
    private String openid;
    private String wx_nickname;
    private String wx_portrait;
    private String wx_unionid;


    public User_Bean() {
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }

    public String getWx_unionid() {
        return wx_unionid;
    }

    public void setWx_unionid(String wx_unionid) {
        this.wx_unionid = wx_unionid;
    }

    public String getWx_portrait() {
        return wx_portrait;
    }

    public void setWx_portrait(String wx_portrait) {
        this.wx_portrait = wx_portrait;
    }

    public String getWx_nickname() {
        return wx_nickname;
    }

    public void setWx_nickname(String wx_nickname) {
        this.wx_nickname = wx_nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getWeChat() {
        return WeChat;
    }

    public void setWeChat(int weChat) {
        WeChat = weChat;
    }

    public int getTication() {
        return Tication;
    }

    public void setTication(int tication) {
        Tication = tication;
    }

    public int getUtype() {
        return Utype;
    }

    public void setUtype(int utype) {
        Utype = utype;
    }

    public String getSeniority() {
        return Seniority;
    }

    public void setSeniority(String seniority) {
        Seniority = seniority;
    }

    public String getBriefIintroduction() {
        return briefIintroduction;
    }

    public void setBriefIintroduction(String briefIintroduction) {
        this.briefIintroduction = briefIintroduction;
    }

    public String getUregion1() {
        return Uregion1;
    }

    public void setUregion1(String uregion1) {
        Uregion1 = uregion1;
    }

    public String getUlevel() {
        return Ulevel;
    }

    public void setUlevel(String ulevel) {
        Ulevel = ulevel;
    }

    public String getPicSrc() {
        return PicSrc;
    }

    public void setPicSrc(String picSrc) {
        PicSrc = picSrc;
    }

    public String getUaddress() {
        return Uaddress;
    }

    public void setUaddress(String uaddress) {
        Uaddress = uaddress;
    }

    public String getUqq() {
        return Uqq;
    }

    public void setUqq(String uqq) {
        Uqq = uqq;
    }

    public String getUnickname() {
        return Unickname;
    }

    public void setUnickname(String unickname) {
        Unickname = unickname;
    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String uname) {
        Uname = uname;
    }

    public String getUsex() {
        return Usex;
    }

    public void setUsex(String usex) {
        Usex = usex;
    }

    public String getUEmail() {
        return UEmail;
    }

    public void setUEmail(String UEmail) {
        this.UEmail = UEmail;
    }

    public String getUaccount() {
        return Uaccount;
    }

    public void setUaccount(String uaccount) {
        Uaccount = uaccount;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
