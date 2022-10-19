package com.weilai.jigsawpuzzle.adapter.portrait;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.bean.BackGroundBean;

import java.util.List;

/**
 ** DATE: 2022/10/19
 ** Author:tangerine
 ** Description:
 **/
public class PortraitBackgroundGroupAdapter extends RecyclerView.Adapter<PortraitBackgroundGroupAdapter.ViewHolder> {
    private List<BackGroundBean> groupList;
    public PortraitBackgroundGroupAdapter(List<BackGroundBean> groupList){
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public PortraitBackgroundGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_portrait_background_group,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortraitBackgroundGroupAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivBig;
        ImageView ivVip;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            ivBig = itemView.findViewById(R.id.image_big);
            ivVip = itemView.findViewById(R.id.vip_tip);
        }
    }
}
