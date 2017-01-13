package com.kunzhuo.xuechelang.model;

/**
 * Created by waaa on 2016/9/2.
 * 答案bean
 */
public class Answer_Bean {

    private int A_sort;
    private String A_option;
    private String ID;
    private String A_value;
    private String A_TitleID;
    private String A_date;

    public Answer_Bean(){}

    public Answer_Bean(int a_sort, String a_option, String ID, String a_value, String a_TitleID, String a_date) {
        A_sort = a_sort;
        A_option = a_option;
        this.ID = ID;
        A_value = a_value;
        A_TitleID = a_TitleID;
        A_date = a_date;
    }

    public int getA_sort() {
        return A_sort;
    }

    public void setA_sort(int a_sort) {
        A_sort = a_sort;
    }

    public String getA_option() {
        return A_option;
    }

    public void setA_option(String a_option) {
        A_option = a_option;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getA_value() {
        return A_value;
    }

    public void setA_value(String a_value) {
        A_value = a_value;
    }

    public String getA_TitleID() {
        return A_TitleID;
    }

    public void setA_TitleID(String a_TitleID) {
        A_TitleID = a_TitleID;
    }

    public String getA_date() {
        return A_date;
    }

    public void setA_date(String a_date) {
        A_date = a_date;
    }
}
