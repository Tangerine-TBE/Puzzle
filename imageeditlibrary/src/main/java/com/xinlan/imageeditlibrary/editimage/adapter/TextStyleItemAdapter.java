package com.xinlan.imageeditlibrary.editimage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xinlan.imageeditlibrary.R;

/**
 ** DATE: 2022/10/12
 ** Author:tangerine
 ** Description:
 **/
public class TextStyleItemAdapter extends RecyclerView.Adapter<TextStyleItemAdapter.ViewHolder> {
    private final int[] icons;
    private final OnClickTextStyle mOnClickTextStyle;
    public TextStyleItemAdapter(int[] icons,OnClickTextStyle mOnClickTextStyle){
        this.icons = icons;
        this.mOnClickTextStyle = mOnClickTextStyle;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_style,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(icons[holder.getAdapterPosition()]);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickTextStyle.textStyleClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return icons.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_img);
        }
    }
    public interface OnClickTextStyle{
        void textStyleClicked(int position);
    }
}
