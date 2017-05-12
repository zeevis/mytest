package com.google.firebase.codelab.friendlychat.matches;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.View;

import com.google.firebase.codelab.friendlychat.R;

/**
 * Created by zeevi on 5/11/2017.
 */

public class BadgedDrawerArrowDrawable extends DrawerArrowDrawable {
    Context mContext;
    /**
     * @param context used to get the configuration for the drawable from
     */
    public BadgedDrawerArrowDrawable(Context context) {
        super(context);
        mContext = context;
        setColor(context.getResources().getColor(R.color.colorTitle));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mContext.getResources().getColor(R.color.colorPrimary));
        canvas.drawCircle((float) (this.getIntrinsicWidth()/1.4),this.getIntrinsicHeight()/3,(float) (this.getGapSize()*1.5),paint);
//        paint.setTextSize(60);
//        canvas.drawText("!",0, View.TEXT_ALIGNMENT_CENTER, paint);
    }
}