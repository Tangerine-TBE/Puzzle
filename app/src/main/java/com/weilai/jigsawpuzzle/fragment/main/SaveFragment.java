package com.weilai.jigsawpuzzle.fragment.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.luck.picture.lib.config.PictureMimeType;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.activity.main.ClientReportBaseActivity;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.db.RecordInfo;
import com.weilai.jigsawpuzzle.util.AppStoreUtil;
import com.weilai.jigsawpuzzle.util.DateUtil;
import com.weilai.jigsawpuzzle.util.ToastUtil;
import com.weilai.jigsawpuzzle.util.UriUtil;
import com.weilai.jigsawpuzzle.util.dao.DaoTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * * DATE: 2022/9/20
 * * Author:tangerine
 * * Description:
 **/
public class SaveFragment extends BaseFragment {
    private TextView tvSize;
    private TextView tvDate;
    private TextView tvPath;
    private boolean isOpen;

    private SaveFragment() {

    }

    public static SaveFragment getInstance(String s, String type) {
        SaveFragment saveFragment = new SaveFragment();
        Bundle bundle = new Bundle();
        bundle.putString("filePath", s);
        bundle.putString("type", type);
        saveFragment.setArguments(bundle);
        return saveFragment;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        Observable.create(new ObservableOnSubscribe<Object[]>() {
            @Override
            public void subscribe(ObservableEmitter<Object[]> emitter) throws Exception {
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
                        if (bitmap == null){
                            throw new RuntimeException("图片不存在或已损坏");
                        }
                        Object[] objects = new Object[2];
                        objects[0] = bitmap;
                        objects[1] = path;
                        emitter.onNext(objects);
                    }
                }

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<Object[]>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(Object[] objects) {
                Bitmap bitmap = (Bitmap) objects[0];
                String path = (String) objects[1];
                if (bitmap != null) {
                    tvSize.setText(String.format("%d*%d", bitmap.getWidth(), bitmap.getHeight()));
                    tvPath.setText(path.trim());
                    imageView.setImage(ImageSource.bitmap(bitmap));
                    String type = getArguments().getString("type");
                    if ("图片信息".equals(type)) {
                        tvDate.setText(DaoTool.getDateWithPath(path));
                        return;
                    }
                    tvDate.setText(DateUtil.unixTimeToDateTimeString(System.currentTimeMillis() / 1000));
                    RecordInfo recordInfo = new RecordInfo();
                    recordInfo.setTime(System.currentTimeMillis());
                    recordInfo.setFilePath(path.trim());
                    recordInfo.setFileName(type);
                    DaoTool.insertRecord(recordInfo);
                    Toast.makeText(_mActivity, "图片已经保存到相册", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast(e.getMessage());
                _mActivity.finish();
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_template_save;
    }

    @Override
    protected void initView(View view) {
        tvSize = view.findViewById(R.id.tv_size);
        tvDate = view.findViewById(R.id.tv_date);
        tvPath = view.findViewById(R.id.tv_path);
        AppCompatTextView appCompatTextView = view.findViewById(R.id.tv_title);
        appCompatTextView.setText(R.string.bitmap_info);
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });
        AppCompatTextView tvSave = view.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setText(R.string.share);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.putExtra(Intent.EXTRA_STREAM, UriUtil.path2Uri(path));
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, "分享到..."));
            }
        });
        view.findViewById(R.id.visible);//是否显示详细信息
        path = getArguments().getString("filePath");
        imageView = view.findViewById(R.id.iv_img);
    }

    @Override
    public boolean onBackPressedSupport() {
        _mActivity.finish();
        return true;
    }

    private String path;
    private SubsamplingScaleImageView imageView;


    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.ll_great).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AppStoreUtil.getAppStoreIntent());
            }
        });
        view.findViewById(R.id.not_better).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(_mActivity, ClientReportBaseActivity.class));
            }
        });
        view.findViewById(R.id.fantastic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.iv_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    isOpen = false;
                    view.findViewById(R.id.ll_path).setVisibility(View.GONE);
                    view.findViewById(R.id.ll_date).setVisibility(View.GONE);
                } else {
                    isOpen = true;
                    view.findViewById(R.id.ll_path).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ll_date).setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
