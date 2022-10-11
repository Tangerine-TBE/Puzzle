package com.xinlan.imageeditlibrary.editimage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xinlan.imageeditlibrary.R;

/**
 ** DATE: 2022/9/27
 ** Author:tangerine
 ** Description:
 **/
public class ColorItemAdapter extends RecyclerView.Adapter<ColorItemAdapter.ViewHolder> {
    private Context mContext;
    private JSONArray mColors;
    private OnColorPickedListener mListener;
    public ColorItemAdapter(Context context, JSONArray colors, OnColorPickedListener listener){
        this.mContext = context;
        this.mColors = colors;
        this.mListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_color,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject jsonObject = mColors.getJSONObject( mColors.size()-1-position);
        String color = jsonObject.getString("value");
        holder.textView.setBackgroundColor(Color.parseColor(color));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onColorPicked(color);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mColors.size();
    }
    public interface OnColorPickedListener{
        void onColorPicked(String color);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_color);
        }
    }
}
