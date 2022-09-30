package com.weilai.jigsawpuzzle.weight.puzzleLP;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.adapter.puzzleLP.LongPicItemAdapter;
import com.weilai.jigsawpuzzle.adapter.puzzleLP.LongPicItemSortAdapter;
import com.weilai.jigsawpuzzle.util.L;

import java.util.Collections;
import java.util.List;

/**
 * * DATE: 2022/9/30
 * * Author:tangerine
 * * Description:
 **/
public class ItemDrag extends ItemTouchHelper.Callback {
    public ItemDrag(List<String> bitmaps, LongPicItemSortAdapter longPicItemAdapter){
        this.bitmaps = bitmaps;
        this.longPicItemAdapter = longPicItemAdapter;
    }
    private final List<String> bitmaps;
    private final LongPicItemSortAdapter longPicItemAdapter;
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN| ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();  //取得第一个item的position
        int toPosition = target.getAdapterPosition();    //取得目标item的position
        Collections.swap(bitmaps, fromPosition, toPosition);  //mChoosed是Recylerview的data集合，将两个item交换
        longPicItemAdapter.notifyItemMoved(fromPosition, toPosition);  //recylerview的adapter通知交换更新
        return true;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }


}
