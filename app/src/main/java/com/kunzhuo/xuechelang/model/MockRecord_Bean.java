package com.kunzhuo.xuechelang.model;

/**
 * Created by waaa on 2016/9/19.
 * 考试记录对象
 */
public class MockRecord_Bean {

    private int Fraction; // 分数
    private int ExamTime; // 本次考试花费时间
    private String RanDate; // 考试提交的时间

    public MockRecord_Bean() {
    }

    public int getFraction() {
        return Fraction;
    }

    public void setFraction(int fraction) {
        Fraction = fraction;
    }

    public int getExamTime() {
        return ExamTime;
    }

    public void setExamTime(int examTime) {
        ExamTime = examTime;
    }

    public String getRanDate() {
        return RanDate;
    }

    public void setRanDate(String ranDate) {
        RanDate = ranDate;
    }
}
