package com.tz.niceappdemo.bean;

import java.io.Serializable;

public class Card implements Serializable {
    private static final long serialVersionUID = -5376313495678563362L;

    private int backgroundColor;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}