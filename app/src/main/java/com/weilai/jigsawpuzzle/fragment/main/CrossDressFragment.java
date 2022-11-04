package com.weilai.jigsawpuzzle.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.feisukj.ad.manager.AdController;
import com.feisukj.base.bean.ad.ADConstants;
import com.weilai.jigsawpuzzle.Constants;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.bean.CrossBannerEntity;
import com.weilai.jigsawpuzzle.fragment.special.FilterActivity;
import com.weilai.jigsawpuzzle.fragment.special.MagicCameraActivity;
import com.weilai.jigsawpuzzle.fragment.special.OldActivity2;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.activity.portrait.PortraitBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzle.PuzzleBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzle9P.Puzzle9PBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzleHLp.PuzzleHLPBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzleLP.PuzzleLPBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzleLine.PuzzleLineBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzleQr.PuzzleQrBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzleSS.PuzzleSShotBaseActivity;
import com.weilai.jigsawpuzzle.activity.template.TemplateBaseActivity;
import com.weilai.jigsawpuzzle.adapter.main.ImageBannerAdapter;
import com.weilai.jigsawpuzzle.net.base.NetConfig;
import com.weilai.jigsawpuzzle.util.GlideEngine;
import com.weilai.jigsawpuzzle.util.ImageCropEngine;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.util.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.RectangleIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * * DATE: 2022/9/13
 * * Author:tangerine
 * * Description:首页
 **/
public class CrossDressFragment extends BaseFragment implements View.OnClickListener {
    private static final int FILTER_PUZZLE_QR_CODE = 1;
    private static final int FILTER_PUZZLE_LP_CODE = 2;
    private static final int FILTER_PUZZLE_9P_CODE = 3;
    private static final int FILTER_PUZZLE_HLP_CODE = 4;
    private static final int FILTER_PUZZLE_LINE_CODE = 5;
    private static final int FILTER_PUZZLE_FITTER_CODE = 6;
    private FrameLayout frameLayout;
    @Override
    protected Object setLayout() {
        return R.layout.fragment_cross_dress;
    }

    @Override
    protected void initView(View view) {
        initUi(view);
    }

    @Override
    protected void initListener(View view) {
        frameLayout = view.findViewById(R.id.ad_container);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        new AdController.Builder(_mActivity
                , ADConstants.HOME_PAGE).setContainer(frameLayout).create().show();
    }

    private void initUi(View view) {
        Banner<CrossBannerEntity, ImageBannerAdapter> banner = view.findViewById(R.id.banner);

        view.findViewById(R.id.tv_template).setOnClickListener(this);
        view.findViewById(R.id.tv_pic).setOnClickListener(this);
        view.findViewById(R.id.tv_splic_health).setOnClickListener(this);
        view.findViewById(R.id.tv_splic_hori).setOnClickListener(this);
        view.findViewById(R.id.tv_l_pic).setOnClickListener(this);
        view.findViewById(R.id.tv_lines).setOnClickListener(this);
        view.findViewById(R.id.tv_lattice).setOnClickListener(this);
        view.findViewById(R.id.tv_screen_shot).setOnClickListener(this);
        view.findViewById(R.id.iv_ai).setOnClickListener(this);
        view.findViewById(R.id.iv_comis).setOnClickListener(this);
        view.findViewById(R.id.iv_toning).setOnClickListener(this);
        NetConfig.getInstance().getINetService().getBanner().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<List<CrossBannerEntity> >() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(List<CrossBannerEntity> responseBody) {
                L.e(responseBody.toString());
                banner.setAdapter(new ImageBannerAdapter(responseBody,getContext()))
                        .setIndicator(new RectangleIndicator(getContext()))
                        .setIndicatorNormalWidth(15)
                        .setIndicatorRadius(100)
                        .setIndicatorHeight(15)
                        .setIndicatorNormalColor(getResources().getColor(R.color.bg_cross_gray))
                        .setIndicatorSelectedWidth(50)
                        .setIndicatorMargins(new IndicatorConfig.Margins(0, 0, 0, 150));
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                ToastUtil.showToast(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_template) {
            XXPermissions.with(this).permission(new String[]{Permission.MANAGE_EXTERNAL_STORAGE,Permission.CAMERA}).request(new OnPermissionCallback() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    if (all){
                        startActivity(new Intent(getContext(), TemplateBaseActivity.class));
                    }
                }

                @Override
                public void onDenied(List<String> permissions, boolean never) {
                    XXPermissions.startPermissionActivity(getContext(),permissions);
                }
            });
        } else if (view.getId() == R.id.tv_pic) {
            XXPermissions.with(this).permission(new String[]{Permission.MANAGE_EXTERNAL_STORAGE,Permission.CAMERA}).request(new OnPermissionCallback() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    if (all){
                        startActivity(new Intent(getContext(), PuzzleBaseActivity.class));
                    }
                }

                @Override
                public void onDenied(List<String> permissions, boolean never) {
                    XXPermissions.startPermissionActivity(getContext(),permissions);
                }
            });
        } else if (view.getId() == R.id.tv_splic_health) {
            XXPermissions.with(this).permission(new String[]{Permission.MANAGE_EXTERNAL_STORAGE,Permission.CAMERA}).request(new OnPermissionCallback() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    if (all){
                        PictureSelector.create(CrossDressFragment.this)
                                .openGallery(SelectMimeType.ofImage())
                                .isDisplayCamera(true)
                                .setMinSelectNum(4)
                                .setMaxSelectNum(4)
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .isPreviewImage(true)
                                .setSelectionMode(SelectModeConfig.MULTIPLE)
                                .forResult(FILTER_PUZZLE_QR_CODE);
                    }
                }

                @Override
                public void onDenied(List<String> permissions, boolean never) {
                    XXPermissions.startPermissionActivity(getContext(),permissions);
                }
            });

        } else if (view.getId() == R.id.tv_splic_hori) {
            XXPermissions.with(this).permission(new String[]{Permission.MANAGE_EXTERNAL_STORAGE,Permission.CAMERA}).request(new OnPermissionCallback() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    if (all){
                        PictureSelector.create(CrossDressFragment.this)
                                .openGallery(SelectMimeType.ofImage())
                                .isDisplayCamera(true)
                                .setMinSelectNum(1)
                                .setMaxSelectNum(10)
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .isPreviewImage(true)
                                .setSelectionMode(SelectModeConfig.MULTIPLE)
                                .forResult(FILTER_PUZZLE_HLP_CODE);
                    }
                }

                @Override
                public void onDenied(List<String> permissions, boolean never) {
                    XXPermissions.startPermissionActivity(getContext(),permissions);
                }
            });

        } else if (view.getId() == R.id.tv_l_pic) {
            XXPermissions.with(this).permission(new String[]{Permission.MANAGE_EXTERNAL_STORAGE,Permission.CAMERA}).request(new OnPermissionCallback() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    if (all){
                        PictureSelector.create(CrossDressFragment.this)
                                .openGallery(SelectMimeType.ofImage())
                                .isDisplayCamera(true)
                                .setMinSelectNum(1)
                                .setMaxSelectNum(10)
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .isPreviewImage(true)
                                .setSelectionMode(SelectModeConfig.MULTIPLE)
                                .forResult(FILTER_PUZZLE_LP_CODE);
                    }
                }
                @Override
                public void onDenied(List<String> permissions, boolean never) {
                    XXPermissions.startPermissionActivity(getContext(),permissions);
                }
            });


        } else if (view.getId() == R.id.tv_lines) {
            XXPermissions.with(this).permission(new String[]{Permission.MANAGE_EXTERNAL_STORAGE,Permission.CAMERA}).request(new OnPermissionCallback() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    if (all){
                        PictureSelector.create(CrossDressFragment.this)
                                .openGallery(SelectMimeType.ofImage())
                                .isDisplayCamera(true)
                                .setMinSelectNum(1)
                                .setMaxSelectNum(10)
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .isPreviewImage(true)
                                .setSelectionMode(SelectModeConfig.MULTIPLE)
                                .forResult(FILTER_PUZZLE_LP_CODE);
                    }
                }

                @Override
                public void onDenied(List<String> permissions, boolean never) {
                    XXPermissions.startPermissionActivity(getContext(),permissions);
                }
            });


        } else if (view.getId() == R.id.tv_lattice) {
            XXPermissions.with(this).permission(new String[]{Permission.MANAGE_EXTERNAL_STORAGE,Permission.CAMERA}).request(new OnPermissionCallback() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    if (all){

                        PictureSelector.create(CrossDressFragment.this)
                                .openGallery(SelectMimeType.ofImage())
                                .isDisplayCamera(true)
                                .setSelectionMode(SelectModeConfig.SINGLE)
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .setCropEngine(new ImageCropEngine(1,1))
                                .forResult(FILTER_PUZZLE_9P_CODE);
                    }
                }

                @Override
                public void onDenied(List<String> permissions, boolean never) {
                    XXPermissions.startPermissionActivity(getContext(),permissions);
                }
            });

        } else if (view.getId() == R.id.tv_screen_shot) {
            XXPermissions.with(this).permission(new String[]{Permission.MANAGE_EXTERNAL_STORAGE,Permission.CAMERA}).request(new OnPermissionCallback() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    if (all){
                        startActivity(new Intent(getActivity(), PuzzleSShotBaseActivity.class));
                    }
                }

                @Override
                public void onDenied(List<String> permissions, boolean never) {
                    XXPermissions.startPermissionActivity(getContext(),permissions);
                }
            });

        }else if (view .getId() == R.id.iv_ai){
            XXPermissions.with(this).permission(Permission.MANAGE_EXTERNAL_STORAGE,Permission.CAMERA).request(new OnPermissionCallback() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    if (all){
                        Intent intent = new Intent();
                        intent.putExtra("type","intelligent");
                        intent.setClass(getContext(),PortraitBaseActivity.class);
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
        }else if (view.getId() == R.id.iv_comis){

            XXPermissions.with(this).permission(Permission.CAMERA).request(new OnPermissionCallback() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    if (all){
                        Intent intent = new Intent();
                        intent.putExtra("typeKey","comic");
                        intent.setClass(getContext(), OldActivity2.class);
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

        }else if (view.getId() == R.id.iv_toning){
            XXPermissions.with(this).permission(Permission.CAMERA).request(new OnPermissionCallback() {
                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    if (all){
                        PictureSelector.create(CrossDressFragment.this)
                                .openGallery(SelectMimeType.ofImage())
                                .isDisplayCamera(true)
                                .setSelectionMode(SelectModeConfig.SINGLE)
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .forResult(FILTER_PUZZLE_FITTER_CODE);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ArrayList<LocalMedia> list = data.getParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION);
            if (list != null) {
                int size = list.size();
                if (size > 0) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (LocalMedia localMedia : list) {
                        arrayList.add(localMedia.getAvailablePath());
                    }
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("data", arrayList);
                    if (requestCode == FILTER_PUZZLE_QR_CODE) {
                        intent.setClass(getContext(),PuzzleQrBaseActivity.class);
                    }else if (requestCode == FILTER_PUZZLE_LP_CODE){
                        intent.setClass(getContext(), PuzzleLPBaseActivity.class);
                    }else if (requestCode == FILTER_PUZZLE_9P_CODE){
                        intent.setClass(getContext(), Puzzle9PBaseActivity.class);
                    }else if (requestCode == FILTER_PUZZLE_HLP_CODE){
                        intent.setClass(getContext(), PuzzleHLPBaseActivity.class);
                    }else if (requestCode == FILTER_PUZZLE_LINE_CODE){
                        intent.setClass(getContext(), PuzzleLineBaseActivity.class);
                    }else if (requestCode == FILTER_PUZZLE_FITTER_CODE){
                        intent.putExtra("img_page",arrayList.get(0));
                        intent.setClass(getContext(),FilterActivity.class);
                    }
                    startActivity(intent);
                }

            }
        }

    }
}
