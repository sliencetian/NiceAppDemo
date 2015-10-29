package com.tz.niceappdemo.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ViewPagerScroller extends Scroller {
    private int mDuration;

    public ViewPagerScroller(Context context) {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }


    public void setDuration(int duration) {
        this.mDuration = duration;
    }


    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, this.mDuration);
    }
}