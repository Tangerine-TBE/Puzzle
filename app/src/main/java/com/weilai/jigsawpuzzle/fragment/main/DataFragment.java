package com.weilai.jigsawpuzzle.fragment.main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;

public class DataFragment extends BaseFragment {
    private DataFragment(){

    }
    public static DataFragment getInstance(){
        return new DataFragment();
    }
    @Override
    protected Object setLayout() {
        return R.layout.fragment_data;
    }
    private RecyclerView recyclerView ;
    @Override
    protected void initView(View view) {
        AppCompatTextView title = view.findViewById(R.id.tv_title);
        title.setText("制作记录");
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initListener(View view) {

    }
}
