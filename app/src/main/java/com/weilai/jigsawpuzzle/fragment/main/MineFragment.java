package com.weilai.jigsawpuzzle.fragment.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.umeng.commonsdk.debug.I;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.activity.main.ClientReportBaseActivity;
import com.weilai.jigsawpuzzle.activity.main.DataBaseActivity;
import com.weilai.jigsawpuzzle.activity.special.AgreementContentActivity;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.util.AppStoreUtil;
import com.weilai.jigsawpuzzle.util.UriUtil;
import com.weilai.jigsawpuzzle.util.maputils.DataCleanManager;

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
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"分享");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"这个拼图抠图应用非常好用!快来下载吧!");
                startActivity(Intent.createChooser(shareIntent, "分享到..."));
            }
        });
        view.findViewById(R.id.ll_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(_mActivity, DataBaseActivity.class));
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
               DataCleanManager.clearAllCache();
                Toast.makeText(_mActivity, "清空完毕~", Toast.LENGTH_SHORT).show();
            }
        });
//        view.findViewById(R.id.ll_great).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳去商店
//                startActivity( AppStoreUtil.getAppStoreIntent());
//
//            }
//        });
        view.findViewById(R.id.ll_advice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(_mActivity,ClientReportBaseActivity.class));
            }
        });
        view.findViewById(R.id.ll_secret).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("agreement_flag","yisi");
                intent.setClass(_mActivity,AgreementContentActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.ll_permissions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", _mActivity.getPackageName(), null));
                startActivity(localIntent);
            }
        });
        view.findViewById(R.id.ll_client).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("agreement_flag","fuwu");
                intent.setClass(_mActivity,AgreementContentActivity.class);
                startActivity(intent);

            }
        });
    }
}
