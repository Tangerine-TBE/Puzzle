package com.feisukj.base.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import androidx.annotation.RequiresApi;

import com.feisukj.base.BaseApplication;
import com.feisukj.base.BaseConstant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapUtils {
    public static final String savePath = BaseApplication.application.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/Drawing";
    //改变颜色
    public static Bitmap setImage(Bitmap mBitmap, int newA, int newR, int newG, int newB) {
        Bitmap bitmap = null;
        if (mBitmap != null) {
            bitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
            int mWidth = bitmap.getWidth();
            int mHeight = bitmap.getHeight();
            for (int i = 0; i < mHeight; i++) {
                for (int j = 0; j < mWidth; j++) {
                    int color = bitmap.getPixel(j, i);
                    int g = Color.green(color);
                    int r = Color.red(color);
                    int b = Color.blue(color);
                    int a = Color.alpha(color);
                    if (a != 0) {
//                        a = newA;
                        g = newG;
                        r = newR;
                        b = newB;
                        color = Color.argb(a, r, g, b);
                        bitmap.setPixel(j, i, color);
                    }
                }
            }
        }
        return bitmap;
    }

    public static Bitmap getBitmapToBg(Context context, int vectorDrawableId, int w, int h) {
        Bitmap bitmap=null;
        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP){
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            int drawableW = vectorDrawable.getIntrinsicWidth();
            int drawableH = vectorDrawable.getIntrinsicHeight();
            float scale1 = w*1f/drawableW;
            float scale2 = h*1f/drawableH;
            float scale = Math.max(scale1,scale2);
            int dw = (int) (drawableW*scale);
            int dh = (int) (drawableH*scale);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds((w - dw)/2, (h - dh)/2, (w+dw)/2, (h+dh)/2);
            vectorDrawable.draw(canvas);
        }else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }

    public static Bitmap getBitmap(Context context, int vectorDrawableId, int w, int h){
        Bitmap bitmap=null;
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

    public static Bitmap getBitmap(String file,int w,int h) {
        Bitmap bitmap=null;
        try {
            Drawable vectorDrawable = Drawable.createFromPath(file);
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap bitMapScale(Bitmap bitmap,float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale,scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }

    public static Bitmap getScaleBitmap(Context context, int vectorDrawableId,int w,int h){
        Bitmap bitmap=null;
        try{
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            int drawableW = vectorDrawable.getIntrinsicWidth();
            int drawableH = vectorDrawable.getIntrinsicHeight();
            float scale1 = w*1f/drawableW;
            float scale2 = h*1f/drawableH;
            float scale = Math.min(scale1,scale2);
            int dw = (int) (drawableW*scale);
            int dh = (int) (drawableH*scale);
            vectorDrawable.setBounds((w - dw)/2, (h - dh)/2, (w+dw)/2, (h+dh)/2);
            vectorDrawable.draw(canvas);
        }catch (Exception e){
            e.printStackTrace();
        }
        return setWhiteBg(bitmap);
    }

    public static Bitmap getBitmapFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = BaseApplication.getApplication().getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private static Bitmap setWhiteBg(Bitmap mBitmap){
        Bitmap bitmap = null;
        if (mBitmap != null) {
            bitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
            int mWidth = bitmap.getWidth();
            int mHeight = bitmap.getHeight();
            for (int i = 0; i < mHeight; i++) {
                for (int j = 0; j < mWidth; j++) {
                    int color = bitmap.getPixel(j, i);
                    int a = Color.alpha(color);
                    if (a > 30) {
                        color = Color.argb(a, 168,168,168);
                    }
                    else{
                        color = Color.argb(255, 255,255,255);
                    }
                    bitmap.setPixel(j, i, color);
                }
            }
        }
        return bitmap;
    }

    /**
     * 保存图片
     */
    public static void saveImageToGallery(Bitmap mBitmap) {
        // 首先保存图片
//        String appDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/Drawing";
        File appDir = new File(BaseConstant.INSTANCE.getSavePath());
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        saveBitmapPhoto(mBitmap);

        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(BaseApplication.getApplication().getContentResolver(), file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        // 最后通知图库更新

//        BaseApplication.getApplication().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+file.getAbsolutePath())));
//        MediaScannerConnection.scanFile(BaseApplication.getApplication(),new String[]{file.getAbsolutePath()},
//                new String[]{"image/*"},
//                new MediaScannerConnection.OnScanCompletedListener() {
//                    @Override
//                    public void onScanCompleted(String path, Uri uri) {
//
//                    }
//                });

//        MediaScanner mediaScanner = new MediaScanner(BaseApplication.getApplication());
//        String[] filePaths = new String[]{file.getAbsolutePath()};
//        String[] mimeTypes = new String[]{MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")};
//        mediaScanner.scanFiles(filePaths, mimeTypes);

    }
    public static void saveImageToGallery(Bitmap mBitmap,String fileDir,String fileName,boolean toGallery) {
        // 首先保存图片
//        String appDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/Drawing";
        File file = new File(fileDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (toGallery)
            saveBitmapPhoto(mBitmap);

        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(BaseApplication.getApplication().getContentResolver(), file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        // 最后通知图库更新

//        BaseApplication.getApplication().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+file.getAbsolutePath())));
//        MediaScannerConnection.scanFile(BaseApplication.getApplication(),new String[]{file.getAbsolutePath()},
//                new String[]{"image/*"},
//                new MediaScannerConnection.OnScanCompletedListener() {
//                    @Override
//                    public void onScanCompleted(String path, Uri uri) {
//
//                    }
//                });

//        MediaScanner mediaScanner = new MediaScanner(BaseApplication.getApplication());
//        String[] filePaths = new String[]{file.getAbsolutePath()};
//        String[] mimeTypes = new String[]{MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")};
//        mediaScanner.scanFiles(filePaths, mimeTypes);

    }

    /**
     * 保存图片到相册(适配安卓11)
     */
    public static void saveBitmapPhoto(Bitmap bm) {
        ContentResolver resolver = BaseApplication.getApplication().getContentResolver();
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

//    /**
//     * 保存图片到相册(适配安卓11)
//     */
//    fun saveBitmapPhoto(bm: Bitmap) {
//        val resolver = BaseApplication.getApplication().contentResolver
//        val contentValues = ContentValues().apply {
//            put(MediaStore.MediaColumns.DISPLAY_NAME, "抠图/${System.currentTimeMillis()}")
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//        }
//        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//        if (uri != null) {
//            resolver.openOutputStream(uri).use {
//                bm.compress(Bitmap.CompressFormat.JPEG, 100, it)
//                ToastUtil.showCenterToast("保存成功")
//            }
//        }
//
//    }
}
