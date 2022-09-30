package com.weilai.jigsawpuzzle.adapter.puzzleLP;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.config.PictureMimeType;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.util.DimenUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 ** DATE: 2022/9/30
 ** Author:tangerine
 ** Description:
 **/
public class LongPicItemSortAdapter extends RecyclerView.Adapter<LongPicItemSortAdapter.ViewHolder> {

    private Context mContext;
    private List<String> bitmaps;

    public LongPicItemSortAdapter(Context context, List<String> bitmaps) {
        this.mContext = context;
        this.bitmaps = bitmaps;
    }


    @NonNull
    @Override
    public LongPicItemSortAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_long_pic_sort, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LongPicItemSortAdapter.ViewHolder holder, int position) {
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(bitmaps.get(position))) {
            Uri srcUri;
            if (PictureMimeType.isContent(bitmaps.get(position)) || PictureMimeType.isHasHttp(bitmaps.get(position))) {
                srcUri = Uri.parse(bitmaps.get(position));
            } else {
                srcUri = Uri.fromFile(new File(bitmaps.get(position)));
            }
            if (srcUri != null) {
                InputStream stream = null;
                try {
                    stream = mContext.getContentResolver().openInputStream(srcUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeStream(stream);
            }
        }
        if (bitmap == null) {
            return;
        }
        AppCompatImageView image = holder.imageView;
        Glide.with(mContext).load(bitmap).into(image);
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_img);
        }
    }
}
