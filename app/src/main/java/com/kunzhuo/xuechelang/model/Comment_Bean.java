package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/9/27 0027.
 * 评论对象
 */
public class Comment_Bean {

    private String Cid;
    private String Content;
    private String Uid;
    private String superior;
    private String Video_Id;
    private String Ctime;
    private int Sort;
    private String Uaccount;
    private String Unickname;
    private String Pic;
    private int rowNum;
    private String httpImg;

    public Comment_Bean() {
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getVideo_Id() {
        return Video_Id;
    }

    public void setVideo_Id(String video_Id) {
        Video_Id = video_Id;
    }

    public String getCtime() {
        return Ctime;
    }

    public void setCtime(String ctime) {
        Ctime = ctime;
    }

    public String getUaccount() {
        return Uaccount;
    }

    public void setUaccount(String uaccount) {
        Uaccount = uaccount;
    }

    public String getUnickname() {
        return Unickname;
    }

    public void setUnickname(String unickname) {
        Unickname = unickname;
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }
}
