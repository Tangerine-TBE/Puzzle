package com.weilai.jigsawpuzzle.adapter.puzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.weight.puzzle.slant.NumberSlantLayout;
import com.weilai.jigsawpuzzle.weight.puzzle.straight.NumberStraightLayout;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.PuzzleView;

import java.util.List;

/**
 * * DATE: 2022/9/23
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleSizeAdapter extends RecyclerView.Adapter<PuzzleSizeAdapter.ViewHolder> {
    private final Context mContext;
    private final List<PuzzleLayout> mPuzzleLayouts;
    private final OnItemClickListener mOnItemClickListener;
    private final List<Bitmap> pics;
    private PuzzleLayout mCurrentPuzzleLayout;
    private View mCurrentView;

    public PuzzleSizeAdapter(Context context, List<PuzzleLayout> puzzleLayouts, OnItemClickListener onItemClickListener, List<Bitmap> pics, PuzzleLayout puzzleLayout) {
        this.mContext = context;
        this.mPuzzleLayouts = puzzleLayouts;
        this.mOnItemClickListener = onItemClickListener;
        this.pics = pics;
        this.mCurrentPuzzleLayout = puzzleLayout;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_puzzle_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        PuzzleLayout puzzleLayout = mPuzzleLayouts.get(position);
        holder.puzzleView.setTouchEnable(false);
        holder.puzzleView.setPuzzleLayout(puzzleLayout);
        holder.puzzleView.addPieces(pics);
        holder.puzzleView.setPiecePadding(20);
        holder.puzzleView.setLineSize(6);
        holder.puzzleView.setNeedDrawLine(true);
        holder.puzzleView.setLineColor(mContext.getResources().getColor(R.color.sel_text_main_color));
        holder.puzzleView.setNeedDrawOuterLine(true);
        if (puzzleLayout instanceof NumberSlantLayout && mCurrentPuzzleLayout instanceof NumberSlantLayout) {
            NumberSlantLayout puzzleLayoutNumber = (NumberSlantLayout) puzzleLayout;
            NumberSlantLayout currentLayoutNumber = (NumberSlantLayout) mCurrentPuzzleLayout;
            if (puzzleLayoutNumber.getTheme() == currentLayoutNumber.getTheme()) {
                holder.frameLayout.setSelected(true);
                mCurrentView = holder.frameLayout;
            } else {
                holder.frameLayout.setSelected(false);
            }
        } else if (puzzleLayout instanceof NumberStraightLayout && mCurrentPuzzleLayout instanceof NumberStraightLayout) {
            NumberStraightLayout puzzleLayoutNumber = (NumberStraightLayout) puzzleLayout;
            NumberStraightLayout currentLayoutNumber = (NumberStraightLayout) mCurrentPuzzleLayout;
            if (puzzleLayoutNumber.getTheme() == currentLayoutNumber.getTheme()) {
                holder.frameLayout.setSelected(true);
                mCurrentView = holder.frameLayout;
            } else {
                holder.frameLayout.setSelected(false);
            }
        } else {
            holder.frameLayout.setSelected(false);
        }

        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.frameLayout != mCurrentView) {
                    holder.frameLayout.setSelected(true);
                    if (mCurrentView != null) {
                        mCurrentView.setSelected(false);

                    }
                    mCurrentView = holder.frameLayout;
                    mCurrentPuzzleLayout = puzzleLayout;
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(puzzleLayout, pics);
                    }
                } else {
                    holder.frameLayout.setSelected(true);
                    mCurrentPuzzleLayout = puzzleLayout;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPuzzleLayouts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        PuzzleView puzzleView;
        FrameLayout frameLayout;
        ViewHolder(View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.layout_content);
            puzzleView = itemView.findViewById(R.id.puzzle_view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(PuzzleLayout puzzleLayout, List<Bitmap> pics);
    }

}
