package com.google.firebase.codelab.friendlychat.wheel_controls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.codelab.friendlychat.R;

import java.util.List;

/**
 * Created by zeevi on 4/30/2017.
 */

public class SwipeDeckAdapter extends BaseAdapter {

    private List<String> data;
    private Context context;
    private OnSwipeCardListener onSwipeCardListener;

    public SwipeDeckAdapter(List<String> data, Context context,OnSwipeCardListener onSwipeCardListener) {
        this.data = data;
        this.context = context;
        this.onSwipeCardListener = onSwipeCardListener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // normally use a viewholder
            v = inflater.inflate(R.layout.test_card, parent, false);
        }

        ImageView imageView = (ImageView) v.findViewById(R.id.imageViewCardLayOutMainPic);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (onSwipeCardListener != null) {
                    float x1 = 0, x2;
                    float y1, y2;
                    switch (motionEvent.getAction()) {
                        // when user first touches the screen we get x and y coordinate
                        case MotionEvent.ACTION_DOWN: {
                            x1 = motionEvent.getX();
                            y1 = motionEvent.getY();
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            x2 = motionEvent.getX();
                            y2 = motionEvent.getY();

                            //if left to right sweep event on screen
                            if (x1 < x2) {
                                onSwipeCardListener.swipeRight();
                            }

                            // if right to left sweep event on screen
                            if (x1 > x2) {
                                onSwipeCardListener.swipeLeft();
                            }

                            // if UP to Down sweep event on screen
//                            if (y1 < y2) {
//                             //   Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
//                            }
//
//                            //if Down to UP sweep event on screen
//                            if (y1 > y2) {
//                              //  Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
//                            }
                            break;
                        }
                    }

                }
                return true;
            }
        });

        Glide.with(context)
                .load(data.get(position))
                .into(imageView);

//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String item = (String)getItem(position);
//                Log.i("MainActivity", item);
//
//            }
//        });

        return v;
    }

    public interface OnSwipeCardListener {
        void swipeLeft();
        void swipeRight();
    }
}