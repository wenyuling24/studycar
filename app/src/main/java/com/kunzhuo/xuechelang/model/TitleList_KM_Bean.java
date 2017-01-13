package com.kunzhuo.xuechelang.model;

/**
 * Created by waaa on 2016/9/1.
 * 题目+正确答案 TitleList_KM Bean
 */
public class TitleList_KM_Bean {

    private String C_option; // 正确答案
    private String T_Subject;
    private String T_Remarks;
    private int T_Tagging;
    private String T_title;
    private String T_DicID_ZJ;
    private String T_Models;
    private int T_sort;
    private String C_value;
    private String T_DicID;
    private String ID;
    private String CID; //收藏ID 为空则没收藏
    private String T_option;
    private int rowNum;
    private String T_src;
    private int pageNum;
    private String RecordOption; // 用户答题记录

    public TitleList_KM_Bean() {
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getT_title() {
        return T_title;
    }

    public void setT_title(String t_title) {
        T_title = t_title;
    }

    public String getC_option() {
        return C_option;
    }

    public void setC_option(String c_option) {
        C_option = c_option;
    }

    public String getT_Subject() {
        return T_Subject;
    }

    public void setT_Subject(String t_Subject) {
        T_Subject = t_Subject;
    }

    public String getT_Remarks() {
        return T_Remarks;
    }

    public void setT_Remarks(String t_Remarks) {
        T_Remarks = t_Remarks;
    }

    public int getT_Tagging() {
        return T_Tagging;
    }

    public void setT_Tagging(int t_Tagging) {
        T_Tagging = t_Tagging;
    }

    public String getT_DicID_ZJ() {
        return T_DicID_ZJ;
    }

    public void setT_DicID_ZJ(String t_DicID_ZJ) {
        T_DicID_ZJ = t_DicID_ZJ;
    }

    public String getT_Models() {
        return T_Models;
    }

    public void setT_Models(String t_Models) {
        T_Models = t_Models;
    }

    public int getT_sort() {
        return T_sort;
    }

    public void setT_sort(int t_sort) {
        T_sort = t_sort;
    }

    public String getC_value() {
        return C_value;
    }

    public void setC_value(String c_value) {
        C_value = c_value;
    }

    public String getT_DicID() {
        return T_DicID;
    }

    public void setT_DicID(String t_DicID) {
        T_DicID = t_DicID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getT_option() {
        return T_option;
    }

    public void setT_option(String t_option) {
        T_option = t_option;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getT_src() {
        return T_src;
    }

    public void setT_src(String t_src) {
        T_src = t_src;
    }

    public String getRecordOption() {
        return RecordOption;
    }

    public void setRecordOption(String recordOption) {
        RecordOption = recordOption;
    }
}
