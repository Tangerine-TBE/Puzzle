package com.weilai.jigsawpuzzle.adapter.puzzleHLP;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;
import com.luck.picture.lib.config.PictureMimeType;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.bean.PicInfo;
import com.weilai.jigsawpuzzle.util.BitmapUtils;
import com.weilai.jigsawpuzzle.util.DimenUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 ** DATE: 2022/10/13
 ** Author:tangerine
 ** Description:
 **/
public class HLongPicItemAdapter extends RecyclerView.Adapter<HLongPicItemAdapter.ViewHolder>{
    private final Context mContext;
    private final List<PicInfo> bitmaps;
    private View mLastSelectedView;
    private int mLastSelectedPosition = -1;
    public HLongPicItemAdapter(Context context, List<PicInfo> bitmaps) {
        this.mContext = context;
        this.bitmaps = bitmaps;
    }

    @NonNull
    @Override
    public HLongPicItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_pic_hlong, parent, false);
        return new HLongPicItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HLongPicItemAdapter.ViewHolder holder, int position) {
        Bitmap bitmap = null;
        String path = bitmaps.get(position).path;
        if (!TextUtils.isEmpty(path)) {
            Uri srcUri = BitmapUtils.pathToUri(path);
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
        int bitMapHeight = bitmap.getHeight();
        BigDecimal bitMapHeightBig = new BigDecimal(bitMapHeight);
        int viewHeight = DimenUtil.getScreenHeight()  / 2;
        BigDecimal viewHeightBig = new BigDecimal(viewHeight);
        float value = viewHeightBig.divide(bitMapHeightBig, 2, RoundingMode.HALF_DOWN).floatValue();
        BigDecimal valueBig = new BigDecimal(value);
        int bigMapWidth = bitmap.getWidth();
        int viewWidth = valueBig.multiply(new BigDecimal(bigMapWidth)).intValue();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(viewWidth,viewHeight);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        holder.layoutContent.setLayoutParams(layoutParams);
        ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(viewWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        holder.itemView.setLayoutParams(layoutParams1);
        FrameLayout.LayoutParams relayout = new FrameLayout.LayoutParams(viewWidth,viewHeight);
        ImageView image = holder.ivImage;
        image.setLayoutParams(relayout);
        image.setImageBitmap(bitmap);
        //加载一个占位图
        AppCompatImageView place = holder.ivPlace;
        place.setLayoutParams(relayout);
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
                mOnItemClickedListener.onItemClicked(holder.itemView,position);
            }
            //弹出多功能模块

        });
    }
    private boolean canSelected = true;
    private OnItemClickedListener mOnItemClickedListener;
    public final void setCanSelected() {
        canSelected = false;
    }
    public interface OnItemClickedListener{
        void onItemClicked(View itemView,int position);
    }
    public final void setOnItemClickedListener(OnItemClickedListener onItemClickedListener){
        this.mOnItemClickedListener = onItemClickedListener;
    }
    public final void resetItem(){
        if (mLastSelectedView != null){
            mLastSelectedView.setSelected(false);
            mLastSelectedPosition = -1;
        }
    }
    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        AppCompatImageView ivPlace;
        RelativeLayout layoutContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_img);
            ivPlace = itemView.findViewById(R.id.iv_place);
            layoutContent = itemView.findViewById(R.id.item_adjust);
        }
    }
}
