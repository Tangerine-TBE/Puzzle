package com.weilai.jigsawpuzzle.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

/**
 ** DATE: 2022/9/19
 ** Author:tangerine
 ** Description:
 **/
public class ScreenShotUtil {

        @SuppressWarnings("deprecation")
        private static Bitmap takeScreenShot(Activity activity) {
            // View是你需要截图的View,这样保存的是当前APP的整个截图，因此使用DecorView，如果是某个子控件，可以通过findViewById找到对应的View
            View view = activity.getWindow().getDecorView();
            boolean isCacheEnable = view.isDrawingCacheEnabled();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap bmp = view.getDrawingCache();
            // 获取状态栏高度
            Rect frameRect = new Rect();
            view.getWindowVisibleDisplayFrame(frameRect);
            int statusBarHeight = frameRect.top;
            // 获取屏幕长和高
            int width;
            int height;
            Display display = activity.getWindowManager().getDefaultDisplay();
            if (Build.VERSION.SDK_INT > 12) {
                Point point = new Point();
                display.getSize(point);
                width = point.x;
                height = point.y;
            } else {
                width = display.getWidth();
                height = display.getHeight();
            }
            // 去掉标题栏，DecorView是不包含标题栏的
            Bitmap bmpScreenshot = Bitmap.createBitmap(bmp, 0,
                    statusBarHeight, width, height - statusBarHeight);
            view.destroyDrawingCache();
            view.setDrawingCacheEnabled(isCacheEnable);
            return bmpScreenshot;
        }

        //保存指定View的截图，与保存Activity的截图类似
        public static String screenshot(View view, String name) {
            if (view == null) {
                return null;
            }
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
            Canvas c = new Canvas(bitmap);
//        view.layout(0, 0, view.getLayoutParams().width, view.getLayoutParams().height);
            view.draw(c);

//        view.setDrawingCacheEnabled(true);
//        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//        view.buildDrawingCache();
//        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(),
//                view.getMeasuredHeight());
//        view.setDrawingCacheEnabled(false);
//        view.destroyDrawingCache();


            try {
                return saveScreenShot(bitmap, "weilai", name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static String saveScreenShot(Bitmap bitmap, String fileDirStr, String fileNameStr) throws Exception {
            FileOutputStream fos;
            File file;
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fileDirStr;
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
            filePath = filePath + "/" + fileNameStr + ".png";
            fos = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            try {
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fos.close();
            }
            return filePath;
        }

        public static byte[] getBmpToByte(Bitmap bitmap) {

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int[] pixels = new int[w * h];
            bitmap.getPixels(pixels, 0, w, 0, 0, w, h);
            byte[] rgb = addBMP_RGB_888(pixels, w, h);
            byte[] header = addBMPImageHeader(rgb.length);
            byte[] infos = addBMPImageInfosHeader(rgb.length, w, h);

            byte[] buffer = new byte[54 + rgb.length];
            System.arraycopy(header, 0, buffer, 0, header.length);
            System.arraycopy(infos, 0, buffer, 14, infos.length);
            System.arraycopy(rgb, 0, buffer, 54, rgb.length);

            return buffer;
        }

        private static byte[] addBMP_RGB_888(int[] b, int w, int h) {
            int len = b.length;
            System.out.println(b.length);
            byte[] buffer = new byte[w * h * 3];
            int offset = 0;

            for (int i = len - 1; i >= w - 1; i -= w) {
                int end = i, start = i - w + 1;
                for (int j = start; j <= end; j++) {
                    buffer[offset] = (byte) (b[j]);
                    buffer[offset + 1] = (byte) (b[j] >> 8);
                    buffer[offset + 2] = (byte) (b[j] >> 16);
                    offset += 3;
                }
            }

            return buffer;

        }

        private static byte[] addBMPImageHeader(int size) {
            byte[] buffer = new byte[14];
            buffer[0] = 0x42;
            buffer[1] = 0x4D;
            buffer[2] = (byte) ((size + 54) >> 0);
            buffer[3] = (byte) ((size + 54) >> 8);
            buffer[4] = (byte) ((size + 54) >> 16);
            buffer[5] = (byte) ((size + 54) >> 24);
            buffer[6] = 0x00;
            buffer[7] = 0x00;
            buffer[8] = 0x00;
            buffer[9] = 0x00;
            buffer[10] = 0x36;
            buffer[11] = 0x00;
            buffer[12] = 0x00;
            buffer[13] = 0x00;
            return buffer;
        }

        private static byte[] addBMPImageInfosHeader(int size, int w, int h) {
            byte[] buffer = new byte[40];

            // 这个是固定的 BMP 信息头要40个字节
            buffer[0] = 0x28;
            buffer[1] = 0x00;
            buffer[2] = 0x00;
            buffer[3] = 0x00;

            // 宽度 地位放在序号前的位置 高位放在序号后的位置
            buffer[4] = (byte) (w >> 0);
            buffer[5] = (byte) (w >> 8);
            buffer[6] = (byte) (w >> 16);
            buffer[7] = (byte) (w >> 24);

            // 长度 同上
            buffer[8] = (byte) (h >> 0);
            buffer[9] = (byte) (h >> 8);
            buffer[10] = (byte) (h >> 16);
            buffer[11] = (byte) (h >> 24);

            // 总是被设置为1
            buffer[12] = 0x01;
            buffer[13] = 0x00;

            // 比特数 像素 32位保存一个比特 这个不同的方式(ARGB 32位 RGB24位不同的!!!!
            buffer[14] = 0x18;
            buffer[15] = 0x00;

            // 0-不压缩 1-8bit位图
            // 2-4bit位图 3-16/32位图
            // 4 jpeg 5 png
            buffer[16] = 0x00;
            buffer[17] = 0x00;
            buffer[18] = 0x00;
            buffer[19] = 0x00;

            // 说明图像大小
            buffer[20] = (byte) size;
            buffer[21] = (byte) (size >> 8);
            buffer[22] = (byte) (size >> 16);
            buffer[23] = (byte) (size >> 24);

            // 水平分辨率
            buffer[24] = 0x00;
            buffer[25] = 0x00;
            buffer[26] = 0x00;
            buffer[27] = 0x00;

            // 垂直分辨率
            buffer[28] = 0x00;
            buffer[29] = 0x00;
            buffer[30] = 0x00;
            buffer[31] = 0x00;

            // 0 使用所有的调色板项
            buffer[32] = 0x00;
            buffer[33] = 0x00;
            buffer[34] = 0x00;
            buffer[35] = 0x00;

            // 不开颜色索引
            buffer[36] = 0x00;
            buffer[37] = 0x00;
            buffer[38] = 0x00;
            buffer[39] = 0x00;
            return buffer;
        }
}
