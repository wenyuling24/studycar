package com.kunzhuo.xuechelang.utils.eventutils;

/**
 * Created by waaa on 2016/9/14.
 * 传输的答题状态
 */
public class RoardAnswer_Event {

    private String ID; // 题目Id
    private boolean state; // 答题对错状态

    public RoardAnswer_Event() {
    }

    public RoardAnswer_Event(String ID, boolean state) {
        this.ID = ID;
        this.state = state;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
