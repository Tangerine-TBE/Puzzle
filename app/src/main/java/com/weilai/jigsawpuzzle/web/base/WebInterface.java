package com.weilai.jigsawpuzzle.web.base;

import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSONObject;

/**
 ** DATE: 2022/10/14
 ** Author:tangerine
 ** Description:
 **/
public class WebInterface {
    private final WebBaseFragment fragment;
    public WebInterface(WebBaseFragment fragment) {
        this.fragment = fragment;

    }
    static WebInterface create(WebBaseFragment fragment) {
        return new WebInterface(fragment);
    }
    @JavascriptInterface
    public String event(String params) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        String actionType = jsonObject.getString("action");

        return null;
    }
}
