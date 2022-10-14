package com.weilai.jigsawpuzzle.fragment.puzzleSS;

import android.util.ArrayMap;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.puzzleSS.PuzzleSSAdapter;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.web.WebViewFragment;
import com.weilai.jigsawpuzzle.weight.template.SpacesItemDecoration;

import java.util.Objects;

/**
 * * DATE: 2022/10/14
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleSShotFragment extends BaseFragment implements PuzzleSSAdapter.OnItemClickListener {
    private final String[] oftenUse = new String[]{"www.", ".com", "http://", "https://", "百度", "360"};
    private AppCompatEditText mEtUrl;
    private AppCompatButton mBtnStart;
    private PuzzleSShotFragment() {

    }

    public static PuzzleSShotFragment getInstance() {
        return new PuzzleSShotFragment();
    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_screen_shot;
    }

    @Override
    protected void initView(View view) {
        ArrayMap<String, Integer> arrayMap = new ArrayMap<>();
        arrayMap.put(SpacesItemDecoration.TOP_SPACE, 20);
        arrayMap.put(SpacesItemDecoration.BOTTOM_SPACE, 20);
        arrayMap.put(SpacesItemDecoration.LEFT_SPACE, 20);
        arrayMap.put(SpacesItemDecoration.RIGHT_SPACE, 20);
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        RecyclerView mRvOftenUse = view.findViewById(R.id.rv_often_use);
        mEtUrl = view.findViewById(R.id.et_web);
        mBtnStart = view.findViewById(R.id.btn_start);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(_mActivity, 4);
        PuzzleSSAdapter puzzleSSAdapter = new PuzzleSSAdapter(oftenUse, this);
        mRvOftenUse.setAdapter(puzzleSSAdapter);
        mRvOftenUse.setLayoutManager(linearLayoutManager);
        mRvOftenUse.addItemDecoration(new SpacesItemDecoration(4,arrayMap,true));
        tvTitle.setText("网址长截图");
    }

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(WebViewFragment.getInstance(Objects.requireNonNull(mEtUrl.getText()).toString()));
            }
        });
    }

    @Override
    public void onItemClick(String item) {
        String text = "";
        if ("百度".equals(item)) {
            text = "https://www.baidu.com";
        } else if ("360".equals(item)) {
            text = "https://www.360.com";
        } else {
            text = item;
        }
        mEtUrl.setText(text);
        mEtUrl.requestFocus();
        mEtUrl.setSelection(text.length());
    }
}
