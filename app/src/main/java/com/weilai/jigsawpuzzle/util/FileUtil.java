package com.weilai.jigsawpuzzle.util;

import static com.weilai.jigsawpuzzle.configure.Config.getApplicationContext;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.weilai.jigsawpuzzle.configure.Config;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 ** DATE: 2022/9/19
 ** Author:tangerine
 ** Description:
 **/
public class FileUtil {
    private static final String BASE_PATH = getApplicationContext().getDatabasePath("template").getAbsolutePath() ;
    public static void saveBitmapToCache(String fileName, Bitmap bitmap) {
        File file = new File(BASE_PATH);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
        File bitmapFile = new File(file.getAbsolutePath(), fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Bitmap getBitmapFromCache(String fileName){
        Bitmap bitmap = null;
        File file = new File(BASE_PATH);
        if (!file.exists() || !file.isDirectory()) {
            return null;
        }
        File bitmapFile = new File(BASE_PATH,fileName);
        if (bitmapFile.exists()){
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(bitmapFile));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static String saveScreenShot(Bitmap bitmap, String fileNameStr) throws Exception {
        FileOutputStream fos;
        File file;
        File externalFileRootDir = getApplicationContext(). getExternalFilesDir(null);
        do {
            externalFileRootDir = Objects.requireNonNull(externalFileRootDir).getParentFile();
        } while (Objects.requireNonNull(externalFileRootDir).getAbsolutePath().contains("/Android"));

        String saveDir = Objects.requireNonNull(externalFileRootDir).getAbsolutePath();
        String filePath = saveDir + "/" + Environment.DIRECTORY_DCIM ;
        file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        filePath = filePath + "/" + fileNameStr + ".jpg";
        fos = new FileOutputStream(filePath);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        try {
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        savePhotoAlbum(bitmap,new File(filePath));

        return filePath;
    }
    private static void savePhotoAlbum(Bitmap src, File file) {

        //先保存到文件
        OutputStream outputStream;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            src.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            if (!src.isRecycled()) {
                src.recycle();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //再更新图库
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getName());
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
            ContentResolver contentResolver = getApplicationContext().getContentResolver();
            Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  values);
            if (uri == null) {
                return;
            }
            try {
                outputStream = contentResolver.openOutputStream(uri);
                FileInputStream fileInputStream = new FileInputStream(file);
                FileUtils.copy(fileInputStream, outputStream);
                fileInputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            MediaScannerConnection.scanFile(
                    getApplicationContext(),
                    new String[]{file.getAbsolutePath()},
                    new String[]{"image/jpeg"},
                    (path, uri) -> {
                        // Scan Completed
                    });
        }
    }
}
