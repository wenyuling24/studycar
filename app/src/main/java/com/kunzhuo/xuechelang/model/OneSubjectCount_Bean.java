package com.kunzhuo.xuechelang.model;

/**
 * Created by waaa on 2016/9/12.
 *  科目统计
 */
public class OneSubjectCount_Bean {

    private int Subject;
    private int Error;
    private int ColnTo;
    private int Order;
    private int random;
    private int Special;
    private int TrafficSign;
    private int Regulations;

    public OneSubjectCount_Bean() {
    }

    public int getRegulations() {
        return Regulations;
    }

    public void setRegulations(int regulations) {
        Regulations = regulations;
    }

    public int getTrafficSign() {
        return TrafficSign;
    }

    public void setTrafficSign(int trafficSign) {
        TrafficSign = trafficSign;
    }

    public int getError() {
        return Error;
    }

    public void setError(int error) {
        Error = error;
    }

    public int getSubject() {
        return Subject;
    }

    public void setSubject(int subject) {
        Subject = subject;
    }

    public int getColnTo() {
        return ColnTo;
    }

    public void setColnTo(int colnTo) {
        ColnTo = colnTo;
    }

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }

    public int getRandom() {
        return random;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    public int getSpecial() {
        return Special;
    }

    public void setSpecial(int special) {
        Special = special;
    }
}
