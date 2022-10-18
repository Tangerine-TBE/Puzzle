package com.weilai.jigsawpuzzle.fragment.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.photoview.PhotoView;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 ** DATE: 2022/9/20
 ** Author:tangerine
 ** Description:
 **/
public class SaveFragment extends BaseFragment {
    private SaveFragment() {

    }

    public static SaveFragment getInstance(String s) {
        SaveFragment saveFragment = new SaveFragment();
        Bundle bundle = new Bundle();
        bundle.putString("filePath", s);
        saveFragment.setArguments(bundle);
        return saveFragment;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (!TextUtils.isEmpty(path)) {
            Uri srcUri;
            if (PictureMimeType.isContent(path) || PictureMimeType.isHasHttp(path)) {
                srcUri = Uri.parse(path);
            } else {
                srcUri = Uri.fromFile(new File(path));
            }
            if (srcUri != null) {
                InputStream stream = null;
                try {
                    stream = _mActivity.getContentResolver().openInputStream(srcUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                if (bitmap != null) {
                    textView.setText(String.format("图片尺寸:%d*%d", bitmap.getWidth(), bitmap.getHeight()));
                    imageView.setImage(ImageSource.bitmap(bitmap));
                }
            }
        }


    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_template_save;
    }

    @Override
    protected void initView(View view) {
        AppCompatTextView appCompatTextView = view.findViewById(R.id.tv_title);
        appCompatTextView.setText(R.string.bitmap_info);
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        AppCompatTextView tvSave = view.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setText(R.string.share);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.visible);//是否显示详细信息
         path = getArguments().getString("filePath");
         imageView = view.findViewById(R.id.iv_img);
         textView = view.findViewById(R.id.tv_mapInfo);//本地路径

    }
    private String path;
    private SubsamplingScaleImageView imageView;
    private TextView textView;


    @Override
    protected void initListener(View view) {

    }
}
