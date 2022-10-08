package com.weilai.jigsawpuzzle.fragment.template;

import static com.luck.picture.lib.config.PictureSelectionConfig.cropFileEngine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSONObject;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.Crop;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.manager.SelectedManager;
import com.luck.picture.lib.utils.DateUtils;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.dialog.template.TemplateConfirmDialog;
import com.weilai.jigsawpuzzle.dialog.template.TemplateEditDialog;
import com.weilai.jigsawpuzzle.fragment.main.SaveFragment;
import com.weilai.jigsawpuzzle.net.base.NetConfig;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.GlideEngine;
import com.weilai.jigsawpuzzle.util.ImageCropEngine;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.weight.template.TemplateView;
import com.weilai.jigsawpuzzle.weight.template.TemplateViewInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * * DATE: 2022/9/14
 * * Author:tangerine
 * * Description:1.After Show the template
 **/
public class TemplateEditFragment extends BaseFragment implements TemplateView.OutRectClickListener, TemplateEditDialog.TemplateDialogItemListener, TemplateConfirmDialog.OnConfirmClickedListener, TemplateView.DrawFinish {
    private TemplateView templateEditView;
    private BitMapInfo bitMapInfo;
    private static final int FILTER_CODE = 1;
    private static final int CROPPER_CODE = 2;
    private PictureSelector mSelector;

    public static TemplateEditFragment newInstance(String bitmapInfo) {
        TemplateEditFragment templateEditFragment = new TemplateEditFragment();
        Bundle bundle = new Bundle();
        bundle.putString("bitmapInfo", bitmapInfo);
        templateEditFragment.setArguments(bundle);
        return templateEditFragment;
    }

    private TemplateEditFragment() {

    }

    @Override
    protected Object setLayout() {


        return R.layout.activity_template_edit;
    }

    @Override
    protected void initView(View view) {
        assert getArguments() != null;
        String json = getArguments().getString("bitmapInfo");
        if (!TextUtils.isEmpty(json)) {
            bitMapInfo = JSONObject.parseObject(json, BitMapInfo.class);
        }
        mSelector = PictureSelector
                .create(this);
        templateEditView = view.findViewById(R.id.icon);
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.preview_template));
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        AppCompatTextView tvSave = view.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumOfPicSelected();
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        NetConfig.getInstance().getINetService().getPhoto1(bitMapInfo.getTemplate()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (responseBody != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream()) ;
                    templateEditView.setTemplateBitmap(bitMapInfo,bitmap);
                }
            }

            @Override
            public void onError(Throwable e) {
                L.e(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
        super.onLazyInitView(savedInstanceState);
    }

    private void checkNumOfPicSelected() {
        ArrayList<TemplateViewInfo> templateViewInfos = templateEditView.getTemplateViewInfo();
        int i = 0;
        for (TemplateViewInfo info : templateViewInfos) {
            if (!info.hasPic()) {
                i++;
            }
        }
        if (i > 0) {
            String str = String.format("你还有%d张贴图未选择,确认生成吗?", i);
            new AlertDialog.Builder(getContext()).setMessage(str).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    dialog.cancel();
                    templateEditView.createBitmap();
                }
            }).show();
        } else {
            templateEditView.createBitmap();
        }
    }


    @Override
    protected void initListener(View view) {
        templateEditView.setOutRectClickListener(this);
        templateEditView.setDrawFinish(this);

    }

    @Override
    public void onRectClick(boolean hasPic) {
        if (hasPic) {
            //show dialog
            new TemplateEditDialog(getContext(), this).show();
        } else {
            //startToCameraActivity To select a pic
            startToSelectCropper();
        }
    }

    private void startToSelectCropper() {
        SelectedManager.clearSelectResult();

        int i = templateEditView.getSettingPosition();
        if (i >= 0) {
            BitMapInfo.Size sizeInfo = bitMapInfo.getSize().get(i);
            mSelector.openGallery(SelectMimeType.ofImage())
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setCropEngine(new ImageCropEngine(sizeInfo.getRatioWidth(), sizeInfo.getRatioHeight()))
                    .isPreviewImage(true)
                    .isDisplayCamera(false)
                    .setSelectionMode(SelectModeConfig.SINGLE)
                    .forResult(FILTER_CODE);
        }

    }

    private void startToCropper() {
        Uri srcUri;
        Uri destinationUri;
        LocalMedia localMedia = templateEditView.getInfoFromView();
        int i = templateEditView.getSettingPosition();
        if (i >= 0) {
            BitMapInfo.Size sizeInfo = bitMapInfo.getSize().get(0);
            if (localMedia == null) {
                return;
            }
            String path = localMedia.getAvailablePath();
            if (path == null || path.isEmpty()) {
                return;
            }
            ArrayList<String> dataCropSource = new ArrayList<>();
            dataCropSource.add(path);
            if (PictureMimeType.isContent(path) || PictureMimeType.isHasHttp(path)) {
                srcUri = Uri.parse(path);
            } else {
                srcUri = Uri.fromFile(new File(path));
            }
            String fileName = DateUtils.getCreateFileName("CROP_") + ".jpg";
            File externalFilesDir = getBaseActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File outputFile = new File(externalFilesDir.getAbsolutePath(), fileName);
            destinationUri = Uri.fromFile(outputFile);
            cropFileEngine = new ImageCropEngine(sizeInfo.getRatioWidth(), sizeInfo.getRatioHeight());
            SelectedManager.addSelectResult(localMedia);
            cropFileEngine.onStartCrop(this, srcUri, destinationUri, dataCropSource, CROPPER_CODE);
        }

    }

    @Override
    public void clickReplace() {
        startToSelectCropper();
    }

    @Override
    public void clickCrop() {
        startToCropper();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == FILTER_CODE) {
            if (data != null) {
                ArrayList<LocalMedia> list = data.getParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION);
                if (list != null) {
                    if (list.size() > 0) {
                        templateEditView.setBitMapFromClient(list.get(0));
                    }
                }
            }
        } else if (requestCode == CROPPER_CODE) {
            if (data != null) {
                List<LocalMedia> selectedResult = SelectedManager.getSelectedResult();
                if (selectedResult.size() > 0) {
                    LocalMedia media = selectedResult.get(0);
                    Uri output = Crop.getOutput(data);
                    media.setCutPath(output != null ? output.getPath() : "");
                    media.setCut(!TextUtils.isEmpty(media.getCutPath()));
                    media.setCropImageWidth(Crop.getOutputImageWidth(data));
                    media.setCropImageHeight(Crop.getOutputImageHeight(data));
                    media.setCropOffsetX(Crop.getOutputImageOffsetX(data));
                    media.setCropOffsetY(Crop.getOutputImageOffsetY(data));
                    media.setCropResultAspectRatio(Crop.getOutputCropAspectRatio(data));
                    media.setCustomData(Crop.getOutputCustomExtraData(data));
                    media.setSandboxPath(media.getCutPath());
                    templateEditView.setBitMapFromClient(media);
                    return;
                }
                L.e("未知错误，失败");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onConfirmClicked(String path) {
        /*生成图片*/
        SaveFragment saveFragment = SaveFragment.getInstance(path);
        start(saveFragment);

    }

    private void doOnBackGround(Bitmap bitmap) {
        showProcessDialog();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                String filePath = null;
                try {
                    filePath = FileUtil.saveScreenShot(bitmap, System.currentTimeMillis() + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                emitter.onNext(filePath);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(String s) {
                hideProcessDialog();
                new TemplateConfirmDialog(getContext(), TemplateEditFragment.this, s).show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private Bitmap shotScrollView(float x, float y, float width, float height) {
        Bitmap bitmap;
        templateEditView.setBackgroundColor(Color.parseColor("#ffffff"));
        bitmap = Bitmap.createBitmap(templateEditView.getWidth(), templateEditView.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        templateEditView.draw(canvas);
        bitmap = Bitmap.createBitmap(bitmap, (int)x, (int)y, (int)width, (int)height);
        return bitmap;
    }



    @Override
    public void drawFinish(float x, float y, float width, float height) {
        Bitmap bitmap = shotScrollView(x, y, width, height);
        templateEditView.resetState();
        doOnBackGround(bitmap);
    }
}
