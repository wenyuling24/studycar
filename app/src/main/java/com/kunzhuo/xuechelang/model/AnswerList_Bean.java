package com.kunzhuo.xuechelang.model;

/**
 * 题库列表
 * Created by waaa on 2016/8/31.
 */
public class AnswerList_Bean {

    private String ID; // 章节ID
    private String Title; // 章节名称
    private int ZJ_Count; // 章节下的总题数
    private String Number; // 已做题数

    public AnswerList_Bean() {

    }

    public AnswerList_Bean(String ID, String title, int ZJ_Count) {
        this.ID = ID;
        this.Title = title;
        this.ZJ_Count = ZJ_Count;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getZJ_Count() {
        return ZJ_Count;
    }

    public void setZJ_Count(int ZJ_Count) {
        this.ZJ_Count = ZJ_Count;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }
}
