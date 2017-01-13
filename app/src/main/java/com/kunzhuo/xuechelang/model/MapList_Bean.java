package com.kunzhuo.xuechelang.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/11 0011.
 * 当前用户周围3公里用户 MapList
 */
public class MapList_Bean implements Serializable {
    private static final long serialVersionUID = -758459502806858414L;

    private String ID;
    private String Name;
    private String Head_portrait;
    private String Telephone;
    private String CoachType;
    private String Driver_school;
    private String Site_address;
    private String CurrentAddress;
    private double X;
    private double Y;
    private String Province;
    private String City;
    private String Area;
    private String httpImg;

    public MapList_Bean() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHead_portrait() {
        return Head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        Head_portrait = head_portrait;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCoachType() {
        return CoachType;
    }

    public void setCoachType(String coachType) {
        CoachType = coachType;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getDriver_school() {
        return Driver_school;
    }

    public void setDriver_school(String driver_school) {
        Driver_school = driver_school;
    }

    public String getSite_address() {
        return Site_address;
    }

    public void setSite_address(String site_address) {
        Site_address = site_address;
    }

    public String getCurrentAddress() {
        return CurrentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        CurrentAddress = currentAddress;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getHttpImg() {
        return httpImg;
    }

    public void setHttpImg(String httpImg) {
        this.httpImg = httpImg;
    }
}
