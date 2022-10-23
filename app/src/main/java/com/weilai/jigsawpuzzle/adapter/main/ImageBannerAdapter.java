package com.weilai.jigsawpuzzle.adapter.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.bean.CrossBannerEntity;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 ** DATE: 2022/9/13
 ** Author:tangerine
 ** Description:
 **/
public class ImageBannerAdapter extends BannerAdapter<CrossBannerEntity,ImageBannerAdapter.BannerViewHolder> {
    private Context context;

    public ImageBannerAdapter(List<CrossBannerEntity> datas, Context context) {
        super(datas);
        this.context =context;
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
 ** Description: test three images .the pic might be from the service;
 **/
    @Override
    public void onBindView(BannerViewHolder holder, CrossBannerEntity data, int position, int size) {
            Glide.with(context).load(data.getBannerurl()).placeholder(R.mipmap.ic_home_tab1).into(holder.imageView);
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public BannerViewHolder(@NonNull  View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView;
        }


    }
}
