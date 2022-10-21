package com.steelkiwi.cropiwa.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.OutputStream;


/**
 * * DATE: 2022/9/19
 * * Author:tangerine
 * * Description:
 **/
public class BitmapUtils {
    public static void saveBitmapPhoto(Context context,Bitmap bm) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/抠图");
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, false);
        }
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis());
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (uri != null) {
            try (OutputStream ops = resolver.openOutputStream(uri)) {
                bm.compress(Bitmap.CompressFormat.JPEG, 100, ops);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
