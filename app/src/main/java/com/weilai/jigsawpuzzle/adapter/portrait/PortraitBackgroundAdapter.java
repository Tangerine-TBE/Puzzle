package com.weilai.jigsawpuzzle.adapter.portrait;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.bean.BackGroundBean;
import com.weilai.jigsawpuzzle.bean.BackGroundGroupBean;
import com.weilai.jigsawpuzzle.configure.Config;
import com.xinlan.imageeditlibrary.editimage.utils.AssetsUtil;

import java.util.List;

/**
 * * DATE: 2022/10/20
 * * Author:tangerine
 * * Description:
 **/
public class PortraitBackgroundAdapter extends RecyclerView.Adapter<PortraitBackgroundAdapter.ViewHolder> {
    private List<BackGroundBean> list;
    private final OnItemClickedListener onItemClickedListener;

    public PortraitBackgroundAdapter(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
    public final void setList(List<BackGroundBean> list){
        this.list = list;
    }

    public interface OnItemClickedListener {
        void onItemClicked(BackGroundBean backGroundBean);
    }

    @NonNull
    @Override
    public PortraitBackgroundAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_portrait_background, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PortraitBackgroundAdapter.ViewHolder holder, int position) {
        BackGroundBean backGroundBean = list.get(position);
        if (list.get(position).isNeedVip()) {
            holder.ivVip.setVisibility(View.VISIBLE);
        } else {
            holder.ivVip.setVisibility(View.INVISIBLE);
        }

        holder.ivImageBig.setImageBitmap(AssetsUtil.getAssertFile(Config.getApplicationContext(), list.get(position).getImgSmall()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickedListener.onItemClicked(backGroundBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImageBig;
        ImageView ivVip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImageBig = itemView.findViewById(R.id.image_big);
            ivVip = itemView.findViewById(R.id.vip_tip);
        }
    }
}
