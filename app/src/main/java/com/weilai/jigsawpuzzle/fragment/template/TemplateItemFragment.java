package com.weilai.jigsawpuzzle.fragment.template;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.template.TemplateAdapter;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseSimpleActivity;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;
import com.weilai.jigsawpuzzle.weight.template.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * * DATE: 2022/9/14
 * * Author:tangerine
 * * Description: For Template
 **/
public class TemplateItemFragment extends BaseFragment implements TemplateAdapter.ItemClickListener {
    /*获取到BitMapInfo*/
    private RecyclerView mRvTemplate;
    private List<BitMapInfo> list;
    private TemplateAdapter templateAdapter;
    private GridLayoutManager gridLayoutManager;
    ArrayMap<String, Integer> arrayMap;

    @Override
    protected Object setLayout() {
        return R.layout.activity_template;
    }

    public static TemplateItemFragment getInstance() {
        return new TemplateItemFragment();
    }

    private TemplateItemFragment() {

    }
    @Override
    protected void initView(View view) {
        list = new ArrayList<>();
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });
        mRvTemplate = view.findViewById(R.id.rv_template_data);
        templateAdapter = new TemplateAdapter(list, getContext(), this);
        gridLayoutManager = new GridLayoutManager(_mActivity, 2);
        mRvTemplate.setLayoutManager(gridLayoutManager);
        mRvTemplate.setAdapter(templateAdapter);


    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        showProcessDialog();
        /*這裏先這樣模擬網絡加載*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                arrayMap = new ArrayMap<>();
                arrayMap.put(SpacesItemDecoration.TOP_SPACE, 60);
                arrayMap.put(SpacesItemDecoration.BOTTOM_SPACE, 60);
                arrayMap.put(SpacesItemDecoration.LEFT_SPACE, 60);
                arrayMap.put(SpacesItemDecoration.RIGHT_SPACE, 20);
                initData1();
                initData2();
                initData3();
                initData4();
                _mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProcessDialog();
                        mRvTemplate.addItemDecoration(new SpacesItemDecoration(2, arrayMap, true));
                        templateAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();


        super.onLazyInitView(savedInstanceState);
    }

    /*for test*/
    private void initData1() {
        BitMapInfo bitMapInfo = new BitMapInfo();
        bitMapInfo.setName("模板1:1");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.template_template11);
        FileUtil.saveBitmapToCache("模板1:1", bitmap);
        bitMapInfo.setBitmap("模板1:1");
        List<BitMapInfo.SizeInfo> sizeInfos = new ArrayList<>();
        BitMapInfo.SizeInfo sizeInfo1 = new BitMapInfo.SizeInfo(0.08f, 0.33f, 0, 0.44f, 0.27f, 1, 1);
        BitMapInfo.SizeInfo sizeInfo2 = new BitMapInfo.SizeInfo(0.55f, 0.33f, 0, 0.44f, 0.74f, 1, 1);
        sizeInfos.add(sizeInfo1);
        sizeInfos.add(sizeInfo2);
        bitMapInfo.setSizeInfos(sizeInfos);
        list.add(bitMapInfo);
    }

    private void initData2() {
        BitMapInfo bitMapInfo = new BitMapInfo();
        bitMapInfo.setName("角度模板1:1");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.template_template_rorate11);
        FileUtil.saveBitmapToCache("角度模板1:1", bitmap);
        bitMapInfo.setBitmap("角度模板1:1");
        List<BitMapInfo.SizeInfo> sizeInfos = new ArrayList<>();
        BitMapInfo.SizeInfo sizeInfo1 = new BitMapInfo.SizeInfo(0.195f, 0.29f, 30, 0.44f, 0.27f, 1, 1);
        BitMapInfo.SizeInfo sizeInfo2 = new BitMapInfo.SizeInfo(0.55f, 0.33f, 0, 0.44f, 0.74f, 1, 1);
        sizeInfos.add(sizeInfo1);
        sizeInfos.add(sizeInfo2);
        bitMapInfo.setSizeInfos(sizeInfos);
        list.add(bitMapInfo);
    }

    private void initData3() {
        BitMapInfo bitMapInfo = new BitMapInfo();
        bitMapInfo.setName("模板3:2");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.template_template32);
        FileUtil.saveBitmapToCache("模板3:2", bitmap);
        bitMapInfo.setBitmap("模板3:2");
        List<BitMapInfo.SizeInfo> sizeInfos = new ArrayList<>();
        BitMapInfo.SizeInfo sizeInfo1 = new BitMapInfo.SizeInfo(0.0773f, 0.3335f, 0, 0.4047f, 0.264f, 2, 3);
        BitMapInfo.SizeInfo sizeInfo2 = new BitMapInfo.SizeInfo(0.553f, 0.3335f, 0, 0.4047f, 0.74f, 2, 3);
        sizeInfos.add(sizeInfo1);
        sizeInfos.add(sizeInfo2);
        bitMapInfo.setSizeInfos(sizeInfos);
        list.add(bitMapInfo);
    }

    private void initData4() {
        BitMapInfo bitMapInfo = new BitMapInfo();
        bitMapInfo.setName("角度模板3:2");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.template_template_rorate32);
        FileUtil.saveBitmapToCache("角度模板3:2", bitmap);
        bitMapInfo.setBitmap("角度模板3:2");
        List<BitMapInfo.SizeInfo> sizeInfos = new ArrayList<>();
        BitMapInfo.SizeInfo sizeInfo1 = new BitMapInfo.SizeInfo(0.165f, 0.29f, 30, 0.4047f, 0.264f, 2, 3);
        BitMapInfo.SizeInfo sizeInfo2 = new BitMapInfo.SizeInfo(0.553f, 0.33f, 0, 0.4047f, 0.74f, 2, 3);
        sizeInfos.add(sizeInfo1);
        sizeInfos.add(sizeInfo2);
        bitMapInfo.setSizeInfos(sizeInfos);
        list.add(bitMapInfo);


    }

    @Override
    protected void initListener(View view) {
    }

    @Override
    public void itemClick(String jsonValue) {
        TemplateShowFragment templateShowFragment = TemplateShowFragment.getInstance(jsonValue);
        start(templateShowFragment);
    }


}
