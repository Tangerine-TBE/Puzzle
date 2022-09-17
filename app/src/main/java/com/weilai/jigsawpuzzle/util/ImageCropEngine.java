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
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropImageEngine;

import java.util.ArrayList;

public class ImageCropEngine implements CropFileEngine {
    /*输出路径*/
    private String getSandboxPath(){
        return null;
    }
    private String[] getNotSupportCrop() {
            return new String[]{PictureMimeType.ofGIF(), PictureMimeType.ofWEBP()};
    }
    @Override
    public void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode) {
        UCrop.Options options =new UCrop.Options();
        options.setHideBottomControls(false);//是否隐藏下面属性条
        options.setMaxScaleMultiplier(100);//裁剪质量
        options.setFreeStyleCropEnabled(false);//自由裁剪
        options.isCropDragSmoothToCenter(false);//裁剪和拖动自动居中
        options.setSkipCropMimeType(getNotSupportCrop());//跳过某些不想要的类型，比如gif web
        options.isForbidCropGifWebp(false);//是否需要支持剪切动态图形gif或webp
        options.setCropOutputPathDir(getSandboxPath());//输出路径
        options.isForbidSkipMultipleCrop(false);//切割多张图片是否禁止跳跃

        UCrop uCrop = UCrop.of(srcUri, destinationUri, dataSource);
        uCrop.withOptions(options);
        uCrop.setImageEngine(new UCropImageEngine() {
            @Override
            public void loadImage(Context context, String url, ImageView imageView) {
                Glide.with(context).load(url).override(180, 180).into(imageView);
            }
            @Override
            public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
                Glide.with(context).asBitmap().load(url).override(maxWidth, maxHeight).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (call != null) {
                            call.onCall(resource);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        if (call != null) {
                            call.onCall(null);
                        }
                    }
                });

            }
        });
        uCrop.start(fragment.requireActivity(), fragment, requestCode);

    }
}
