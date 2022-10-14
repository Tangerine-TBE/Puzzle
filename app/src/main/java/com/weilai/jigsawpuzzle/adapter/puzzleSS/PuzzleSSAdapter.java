package com.weilai.jigsawpuzzle.adapter.puzzleSS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.R;

/**
 ** DATE: 2022/10/14
 ** Author:tangerine
 ** Description:
 **/
public class PuzzleSSAdapter extends RecyclerView.Adapter<PuzzleSSAdapter.ViewHolder> {
    private final String[] oftenUse;
    public PuzzleSSAdapter (String[] oftenUse,OnItemClickListener onItemClickListener){
        this.oftenUse = oftenUse;
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ss_view,parent,false);

        return new ViewHolder( view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.mTvItem.setText(oftenUse[position]);
            holder.mTvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(oftenUse[holder.getAdapterPosition()]);
                }
            });
    }
    public interface OnItemClickListener{
        void onItemClick(String item);
    }
    private final  OnItemClickListener onItemClickListener;


    @Override
    public int getItemCount() {
        return oftenUse.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView mTvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvItem = itemView.findViewById(R.id.tv_item);
        }
    }
}
