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
 * * DATE: 2022/9/19
 * * Author:tangerine
 * * Description:
 **/
public class FileUtil {
    private static final String BASE_CACHE_PATH = getApplicationContext().getDatabasePath("puzzle").getAbsolutePath();

    public static String saveBitmapToCache(String fileName, Bitmap bitmap) {

        File file = new File(BASE_CACHE_PATH);
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
        return bitmapFile.getAbsolutePath();
    }

    public static Bitmap getBitmapFromCache(String fileName) {
        Bitmap bitmap = null;
        File file = new File(BASE_CACHE_PATH);
        if (!file.exists() || !file.isDirectory()) {
            return null;
        }
        File bitmapFile = new File(BASE_CACHE_PATH, fileName);
        if (bitmapFile.exists()) {
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(bitmapFile));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    public static final String filePath = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/Output";
    public static String saveScreenShot(Bitmap bitmap, String fileNameStr) throws Exception {
        FileOutputStream fos;
        File file;
        file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = filePath+"/" + fileNameStr + ".jpeg";
        fos = new FileOutputStream(path);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        try {
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        savePhotoAlbum(bitmap, new File(path));
        return path;
    }
    @Deprecated
    public static String saveScreenShot(Bitmap bitmap, String fileNameStr,int quality) throws Exception {
        FileOutputStream fos;
        File file;
        File externalFileRootDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);


        String saveDir = Objects.requireNonNull(externalFileRootDir).getAbsolutePath();
        String filePath = saveDir + "/OutPut";
        file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        filePath = filePath + "/" + fileNameStr + ".jpeg";
        fos = new FileOutputStream(filePath);
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, fos);
        try {
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        savePhotoAlbum(bitmap, new File(filePath));
        return filePath;
    }

    public static String getAnPicPath(String fileNameStr) {
        File externalFileRootDir = getApplicationContext().getExternalFilesDir(null);
        do {
            externalFileRootDir = Objects.requireNonNull(externalFileRootDir).getParentFile();
        } while (Objects.requireNonNull(externalFileRootDir).getAbsolutePath().contains("/Android"));
        String saveDir = Objects.requireNonNull(externalFileRootDir).getAbsolutePath();
        String filePath = saveDir + "/" + Environment.DIRECTORY_DCIM;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        filePath = filePath + "/" + fileNameStr + ".jpeg";
        return filePath;
    }

    private static void savePhotoAlbum(Bitmap src, File file) {

        //先保存到文件
        OutputStream outputStream;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            src.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //再更新图库
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getName());
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/拼图抠图");
            values.put(MediaStore.MediaColumns.IS_PENDING, false);
            ContentResolver contentResolver = getApplicationContext().getContentResolver();
            Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri == null) {
                return;
            }
            try {
                outputStream = contentResolver.openOutputStream(uri);
                src.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
//                FileInputStream fileInputStream = new FileInputStream(file);
//                FileUtils.copy(fileInputStream, outputStream);
//                fileInputStream.close();
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
        if (!src.isRecycled()) {
            src.recycle();
        }
    }
}
