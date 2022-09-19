package com.weilai.jigsawpuzzle.fragment.template;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;
import com.weilai.jigsawpuzzle.util.ScreenShotUtil;
import com.weilai.jigsawpuzzle.weight.template.TemplateView;
import com.weilai.jigsawpuzzle.weight.template.TemplateViewInfo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @description:
 * @author: luo
 * @date: 2022/9/19
 */
public class TemplateSaveFragment extends BaseFragment {
    private TemplateSaveFragment() {

    }

    public static TemplateSaveFragment getInstance(String tempInfo,String bimapInfo) {
        TemplateSaveFragment templateSaveFragment = new TemplateSaveFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tempInfo", tempInfo);
        bundle.putString("mapInfo",bimapInfo);
        templateSaveFragment.setArguments(bundle);
        return templateSaveFragment;
    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_template_save;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.rv_show);//是否显示图片信息
        view.findViewById(R.id.tv_big);//尺寸
        view.findViewById(R.id.tv_create_date);//创建时间
        view.findViewById(R.id.tv_path);//本地路径
        view.findViewById(R.id.visible);//是否显示详细信息
        TemplateView templateView = view.findViewById(R.id.templateView);//templateView
        String tempInfo = getArguments().getString("tempInfo");
        String mapInfo = getArguments().getString("mapInfo");
        List<TemplateViewInfo> list = JSONArray.parseArray(tempInfo, TemplateViewInfo.class);
        BitMapInfo bitMapInfo = JSONObject.parseObject(mapInfo,BitMapInfo.class);
        templateView.createBitmap(list,bitMapInfo);


    }

    @Override
    protected void initListener(View view) {

    }
}
