package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class MapCommentList_Bean {

    private String ID;
    private String EID;
    private String Comment;
    private String Head_portrait;
    private String Name;
    private String openid;
    private String Ctime;
    private int rowNum;

    public MapCommentList_Bean() {
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getEID() {
        return EID;
    }

    public void setEID(String EID) {
        this.EID = EID;
    }

    public String getCtime() {
        return Ctime;
    }

    public void setCtime(String ctime) {
        Ctime = ctime;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getHead_portrait() {
        return Head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        Head_portrait = head_portrait;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
