package com.weilai.jigsawpuzzle.util

import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


object DownloadUtils {
//    fun downloadPicture(url: String, filePath: String){
//        val client = OkHttpClient()
//        val request = Request.Builder().get()
//            .url(url)
//            .build()
//        val response = client.newCall(request).execute()
//        val inputStream = response.body()!!.byteStream()
//        var fos : FileOutputStream
//        val file = File(filePath)
//        try {
//            fos = FileOutputStream(file)
//            fos.write(inputStream.readBytes())          //这里写成read错了无数次
//            fos.flush()
//            fos.close()
//        }
//        catch (e: Exception) {e.printStackTrace()}
//    }

    /**
     * 下载文件
     */
    fun downloadFile(url: String, filePath: String, callback: RequestCallback) {
        val startTime = System.currentTimeMillis()
        val okHttpClient = OkHttpClient()
        val request = Request.Builder().url(url).build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 下载失败
                e.printStackTrace()
                callback.onFailure("download failed", e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                var `is`: InputStream? = null
                val buf = ByteArray(2048)
                var len = 0
                var fos: FileOutputStream? = null
                // 储存下载文件的目录
//                String savePath = VoiceConstants.SAVE_PATH+VoiceConstants.SAVE_CHILD_PATH;
                try {
                    `is` = response.body!!.byteStream()
                    val total = response.body!!.contentLength()
                    val file = File(filePath)
                    fos = FileOutputStream(file)
                    var sum: Long = 0
                    while (`is`.read(buf).also { len = it } != -1) {
                        fos.write(buf, 0, len)
                        sum += len.toLong()
                        val progress = (sum * 1.0f / total * 100).toInt()
                        // 下载中
//                        listener.onDownloading(progress);
                    }
                    fos.flush()
                    // 下载完成
//                    listener.onDownloadSuccess();
                    callback.onSuccess("download success")
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    //                    listener.onDownloadFailed();
                    callback.onFailure("download failed", e)
                } finally {
                    try {
                        `is`?.close()
                    } catch (e: IOException) {
                    }
                    try {
                        fos?.close()
                    } catch (e: IOException) {
                    }
                }
            }
        })
    }

    interface RequestCallback {
        fun onSuccess(response: String?)
        fun onFailure(msg: String?, e: java.lang.Exception?)
    }
}