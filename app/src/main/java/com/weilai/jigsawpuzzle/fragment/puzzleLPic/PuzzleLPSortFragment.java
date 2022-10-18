package com.weilai.jigsawpuzzle.fragment.puzzleLPic;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.puzzleLP.LongPicItemSortAdapter;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.event.LpSortEvent;
import com.weilai.jigsawpuzzle.util.EvenUtil;
import com.weilai.jigsawpuzzle.weight.puzzleLP.ItemDrag;
import java.util.ArrayList;

/**
 * * DATE: 2022/9/29
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleLPSortFragment extends BaseFragment {
    public static PuzzleLPSortFragment getInstance(ArrayList<String> bitmaps) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("data", bitmaps);
        PuzzleLPSortFragment puzzleLPSortFragment = new PuzzleLPSortFragment();
        puzzleLPSortFragment.setArguments(bundle);
        return puzzleLPSortFragment;
    }


    private RecyclerView mRvSort;

    private PuzzleLPSortFragment() {

    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_lp_sort;
    }
    private ArrayList<String> bitmaps ;
    @Override
    protected void initView(View view) {
        bitmaps = getArguments().getStringArrayList("data");
        mRvSort = view.findViewById(R.id.rv_sort);
        AppCompatTextView title = view.findViewById(R.id.tv_title);
        title.setText("排序");
        view.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        mRvSort.setLayoutManager(linearLayoutManager);
        LongPicItemSortAdapter longPicItemAdapter = new LongPicItemSortAdapter(_mActivity, bitmaps);
        mRvSort.setAdapter(longPicItemAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemDrag(bitmaps, longPicItemAdapter));
        touchHelper.attachToRecyclerView(mRvSort);
        view.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.tips).setVisibility(View.GONE);
            }
        });
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
                EvenUtil.post(new LpSortEvent(bitmaps));
            }
        });
    }
}
