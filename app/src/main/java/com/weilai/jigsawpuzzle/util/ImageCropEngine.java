package com.weilai.jigsawpuzzle.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.engine.CropFileEngine;
import com.weilai.jigsawpuzzle.Constant;
import com.weilai.jigsawpuzzle.configure.Config;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropImageEngine;

import java.io.File;
import java.util.ArrayList;

public class ImageCropEngine implements CropFileEngine {
    private String[] getNotSupportCrop() {
        return new String[]{PictureMimeType.ofGIF(), PictureMimeType.ofWEBP()};
    }
    private final float ratioX;
    private final float ratioY;
    public ImageCropEngine(float ratioX , float ratioY){
        this.ratioX = ratioX;
        this.ratioY = ratioY;
    }
    @Override
    public void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode) {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(false);//是否隐藏下面属性条
        options.setMaxScaleMultiplier(100);//裁剪质量
        options.setFreeStyleCropEnabled(false);//自由裁剪
        options.isCropDragSmoothToCenter(false);//裁剪和拖动自动居中
        options.setSkipCropMimeType(getNotSupportCrop());//跳过某些不想要的类型，比如gif web
        options.isForbidCropGifWebp(false);//是否需要支持剪切动态图形gif或webp
        options.isForbidSkipMultipleCrop(false);//切割多张图片是否禁止跳跃
        options.withAspectRatio(ratioX, ratioY);//设置为3:2
        UCrop uCrop = UCrop.of(srcUri, destinationUri, dataSource);
        uCrop.withOptions(options);
        uCrop.start(fragment.requireActivity(), fragment, requestCode);

    }
}
