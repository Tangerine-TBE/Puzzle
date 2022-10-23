package com.weilai.jigsawpuzzle.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.util.UriUtil;

/**
 ** DATE: 2022/9/26
 ** Author:tangerine
 ** Description:
 **/
public class MineFragment  extends BaseFragment {
    @Override
    protected Object setLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.ll_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "分享到..."));
            }
        });
        view.findViewById(R.id.ll_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据库连接
            }
        });
        view.findViewById(R.id.ll_clean_template).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(_mActivity, "清空完毕~", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.ll_clean_cache).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除缓存

            }
        });
        view.findViewById(R.id.ll_great).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳去商店
            }
        });
        view.findViewById(R.id.ll_advice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.ll_secret).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.ll_permissions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
