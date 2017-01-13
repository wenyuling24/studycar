package com.kunzhuo.xuechelang.model;

/**
 * Created by Administrator on 2016/11/5 0005.
 */
public class SpOrders_Bean {

    private String ProMoney;
    private String ProName;
    private String LearnTime;
    private String ProType;
    private String OrdAddress;
    private String mg_address;
    private String Name;
    private String Telephone;

    public SpOrders_Bean() {
    }

    public String getProName() {
        return ProName;
    }

    public void setProName(String proName) {
        ProName = proName;
    }

    public String getProMoney() {
        return ProMoney;
    }

    public void setProMoney(String proMoney) {
        ProMoney = proMoney;
    }

    public String getOrdAddress() {
        return OrdAddress;
    }

    public void setOrdAddress(String ordAddress) {
        OrdAddress = ordAddress;
    }

    public String getProType() {
        return ProType;
    }

    public void setProType(String proType) {
        ProType = proType;
    }

    public String getLearnTime() {
        return LearnTime;
    }

    public void setLearnTime(String learnTime) {
        LearnTime = learnTime;
    }

    public String getMg_address() {
        return mg_address;
    }

    public void setMg_address(String mg_address) {
        this.mg_address = mg_address;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
