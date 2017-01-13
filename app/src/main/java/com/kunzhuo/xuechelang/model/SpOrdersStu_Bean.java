package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/11/7 0007.
 */
public class SpOrdersStu_Bean {

    private String ProMoney;
    private String ProName;
    private String LearnTime;
    private String ProType;
    private String OrdAddress;
    private String mg_address;
    private String Uname;
    private String Uaccount;
    private int Whether;

    public SpOrdersStu_Bean() {
    }

    public int getWhether() {
        return Whether;
    }

    public void setWhether(int whether) {
        Whether = whether;
    }

    public String getProMoney() {
        return ProMoney;
    }

    public void setProMoney(String proMoney) {
        ProMoney = proMoney;
    }

    public String getProName() {
        return ProName;
    }

    public void setProName(String proName) {
        ProName = proName;
    }

    public String getLearnTime() {
        return LearnTime;
    }

    public void setLearnTime(String learnTime) {
        LearnTime = learnTime;
    }

    public String getProType() {
        return ProType;
    }

    public void setProType(String proType) {
        ProType = proType;
    }

    public String getOrdAddress() {
        return OrdAddress;
    }

    public void setOrdAddress(String ordAddress) {
        OrdAddress = ordAddress;
    }

    public String getUname() {
        return Uname;
    }

    public void setUname(String uname) {
        Uname = uname;
    }

    public String getMg_address() {
        return mg_address;
    }

    public void setMg_address(String mg_address) {
        this.mg_address = mg_address;
    }

    public String getUaccount() {
        return Uaccount;
    }

    public void setUaccount(String uaccount) {
        Uaccount = uaccount;
    }
}
