package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/10/8 0008.
 */
public class VideoType_Bean {

    private String Pid; // 分类ID
    private String Pname; // 分类名称
    private String Pval;
    private int Psort;
    private String Psuperior;

    public VideoType_Bean() {
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public int getPsort() {
        return Psort;
    }

    public void setPsort(int psort) {
        Psort = psort;
    }

    public String getPsuperior() {
        return Psuperior;
    }

    public void setPsuperior(String psuperior) {
        Psuperior = psuperior;
    }

    public String getPval() {
        return Pval;
    }

    public void setPval(String pval) {
        Pval = pval;
    }
}
