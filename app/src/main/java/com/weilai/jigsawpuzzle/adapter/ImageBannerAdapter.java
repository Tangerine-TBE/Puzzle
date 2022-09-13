package com.weilai.jigsawpuzzle.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.R;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 ** DATE: 2022/9/13
 ** Author:tangerine
 ** Description:
 **/
public class ImageBannerAdapter extends BannerAdapter<String,ImageBannerAdapter.BannerViewHolder> {


    public ImageBannerAdapter(List<String> datas) {
        super(datas);
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);
    }
/**
 ** DATE: 2022/9/13
 ** Author:tangerine
 ** Description: test three images
 **/
    @Override
    public void onBindView(BannerViewHolder holder, String data, int position, int size) {
        if (position == 0){
            holder.imageView.setImageResource(R.mipmap.ic_home_tab1);
        } else if (position == 1) {
            holder.imageView.setImageResource(R.mipmap.ic_home_tab2);
        }else if (position == 2){
            holder.imageView.setImageResource(R.mipmap.ic_home_tab3);
        }
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public BannerViewHolder(@NonNull  View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView;
        }


    }
}
