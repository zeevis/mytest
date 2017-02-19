package com.google.firebase.codelab.friendlychat;

import android.content.Context;
import android.util.AttributeSet;

import kankan.wheel.widget.WheelView;

/**
 * Created by zeevi on 2/18/2017.
 */

public class MyWheelView extends WheelView {
    private int wheelBackground;
    private int wheelForeground;

    public MyWheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        wheelBackground = R.color.transparent;
        wheelForeground = R.color.transparent;
    }

    public MyWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        wheelBackground = R.color.transparent;
        wheelForeground = R.color.transparent;
    }

    public MyWheelView(Context context) {
        super(context);
      //  getRootView().setBackgroundColor(getResources().getColor(R.color.transparent));
       // setWheelBackground(getResources().getColor(R.color.transparent));
        wheelBackground = R.color.transparent;
       wheelForeground = R.color.transparent;

    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override
    public void setWheelBackground(int resource) {
        //super.setWheelBackground(resource);
        wheelBackground = resource;
        wheelForeground = resource;
    }


}
