package com.weilai.jigsawpuzzle.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.umeng.commonsdk.debug.I;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.activity.main.SaveBaseActivity;
import com.weilai.jigsawpuzzle.adapter.data.RecordAdapter;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.db.RecordInfo;
import com.weilai.jigsawpuzzle.util.dao.DaoTool;
import com.weilai.jigsawpuzzle.weight.MyLinearLayoutManager;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DataFragment extends BaseFragment implements RecordAdapter.OnAllCleanListener ,RecordAdapter.OnItemClickedListener {
    private RecordAdapter mRecordAdapter;
    private boolean isEditing;
    private CheckBox checkbox;
    private AppCompatTextView tvDelete;
    private LinearLayoutCompat linearLayoutCompat;

    private DataFragment() {

    }

    public static DataFragment getInstance() {
        return new DataFragment();
    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_data;
    }

    private RecyclerView recyclerView;

    @Override
    protected void initView(View view) {
        AppCompatTextView title = view.findViewById(R.id.tv_title);
        title.setText("制作记录");
        recyclerView = view.findViewById(R.id.recyclerView);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.getItemAnimator().setChangeDuration(0);
        checkbox = view.findViewById(R.id.checkbox);
        tvDelete = view.findViewById(R.id.tv_delete);
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(_mActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        AppCompatTextView appCompatTextView = view.findViewById(R.id.tv_save);
        appCompatTextView.setVisibility(View.VISIBLE);
        linearLayoutCompat = view.findViewById(R.id.layout_edit);
        appCompatTextView.setText("编辑");
        appCompatTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    isEditing = false;
                    mRecordAdapter.setMode(false);
                    linearLayoutCompat.setVisibility(View.GONE);
                } else {
                    isEditing = true;
                    mRecordAdapter.setMode(true);
                    linearLayoutCompat.setVisibility(View.VISIBLE);

                }
            }
        });
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        DaoTool.obtainAllRecordInfo().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<RecordInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(List<RecordInfo> recordInfos) {
                mRecordAdapter = new RecordAdapter(recordInfos, _mActivity, DataFragment.this);
                mRecordAdapter.setOnItemClickedListener(DataFragment.this);
                recyclerView.setAdapter(mRecordAdapter);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void initListener(View view) {
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setText("取消全选");
                    mRecordAdapter.setSelectAll();
                } else {
                    buttonView.setText("全选");
                    mRecordAdapter.setUnSelectAll();
                }
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecordAdapter.deleteSelected();
            }
        });
    }

    @Override
    public void allClean() {
        isEditing = false;
        mRecordAdapter.setMode(false);
        linearLayoutCompat.setVisibility(View.GONE);
    }

    @Override
    public void onItemCLicked(String path) {
        Intent intent = new Intent();
        intent.putExtra("data",path);
        intent.putExtra("type","图片信息");
        intent.setClass(_mActivity, SaveBaseActivity.class);
        startActivity(intent);
    }
}
