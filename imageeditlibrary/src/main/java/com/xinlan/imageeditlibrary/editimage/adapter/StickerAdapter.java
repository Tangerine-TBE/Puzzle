package com.xinlan.imageeditlibrary.editimage.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.fragment.StickerFragment;


/**
 * 贴图分类列表Adapter
 *
 * @author panyi
 */
public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
    private final Context mContext;
    private final int [] mBitmapInfo;
    public StickerAdapter(Context context,int[] bitmapInfo,OnImageViewClicked onImageViewClicked){
        this.mContext = context;
        this.mBitmapInfo = bitmapInfo;
        this.onImageViewClicked = onImageViewClicked;
    }
    public interface OnImageViewClicked{
        void imageViewClicked(int id);
    }
    private final OnImageViewClicked onImageViewClicked;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final  View view = LayoutInflater.from(mContext).inflate(R.layout.view_sticker_item,parent,false) ;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(mBitmapInfo[holder.getAdapterPosition()]);
        holder.imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageViewClicked.imageViewClicked(mBitmapInfo[holder.getAdapterPosition()]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBitmapInfo.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
        }
    }
}
