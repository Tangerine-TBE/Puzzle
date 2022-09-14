package com.weilai.jigsawpuzzle.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.R;

import java.util.List;

/**
 ** DATE: 2022/9/14
 ** Author:tangerine
 ** Description: For RecyclerView
 **/
public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {
    private List<String> size;
    public TemplateAdapter(List<String> size){
        this.size = size;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return size.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.aiv_image);
            textView = itemView.findViewById(R.id.tv_name);
        }
    }
}
