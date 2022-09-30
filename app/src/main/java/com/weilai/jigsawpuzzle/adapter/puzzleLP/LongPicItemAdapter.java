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
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.config.PictureMimeType;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.util.DimenUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * * DATE: 2022/9/27
 * * Author:tangerine
 * * Description:
 **/
public class LongPicItemAdapter extends RecyclerView.Adapter<LongPicItemAdapter.ViewHolder> {
    private Context mContext;
    private List<String> bitmaps;

    public LongPicItemAdapter(Context context, List<String> bitmaps) {
        this.mContext = context;
        this.bitmaps = bitmaps;
    }


    @NonNull
    @Override
    public LongPicItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_pic_long, parent, false);
        return new ViewHolder(view);
    }

    private View mLastSelectedView;
    private int mLastSelectedPosition = -1;

    @Override
    public void onBindViewHolder(@NonNull LongPicItemAdapter.ViewHolder holder, int position) {
        /*计算一次图片大小适配一下控件*/
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
        int bitMapWidth = bitmap.getWidth();
        BigDecimal bitMapWidthBig = new BigDecimal(bitMapWidth);
        int viewWidth = DimenUtil.getScreenWidth() * 3 / 4;
        BigDecimal viewWidthBig = new BigDecimal(viewWidth);
        double value = viewWidthBig.divide(bitMapWidthBig, 2, RoundingMode.HALF_UP).doubleValue();
        BigDecimal valueBig = new BigDecimal(value);
        int bigMapHeight = bitmap.getHeight();
        int viewHeight = valueBig.multiply(new BigDecimal(bigMapHeight)).intValue();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(viewWidth, viewHeight);
        AppCompatImageView image = holder.ivImage;
        image.setLayoutParams(layoutParams);
        Glide.with(mContext).load(bitmap).into(image);
        //加载一个占位图
        AppCompatImageView place = holder.ivPlace;
        place.setLayoutParams(layoutParams);
        if (mLastSelectedPosition == -1) {
            place.setSelected(false);
        } else {
            place.setSelected(position == mLastSelectedPosition);
        }
        place.setOnClickListener(v -> {
            if (canSelected) {
                if (mLastSelectedView != null) {
                    if (mLastSelectedView != v) {
                        mLastSelectedView.setSelected(false);
                        v.setSelected(true);
                        mLastSelectedView = v;
                        mLastSelectedPosition = holder.getAdapterPosition();
                    } else {
                        if (v.isSelected()) {
                            v.setSelected(false);
                            mLastSelectedView = null;
                            mLastSelectedPosition = -1;
                        } else {
                            v.setSelected(true);
                            mLastSelectedView = v;
                            mLastSelectedPosition = holder.getAdapterPosition();
                        }

                    }
                } else {
                    v.setSelected(true);
                    mLastSelectedView = v;
                    mLastSelectedPosition = holder.getAdapterPosition();
                }
            }
            //弹出多功能模块

        });
    }

    private boolean canSelected = true;

    public final void setCanSelected() {
        canSelected = false;
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView ivImage;
        AppCompatImageView ivPlace;
        FrameLayout layoutContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_img);
            ivPlace = itemView.findViewById(R.id.iv_place);
            layoutContent = itemView.findViewById(R.id.layout_content);
        }
    }
}
