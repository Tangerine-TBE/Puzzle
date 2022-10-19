package com.weilai.jigsawpuzzle.adapter.puzzle9P;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.weilai.jigsawpuzzle.R;
import java.util.List;

/**
 * * DATE: 2022/9/28
 * * Author:tangerine
 * * Description:
 **/
public class Puzzle9PAdapter extends RecyclerView.Adapter<Puzzle9PAdapter.ViewHolder> {
    private final Context mContext;
    private final List<String> list;

    public Puzzle9PAdapter(Context context, List<String> bitmaps) {
        this.mContext = context;
        this.list = bitmaps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_puzzle_9p, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String bitmap = list.get(position);
        Glide.with(mContext).load(bitmap).into(holder.appCompatImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView appCompatImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appCompatImageView = itemView.findViewById(R.id.iv_img);
        }
    }
}
