package com.xinlan.imageeditlibrary.editimage.utils;


import android.text.TextUtils;

/**
 * @author：luck
 * @date：2017-5-24 17:02
 * @describe：PictureMimeType
 */

public final class PictureMimeType {

    /**
     * isGif
     *
     * @param mimeType
     * @return
     */
    public static boolean isHasGif(String mimeType) {
        return mimeType != null && (mimeType.equals("image/gif") || mimeType.equals("image/GIF"));
    }

    /**
     * isGif
     *
     * @param url
     * @return
     */
    public static boolean isUrlHasGif(String url) {
        return url.toLowerCase().endsWith(".gif");
    }

    /**
     * is has image
     *
     * @param url
     * @return
     */
    public static boolean isUrlHasImage(String url) {
        return url.toLowerCase().endsWith(".jpg")
                || url.toLowerCase().endsWith(".jpeg")
                || url.toLowerCase().endsWith(".png")
                || url.toLowerCase().endsWith(".heic");
    }

    /**
     * isWebp
     *
     * @param mimeType
     * @return
     */
    public static boolean isHasWebp(String mimeType) {
        return mimeType != null && mimeType.equalsIgnoreCase("image/webp");
    }

    /**
     * isWebp
     *
     * @param url
     * @return
     */
    public static boolean isUrlHasWebp(String url) {
        return url.toLowerCase().endsWith(".webp");
    }

    /**
     * isVideo
     *
     * @param mimeType
     * @return
     */
    public static boolean isHasVideo(String mimeType) {
        return mimeType != null && mimeType.startsWith(MIME_TYPE_PREFIX_VIDEO);
    }

    /**
     * isVideo
     *
     * @param url
     * @return
     */
    public static boolean isUrlHasVideo(String url) {
        return url.toLowerCase().endsWith(".mp4");
    }

    /**
     * isAudio
     *
     * @param mimeType
     * @return
     */
    public static boolean isHasAudio(String mimeType) {
        return mimeType != null && mimeType.startsWith(MIME_TYPE_PREFIX_AUDIO);
    }

    /**
     * isAudio
     *
     * @param url
     * @return
     */
    public static boolean isUrlHasAudio(String url) {
        return url.toLowerCase().endsWith(".amr") || url.toLowerCase().endsWith(".mp3");
    }

    /**
     * isImage
     *
     * @param mimeType
     * @return
     */
    public static boolean isHasImage(String mimeType) {
        return mimeType != null && mimeType.startsWith(MIME_TYPE_PREFIX_IMAGE);
    }

    /**
     * isHasBmp
     *
     * @param mimeType
     * @return
     */
    public static boolean isHasBmp(String mimeType) {
        if (TextUtils.isEmpty(mimeType)) {
            return false;
        }
        return mimeType.startsWith(PictureMimeType.ofBMP())
                || mimeType.startsWith(PictureMimeType.ofXmsBMP())
                || mimeType.startsWith(PictureMimeType.ofWapBMP());
    }


    public static boolean isJPEG(String mimeType) {
        if (TextUtils.isEmpty(mimeType)) {
            return false;
        }
        return mimeType.startsWith(MIME_TYPE_JPEG) || mimeType.startsWith(MIME_TYPE_JPG);
    }


    public static boolean isJPG(String mimeType) {
        if (TextUtils.isEmpty(mimeType)) {
            return false;
        }
        return mimeType.startsWith(MIME_TYPE_JPG);
    }


    /**
     * is Network image
     *
     * @param path
     * @return
     */
    public static boolean isHasHttp(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return path.startsWith("http") || path.startsWith("https");
    }


    /**
     * Picture or video
     *
     * @return
     */

    /**
     * Get image suffix
     *
     * @param mineType
     * @return
     */
    public static String getLastImgSuffix(String mineType) {
        try {
            return mineType.substring(mineType.lastIndexOf("/")).replace("/", ".");
        } catch (Exception e) {
            e.printStackTrace();
            return JPG;
        }
    }

    /**
     * Get url to file name
     *
     * @param path
     * @return
     */
    public static String getUrlToFileName(String path) {
        String result = "";
        try {
            int lastIndexOf = path.lastIndexOf("/");
            if (lastIndexOf != -1) {
                result = path.substring(lastIndexOf + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * is content://
     *
     * @param url
     * @return
     */
    public static boolean isContent(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return url.startsWith("content://");
    }


    public static String ofPNG() {
        return MIME_TYPE_PNG;
    }

    public static String ofJPEG() {
        return MIME_TYPE_JPEG;
    }

    public static String ofBMP() {
        return MIME_TYPE_BMP;
    }

    public static String ofXmsBMP() {
        return MIME_TYPE_XMS_BMP;
    }

    public static String ofWapBMP() {
        return MIME_TYPE_WAP_BMP;
    }

    public static String ofGIF() {
        return MIME_TYPE_GIF;
    }

    public static String ofWEBP() {
        return MIME_TYPE_WEBP;
    }

    public static String of3GP() {
        return MIME_TYPE_3GP;
    }

    public static String ofMP4() {
        return MIME_TYPE_MP4;
    }

    public static String ofMPEG() {
        return MIME_TYPE_MPEG;
    }

    public static String ofAVI() {
        return MIME_TYPE_AVI;
    }


    public final static String MIME_TYPE_IMAGE = "image/jpeg";
    public final static String MIME_TYPE_VIDEO = "video/mp4";
    public final static String MIME_TYPE_AUDIO = "audio/mpeg";
    public final static String MIME_TYPE_AUDIO_AMR = "audio/amr";

    public final static String MIME_TYPE_PREFIX_IMAGE = "image";
    public final static String MIME_TYPE_PREFIX_VIDEO = "video";
    public final static String MIME_TYPE_PREFIX_AUDIO = "audio";

    private final static String MIME_TYPE_PNG = "image/png";
    public final static String MIME_TYPE_JPEG = "image/jpeg";
    private final static String MIME_TYPE_JPG = "image/jpg";
    private final static String MIME_TYPE_BMP = "image/bmp";
    private final static String MIME_TYPE_XMS_BMP = "image/x-ms-bmp";
    private final static String MIME_TYPE_WAP_BMP = "image/vnd.wap.wbmp";
    private final static String MIME_TYPE_GIF = "image/gif";
    private final static String MIME_TYPE_WEBP = "image/webp";

    private final static String MIME_TYPE_3GP = "video/3gp";
    private final static String MIME_TYPE_MP4 = "video/mp4";
    private final static String MIME_TYPE_MPEG = "video/mpeg";
    private final static String MIME_TYPE_AVI = "video/avi";



    public final static String JPEG = ".jpeg";

    public final static String JPG = ".jpg";

    public final static String PNG = ".png";

    public final static String WEBP = ".webp";

    public final static String GIF = ".gif";

    public final static String BMP = ".bmp";

    public final static String AMR = ".amr";

    public final static String WAV = ".wav";

    public final static String MP3 = ".mp3";

    public final static String MP4 = ".mp4";

    public final static String AVI = ".avi";

    public final static String JPEG_Q = "image/jpeg";

    public final static String PNG_Q = "image/png";

    public final static String MP4_Q = "video/mp4";

    public final static String AVI_Q = "video/avi";

    public final static String AMR_Q = "audio/amr";

    public final static String WAV_Q = "audio/x-wav";

    public final static String MP3_Q = "audio/mpeg";

    public final static String DCIM = "DCIM/Camera";

    public final static String CAMERA = "Camera";

}