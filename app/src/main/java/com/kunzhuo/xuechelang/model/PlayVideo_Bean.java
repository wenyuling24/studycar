package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/9/26 0026.
 * 视频详情 VideoDetails
 */
public class PlayVideo_Bean {

    private String Vid; // ID
    private String Vtitle; // 视频标题
    private String Vcontent; // 视频简介
    private String Vsrc; // 播放地址
    private String Vhost; // host
    private String Vpid; // 视频封面
    private int Playnumber; // 视频点击量
    private String Vtime; // 视频添加时间
    private String httpImg; // 视频图片地址
    private String Cid; // 收藏ID 0:  无ID
    private String Zid; // 点赞ID
    private int Thing; // 点赞总数量'

    public PlayVideo_Bean() {
    }

    public int getThing() {
        return Thing;
    }

    public void setThing(int thing) {
        Thing = thing;
    }

    public String getVhost() {
        return Vhost;
    }

    public void setVhost(String vhost) {
        Vhost = vhost;
    }

    public String getVtitle() {
        return Vtitle;
    }

    public void setVtitle(String vtitle) {
        Vtitle = vtitle;
    }

    public String getVcontent() {
        return Vcontent;
    }

    public void setVcontent(String vcontent) {
        Vcontent = vcontent;
    }

    public String getVsrc() {
        return Vsrc;
    }

    public void setVsrc(String vsrc) {
        Vsrc = vsrc;
    }

    public String getVpid() {
        return Vpid;
    }

    public void setVpid(String vpid) {
        Vpid = vpid;
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

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getZid() {
        return Zid;
    }

    public void setZid(String zid) {
        Zid = zid;
    }

    public String getVid() {
        return Vid;
    }

    public void setVid(String vid) {
        Vid = vid;
    }
}
