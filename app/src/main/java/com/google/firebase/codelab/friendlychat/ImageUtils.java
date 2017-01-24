package com.google.firebase.codelab.friendlychat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Target;

/**
 * Created by zeevi on 1/25/2017.
 */

public class ImageUtils {

    public static RoundedBitmapDrawable getRoundedDrawable(
            Resources res, Bitmap bitmap) {

        RoundedBitmapDrawable imageDrawable =
                RoundedBitmapDrawableFactory.create(res, bitmap);
        imageDrawable.setCircular(true);
        imageDrawable.setCornerRadius(bitmap.getWidth());
        return imageDrawable;
    }

    public static Bitmap getSquareScaledBitmap(Uri aUri, Bitmap bitmap, int width) {


        Bitmap scaledBitmap = scaleBitmapAndKeepRation(
                rotateImageBitmap(bitmap,aUri.toString()), 500, 500);
        bitmap.recycle();

        int smallerSize = Math.min(width,
                Math.min(scaledBitmap.getWidth(), scaledBitmap.getHeight()));


        Bitmap resultBitmap = null;
        resultBitmap = Bitmap.createBitmap(scaledBitmap, 0
                , 0, smallerSize, smallerSize);
        scaledBitmap.recycle();

        return resultBitmap;

    }

    public static Bitmap scaleBitmapAndKeepRation(Bitmap TargetBmp, int reqHeightInPixels, int reqWidthInPixels) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, TargetBmp.getWidth(), TargetBmp.getHeight()), new RectF(0, 0, reqWidthInPixels, reqHeightInPixels), Matrix.ScaleToFit.CENTER);
        Bitmap scaledBitmap = Bitmap.createBitmap(TargetBmp, 0, 0, TargetBmp.getWidth(), TargetBmp.getHeight(), m, true);
        return scaledBitmap;
    }

    private static Bitmap rotateImageBitmap(Bitmap orig,
                                            String url) {

        Bitmap tmp = orig;
        float rotation = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(url);

            int exifRotation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            if (exifRotation != ExifInterface.ORIENTATION_UNDEFINED) {
                switch (exifRotation) {
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotation = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotation = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotation = 90;
                        break;
                }
            }
        } catch (IOException e) {
            Log.e("error rotation bitmap", e.getMessage());
            return tmp;
        }

        if (rotation != 0) {

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            tmp = Bitmap.createBitmap(orig, 0, 0, orig.getWidth(), orig.getHeight(), matrix, true);
            orig.recycle();
        }
        return tmp;

    }

    public static void getBitmapFromUri(Context aContext, Target aTarget, Uri aUri) {
        Picasso.with(aContext).load(aUri.toString()).into(aTarget);
    }

    public static Uri getImageUri(Context aContext, Bitmap aBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        aBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(aContext.getContentResolver(), aBitmap, "Title", null);
        return Uri.parse(path);
    }
}
