package com.weilai.jigsawpuzzle.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.weilai.jigsawpuzzle.activity.portrait.PortraitBaseActivity;
import com.weilai.jigsawpuzzle.fragment.special.MagicCameraActivity;
import com.weilai.jigsawpuzzle.fragment.special.OldActivity2;
import com.weilai.jigsawpuzzle.R;

import java.util.List;

/**
 ** DATE: 2022/9/13
 ** Author:tangerine
 ** Description:特效
 **/
public class EditImageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit_image,container,false);
        Intent intent = new Intent();
        intent.setClass(getContext(), OldActivity2.class);
        view.findViewById(R.id.aiv_old).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //变老
                XXPermissions.with(getContext()).permission(Permission.CAMERA).request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            intent.putExtra("typeKey","old");
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if(never){
                            XXPermissions.startPermissionActivity(getContext(),permissions);
                        }
                    }
                });


            }
        });
        view.findViewById(R.id.aiv_unbelived).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                XXPermissions.with(getContext()).permission(Permission.CAMERA).request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            intent.putExtra("typeKey","comic");
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if(never){
                            XXPermissions.startPermissionActivity(getContext(),permissions);
                        }
                    }
                });
                //漫画脸
            }
        });
        view.findViewById(R.id.aiv_child).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XXPermissions.with(getContext()).permission(Permission.CAMERA).request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            intent.putExtra("typeKey","young");
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if(never){
                            XXPermissions.startPermissionActivity(getContext(),permissions);
                        }
                    }
                });


                //童颜相机
            }
        });
        view.findViewById(R.id.aiv_csex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XXPermissions.with(getContext()).permission(Permission.CAMERA).request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            intent.putExtra("typeKey","sex");
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if(never){
                            XXPermissions.startPermissionActivity(getContext(),permissions);
                        }
                    }
                });


                //性别转换
            }
        });
        view.findViewById(R.id.aiv_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //魔法相机
                XXPermissions.with(getContext()).permission(Permission.CAMERA).request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all){
                            Intent intent = new Intent();
                            intent.setClass(getContext(), MagicCameraActivity.class);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if(never){
                            XXPermissions.startPermissionActivity(getContext(),permissions);
                        }
                    }
                });
            }
        });
        return view;
    }
}
