package com.weilai.jigsawpuzzle.fragment.template;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSONArray;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.template.TemplateAdapter;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.net.base.NetConfig;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.weight.template.SpacesItemDecoration;
import com.weilai.jigsawpuzzle.weight.template.SplitItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

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
    private StaggeredGridLayoutManager gridLayoutManager;
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
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRvTemplate.setLayoutManager(gridLayoutManager);
        mRvTemplate.setAdapter(templateAdapter);
        mRvTemplate.addItemDecoration(new SplitItemDecoration(20));
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
        NetConfig.getInstance().getINetService().getPicTemplate().flatMap(new Function<ResponseBody, ObservableSource<List<BitMapInfo>>>() {
            @Override
            public ObservableSource<List<BitMapInfo>> apply(ResponseBody responseBody) throws Exception {
                ArrayList<BitMapInfo> finalBitMapInfos = null;
                try {
                    ArrayList<BitMapInfo> bitMapInfos = null;
                    if (responseBody != null) {
                        String json = "";
                        json = responseBody.string();
                        bitMapInfos = new ArrayList<>(JSONArray.parseArray(json, BitMapInfo.class));
                        for (BitMapInfo bitMapInfo : bitMapInfos) {
                            Response<ResponseBody> responseBitmap = NetConfig.getInstance().getINetService().getPhoto(bitMapInfo.getBitmap()).execute();
                            assert responseBitmap.body() != null;
                            Bitmap bitmapBitmap = BitmapFactory.decodeStream(responseBitmap.body().byteStream());
                            String bitmapPath = FileUtil.saveBitmapToCache(bitMapInfo.getName() + "bitmap", bitmapBitmap);
                            bitMapInfo.setBitmap(bitmapPath);
                        }
                    }
                    finalBitMapInfos = bitMapInfos;
                } catch (Exception e) {
                e.printStackTrace();
                }

                ArrayList<BitMapInfo> finalBitMapInfos1 = finalBitMapInfos;
                return Observable.create(emitter -> emitter.onNext(finalBitMapInfos1));

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<BitMapInfo>>() {
            @Override
            public void onNext(List<BitMapInfo> bitMapInfos) {
                if (bitMapInfos != null) {
                    if (list != null){
                        if (!list.isEmpty()){
                            list.clear();
                        }
                        list.addAll(bitMapInfos);
                    }
                    templateAdapter.notifyDataSetChanged();
                    refreshLayout.finishRefresh(true);
                }
            }

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onError(Throwable e) {
                refreshLayout.finishRefresh(2000,false,false);
            }

            @Override
            public void onComplete() {

            }
        });

    }


}
