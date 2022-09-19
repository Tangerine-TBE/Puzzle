package com.weilai.jigsawpuzzle.adapter.template;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;

import java.util.List;

/**
 ** DATE: 2022/9/14
 ** Author:tangerine
 ** Description: For RecyclerView
 **/
public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {
    private List<BitMapInfo> mapInfos;
    private Context mContext;
    private ItemClickListener mItemClickListener;
    public TemplateAdapter(List<BitMapInfo> size, Context context,ItemClickListener itemClickListener){
        this.mapInfos = size;
        this.mContext = context;
        this.mItemClickListener =itemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       BitMapInfo bitMapInfo =  mapInfos.get(position);
       if (bitMapInfo != null){
           String name = bitMapInfo.getName();
           holder.textView.setText(name);
           if (name.equals("模板1:1")){
               Glide.with(mContext).load(R.mipmap.template_show_11).into(holder.imageView);
           }else if (name.equals("模板3:2")){
               Glide.with(mContext).load(R.mipmap.template_shhow_32).into(holder.imageView);
           }else if (name.equals("角度模板1:1")){
               Glide.with(mContext).load(R.mipmap.template_show_rorate_11).into(holder.imageView);

           }else if (name.equals("角度模板3:2")){
               Glide.with(mContext).load(R.mipmap.template_show_rorate_32).into(holder.imageView);
           }
           String valueJson = JSONObject.toJSONString(bitMapInfo);
            holder.layoutContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.itemClick(valueJson);
                }
            });

       }
    }
    public interface ItemClickListener{
        void itemClick(String jsonValue);
    }
    @Override
    public int getItemCount() {
        return mapInfos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayoutCompat layoutContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutContent = itemView.findViewById(R.id.layout_content);
            imageView = itemView.findViewById(R.id.aiv_image);
            textView = itemView.findViewById(R.id.tv_name);
        }
    }
}
