package com.google.firebase.codelab.friendlychat;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by zeevi on 11/18/2016.
 */

public class DialogUtils {

    public static void createDialog(Context c, String message, final Interfaces.basicListener basicListener, String positiveButtonText,String negetiveButtonText){
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialog = inflater.inflate(R.layout.basic_dialog, null);

        TextView messageTextView = (TextView) dialog.findViewById(R.id.messagePlaceHolderTextView);
        Button yesButton = (Button) dialog.findViewById(R.id.yesButton);
        Button noButton = (Button) dialog.findViewById(R.id.noButton);

        messageTextView.setText(message);
        builder.setView(dialog);
        final AlertDialog mAlert = builder.create();
        mAlert.setCanceledOnTouchOutside(true);
        mAlert.setCancelable(true);

        if(positiveButtonText != null){
            yesButton.setText(positiveButtonText);
        }

        if(negetiveButtonText != null){
            noButton.setText(negetiveButtonText);
        }
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicListener.onSuccess();
                mAlert.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicListener.onError();
                mAlert.dismiss();
            }
        });
        mAlert.show();
    }

    public static void createDialog(Context c, String message, final Interfaces.basicListener basicListener){
        createDialog( c,  message,  basicListener, null,null);
    }
}
