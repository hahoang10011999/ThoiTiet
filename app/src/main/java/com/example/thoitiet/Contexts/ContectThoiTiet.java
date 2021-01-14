package com.example.thoitiet.Contexts;

import java.util.Date;

public class ContectThoiTiet {
    String main,max,min,day;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ContectThoiTiet(String main, String max, String min, String day) {
        this.main = main;
        this.max = max;
        this.min = min;
        this.day = day;
    }

    public ContectThoiTiet(){

    }
}
