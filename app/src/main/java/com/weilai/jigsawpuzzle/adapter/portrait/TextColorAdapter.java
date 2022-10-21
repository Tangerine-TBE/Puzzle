package com.weilai.jigsawpuzzle.adapter.portrait;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.util.DimenUtil;

import java.util.List;

/**
 * * DATE: 2022/10/20
 * * Author:tangerine
 * * Description:
 **/
public class TextColorAdapter extends RecyclerView.Adapter<TextColorAdapter.ViewHolder> {
    private final List<Integer> list;
    private int selectedItem;
    private final OnItemClickedListener onItemClickedListener;
    public interface OnItemClickedListener{
        void onItemClicked(int position);
    }

    public TextColorAdapter(List<Integer> list, OnItemClickedListener onItemClickedListener) {
        this.list = list;
        this.onItemClickedListener = onItemClickedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_portrait_text_color, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.colorBg.setBackground(getBg(list.get(position),position == selectedItem));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem =holder.getAdapterPosition();
                onItemClickedListener.onItemClicked(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }
    public GradientDrawable getBg(int color,boolean is){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(DimenUtil.dipTopx(4f));
        if (is){
            gradientDrawable.setStroke(DimenUtil.dipTopxAsInt(1f),Color.parseColor("#ffffff"));
        }
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        return gradientDrawable;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View colorBg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorBg = itemView.findViewById(R.id.colorBg);
        }
    }
}
