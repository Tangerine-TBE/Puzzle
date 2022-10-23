package com.weilai.jigsawpuzzle.util;

import android.Manifest;
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

import com.luck.picture.lib.config.PictureMimeType;
import com.weilai.jigsawpuzzle.BaseConstant;
import com.weilai.jigsawpuzzle.configure.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    public static String saveImageToGallery(Bitmap mBitmap) {
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
            return "";
        }
        saveBitmapPhoto(mBitmap);

        return file.getAbsolutePath();

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

    public static void saveImageToGallery(Bitmap mBitmap, String fileDir, String fileName, boolean toGallery) {
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

    public static Bitmap getBitmapFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = Config.getApplicationContext().getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void saveBitmapPhoto(Bitmap bm) {
        ContentResolver resolver = Config.getApplicationContext().getContentResolver();
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
                        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
                        int size = bitmap.getByteCount();
                        if (size > 1080 * 1080 * 5L) {
                            if (!bitmap.isRecycled()) {
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

    public static boolean shouldLoadBitmap(String path, boolean vertical) {
        Uri srcUri;
        if (PictureMimeType.isContent(path) || PictureMimeType.isHasHttp(path)) {
            srcUri = Uri.parse(path);
        } else {
            srcUri = Uri.fromFile(new File(path));
        }
        InputStream stream = null;
        try {
            stream = Config.getApplicationContext().getContentResolver().openInputStream(srcUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, null, options);
        if (vertical) {
            return options.outHeight <= DimenUtil.getScreenHeight() * 3;
        } else {
            return options.outWidth <= DimenUtil.getScreenWidth() * 3;
        }
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
    public static Uri pathToUri(String path){
        Uri srcUri;
        if (PictureMimeType.isContent(path) || PictureMimeType.isHasHttp(path)) {
            srcUri = Uri.parse(path);
        } else {
            srcUri = Uri.fromFile(new File(path));
        }
        return srcUri;
    }
    public static Bitmap bitMapScale(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }
}
