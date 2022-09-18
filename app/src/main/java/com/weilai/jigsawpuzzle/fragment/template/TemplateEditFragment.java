package com.weilai.jigsawpuzzle.fragment.template;

import static com.luck.picture.lib.config.PictureSelectionConfig.cropFileEngine;

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
import com.weilai.jigsawpuzzle.dialog.template.TemplateEditDialog;
import com.weilai.jigsawpuzzle.util.GlideEngine;
import com.weilai.jigsawpuzzle.util.ImageCropEngine;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.weight.template.TemplateView;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * * DATE: 2022/9/14
 * * Author:tangerine
 * * Description:1.After Show the template
 **/
public class TemplateEditFragment extends BaseFragment implements TemplateView.OutRectClickListener, TemplateEditDialog.TemplateDialogItemListener {
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
    }

    @Override
    protected void initListener(View view) {
        templateEditView.setOutRectClickListener(this);
    }

    @Override
    public boolean onRectClick(boolean hasPic) {
        if (hasPic) {
            //show dialog
            new TemplateEditDialog(getContext(), this).show();
        } else {
            //startToCameraActivity To select a pic
            startToSelectCropper();
        }
        return true;
    }

    private void startToSelectCropper() {
        mSelector.openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .setCropEngine(new ImageCropEngine())
                .isPreviewImage(true)
                .isDisplayCamera(false)
                .setSelectionMode(SelectModeConfig.SINGLE)
                .forResult(FILTER_CODE);
    }

    private void startToCropper() {
        Uri srcUri;
        Uri destinationUri;
        LocalMedia localMedia = templateEditView.getInfoFromView();
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
        File externalFilesDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File outputFile = new File(externalFilesDir.getAbsolutePath(), fileName);
        destinationUri = Uri.fromFile(outputFile);
        cropFileEngine = new ImageCropEngine();
        SelectedManager.addSelectResult(localMedia);
        cropFileEngine.onStartCrop(this, srcUri, destinationUri, dataCropSource, CROPPER_CODE);

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

}
