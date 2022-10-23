package com.weilai.jigsawpuzzle.fragment.puzzleLPic;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.bean.PicInfo;
import com.weilai.jigsawpuzzle.event.LpSplitEvent;
import com.weilai.jigsawpuzzle.util.EvenUtil;
import com.weilai.jigsawpuzzle.util.ToastUtil;
import com.weilai.jigsawpuzzle.weight.puzzleLP.PuzzleLpEditView;

import java.util.ArrayList;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * * DATE: 2022/10/8
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleLpSplitFragment extends BaseFragment {
    private PuzzleLpEditView mEditBottom;
    private PuzzleLpEditView mEditTop;
    private int type;

    private PuzzleLpSplitFragment() {
    }

    public static PuzzleLpSplitFragment getInstance(ArrayList<PicInfo> bitmapInfo, int type, int mode) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("bitmapInfo", bitmapInfo);
        bundle.putInt("type", type);
        bundle.putInt("mode", mode);
        PuzzleLpSplitFragment puzzleLpSplitFragment = new PuzzleLpSplitFragment();
        puzzleLpSplitFragment.setArguments(bundle);
        return puzzleLpSplitFragment;
    }

    @Override
    protected Object setLayout() {
        type = getArguments().getInt("type");
        int mode = getArguments().getInt("mode");
        if (mode == 1) {
            return R.layout.fragment_lp_edit;
        } else {
            return R.layout.fragment_h_lp_edit;
        }
    }

    @Override
    protected void initView(View view) {
        assert getArguments() != null;
        mEditBottom = view.findViewById(R.id.edit_bottom);
        mEditTop = view.findViewById(R.id.edit_top);
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        view.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
        AppCompatTextView tvSave = view.findViewById(R.id.tv_save);
        tvSave.setText("确定");
        tvTitle.setText("");

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        ArrayList<PicInfo> bitmaps = getArguments().getParcelableArrayList("bitmapInfo");
        type = getArguments().getInt("type");
        if (type == 1) {
            //裁顶部
            mEditTop.setImage(bitmaps.get(0).path);
        } else if (type == 2) {
            //裁底部
            if (bitmaps.size() >= 2) {
                mEditTop.setImage(bitmaps.get(1).path);
            }
            mEditBottom.setImage(bitmaps.get(0).path);
        }
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        view.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save
                showProcessDialog();
                ArrayList<String> paths = new ArrayList<>();
                mEditBottom.saveImage().subscribeOn(Schedulers.io()).flatMap((Function<String, ObservableSource<String>>) s -> {
                    if (!TextUtils.isEmpty(s)){
                        paths.add(s);
                    }
                    return mEditTop.saveImage();
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(String s) {
                        if (!TextUtils.isEmpty(s)){
                            paths.add(s);
                        }
                        EvenUtil.post(new LpSplitEvent(paths, type));
                        hideProcessDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProcessDialog();
                        ToastUtil.showToast("编辑失败!请重新编辑");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
    }
}
