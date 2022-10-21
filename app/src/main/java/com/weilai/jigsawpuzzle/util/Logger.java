package com.weilai.jigsawpuzzle.util;



/**
 * Author : Gupingping
 * Date : 2018/9/28
 * QQ : 464955343
 */
public class Logger implements LoggingInterceptor.Logger {

    @Override
    public void log(String message) {
        L.e("http----: " +message);
    }
}
