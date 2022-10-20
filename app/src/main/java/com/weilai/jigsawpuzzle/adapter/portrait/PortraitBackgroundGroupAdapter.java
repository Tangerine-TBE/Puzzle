package com.weilai.jigsawpuzzle.adapter.portrait;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.bean.BackGroundGroupBean;
import com.weilai.jigsawpuzzle.configure.Config;
import com.xinlan.imageeditlibrary.editimage.utils.AssetsUtil;

import java.util.List;

/**
 * * DATE: 2022/10/19
 * * Author:tangerine
 * * Description:
 **/
public class PortraitBackgroundGroupAdapter extends RecyclerView.Adapter<PortraitBackgroundGroupAdapter.ViewHolder> {
    private final List<BackGroundGroupBean> groupList;
    private final OnItemClickListener onItemClickListener;
    public PortraitBackgroundGroupAdapter(List<BackGroundGroupBean> groupList, OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.groupList = groupList;
    }
    public interface OnItemClickListener{
        void onItemClicked(int position);
    }

    @NonNull
    @Override
    public PortraitBackgroundGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_portrait_background_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortraitBackgroundGroupAdapter.ViewHolder holder, int position) {
        BackGroundGroupBean backGroundBean = groupList.get(position);
        holder.tvName.setText(backGroundBean.getName());
        holder.ivBig.setImageBitmap(AssetsUtil.getAssertFile(Config.getApplicationContext(), backGroundBean.getImgBig()));
        if (backGroundBean.isNeedVip()) {
            holder.ivVip.setVisibility(View.VISIBLE);
        } else {
            holder.ivVip.setVisibility(View.INVISIBLE);
        }
        holder.tvBig.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivBig;
        ImageView ivVip;
        ImageView tvBig;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBig = itemView.findViewById(R.id.text_bg);
            tvName = itemView.findViewById(R.id.name);
            ivBig = itemView.findViewById(R.id.image_big);
            ivVip = itemView.findViewById(R.id.vip_tip);
        }
    }
}
