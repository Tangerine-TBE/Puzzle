package com.weilai.jigsawpuzzle.fragment.template;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;
import com.weilai.jigsawpuzzle.util.FileUtil;
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
    private ScrollView scrollView;
    @Override
    protected void initView(View view) {
       scrollView =  view.findViewById(R.id.scrollView);
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
        templateView.setDrawFinish(new TemplateView.DrawFinish() {
            @Override
            public void drawFinish() {
                //线程操作
                Bitmap bitmap =   shotScrollView();
                try {
                    FileUtil.saveScreenShot(bitmap,System.currentTimeMillis()+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private Bitmap shotScrollView(){
        int h = 0 ;
        Bitmap bitmap;
        for (int i =0 ; i <scrollView.getChildCount();i ++ ){
            h+= scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        bitmap =Bitmap.createBitmap(scrollView.getWidth(),h,Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    @Override
    protected void initListener(View view) {

    }
}
