package com.weilai.jigsawpuzzle.fragment.template;

import static com.luck.picture.lib.config.PictureSelectionConfig.cropFileEngine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.Crop;
import com.luck.picture.lib.config.CustomIntentKey;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.manager.SelectedManager;
import com.luck.picture.lib.utils.DateUtils;
import com.luck.picture.lib.utils.ToastUtils;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.dialog.template.TemplateConfirmDialog;
import com.weilai.jigsawpuzzle.dialog.template.TemplateEditDialog;
import com.weilai.jigsawpuzzle.util.GlideEngine;
import com.weilai.jigsawpuzzle.util.ImageCropEngine;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.weight.template.TemplateView;
import com.weilai.jigsawpuzzle.weight.template.TemplateViewInfo;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * * DATE: 2022/9/14
 * * Author:tangerine
 * * Description:1.After Show the template
 **/
public class TemplateEditFragment extends BaseFragment implements TemplateView.OutRectClickListener, TemplateEditDialog.TemplateDialogItemListener,TemplateConfirmDialog.OnConfirmClickedListener {
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
        templateEditView.setTemplateBitmap(bitMapInfo);
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.preview_template));
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
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
    private void checkNumOfPicSelected(){
        ArrayList<TemplateViewInfo> templateViewInfos = templateEditView.getTemplateViewInfo();
        int i = 0;
        for (TemplateViewInfo info : templateViewInfos){
           if (!info.hasPic()){
               i++;
           }
        }
        if (i >0){
            String str = String.format("你还有%d张贴图未选择,确认生成吗?",i);
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
                    new TemplateConfirmDialog(getContext(),TemplateEditFragment.this).show();

                }
            }).show();
        }
    }



    @Override
    protected void initListener(View view) {
        templateEditView.setOutRectClickListener(this);
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
        if (i >=0){
           BitMapInfo.SizeInfo sizeInfo =  bitMapInfo.getSizeInfos().get(0);
            mSelector.openGallery(SelectMimeType.ofImage())
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setCropEngine(new ImageCropEngine(sizeInfo.getAspectRatioWidth(),sizeInfo.getAspectRatioHeight()))
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
       if (i >= 0 ){
          BitMapInfo.SizeInfo sizeInfo =  bitMapInfo.getSizeInfos().get(0);
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
        cropFileEngine = new ImageCropEngine(sizeInfo.getAspectRatioWidth(),sizeInfo.getAspectRatioHeight());
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
                if (selectedResult.size() > 0){
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
    public void onConfirmClicked() {
        /*生成图片*/
        String tempInfo = JSONArray.toJSONString(templateEditView.getTemplateViewInfo());
        String bitmapInfo = JSONObject.toJSONString(bitMapInfo);
        start(TemplateSaveFragment.getInstance(tempInfo,bitmapInfo));


    }
}