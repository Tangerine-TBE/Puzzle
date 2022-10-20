package com.weilai.jigsawpuzzle.util;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import com.luck.picture.lib.config.PictureMimeType;
import com.weilai.jigsawpuzzle.configure.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * * DATE: 2022/9/19
 * * Author:tangerine
 * * Description:
 **/
public class BitmapUtils {
    public static int calculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width < reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Observable<Bitmap> loadBitmapWithSize(String path) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                if (!TextUtils.isEmpty(path)) {
                    Uri srcUri;
                    if (PictureMimeType.isContent(path) || PictureMimeType.isHasHttp(path)) {
                        srcUri = Uri.parse(path);
                    } else {
                        srcUri = Uri.fromFile(new File(path));
                    }
                    if (srcUri != null) {
                        InputStream stream = null;
                        try {
                            stream = Config.getApplicationContext().getContentResolver().openInputStream(srcUri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                       Bitmap bitmap =  BitmapFactory.decodeStream(stream, null, options);
                       int size = bitmap.getByteCount();
                        if ( size > 1080 * 1080 * 5L) {
                            if (!bitmap.isRecycled()){
                                bitmap.recycle();
                            }
                            throw new RuntimeException("图片过大!");
                        } else {
                            emitter.onNext(bitmap);
                        }
                    }
                }
            }
        });

    }

    public static Bitmap getBitmap(Context context, int vectorDrawableId, int w, int h) {
        Bitmap bitmap = null;
//        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
        Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
//        }else {
//            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
//        }
        return bitmap;
    }

    public static Bitmap bitMapScale(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }
}
