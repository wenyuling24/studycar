package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
public class RegionList_Bean {

    private String Pid;
    private String Pname;
    private String Psort;

    public RegionList_Bean(String pid, String pname, String psort) {
        Pid = pid;
        Pname = pname;
        Psort = psort;
    }

    public RegionList_Bean() {
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPsort() {
        return Psort;
    }

    public void setPsort(String psort) {
        Psort = psort;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }
}
