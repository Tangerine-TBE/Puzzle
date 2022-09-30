package com.weilai.jigsawpuzzle.fragment.template;

import android.annotation.SuppressLint;
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

import com.alibaba.fastjson.JSONArray;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.template.TemplateAdapter;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.net.base.NetConfig;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.weight.template.SpacesItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * * DATE: 2022/9/14
 * * Author:tangerine
 * * Description: For Template
 **/
public class TemplateItemFragment extends BaseFragment implements TemplateAdapter.ItemClickListener, OnRefreshListener {
    /*获取到BitMapInfo*/
    private RecyclerView mRvTemplate;
    private List<BitMapInfo> list = new ArrayList<>();
    private TemplateAdapter templateAdapter;
    private GridLayoutManager gridLayoutManager;
    private SmartRefreshLayout mRefreshLayout;
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
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });
        mRefreshLayout = view.findViewById(R.id.smart_layout);
        mRvTemplate = view.findViewById(R.id.rv_template_data);
        templateAdapter = new TemplateAdapter(list, getContext(), this);
        gridLayoutManager = new GridLayoutManager(_mActivity, 2);
        mRvTemplate.setLayoutManager(gridLayoutManager);
        mRvTemplate.setAdapter(templateAdapter);
        arrayMap = new ArrayMap<>();
        arrayMap.put(SpacesItemDecoration.TOP_SPACE, 60);
        arrayMap.put(SpacesItemDecoration.BOTTOM_SPACE, 60);
        arrayMap.put(SpacesItemDecoration.LEFT_SPACE, 60);
        arrayMap.put(SpacesItemDecoration.RIGHT_SPACE, 20);
        mRvTemplate.addItemDecoration(new SpacesItemDecoration(2, arrayMap, true));
        mRefreshLayout.setPrimaryColors(new int[]{getResources().getColor(R.color.sel_text_main_color)});
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        /*這裏先這樣模擬網絡加載*/
        mRefreshLayout.autoRefresh();
        super.onLazyInitView(savedInstanceState);
    }


    @Override
    protected void initListener(View view) {
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void itemClick(String jsonValue) {
        TemplateShowFragment templateShowFragment = TemplateShowFragment.getInstance(jsonValue);
        start(templateShowFragment);
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        NetConfig.getInstance().getINetService().getPicTemplate().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (responseBody != null) {
                    String json = "";
                    try {
                        json = responseBody.string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (list != null){
                        if (!list.isEmpty()) {
                            list.clear();
                        }
                        try {
                            list.addAll(JSONArray.parseArray(json, BitMapInfo.class));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }


                }
            }

            @Override
            public void onError(Throwable e) {
                refreshLayout.finishRefresh();
            }

            @Override
            public void onComplete() {
                templateAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh();
            }
        });

    }




}
