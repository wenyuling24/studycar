package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class Brand_Bean {
    private String Pid;
    private String Pname;

    public Brand_Bean(String pid, String pname) {
        Pid = pid;
        Pname = pname;
    }

    public Brand_Bean() {
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }
}
