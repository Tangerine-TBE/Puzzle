package com.weilai.jigsawpuzzle.adapter.template;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.net.base.NetConfig;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;
import com.weilai.jigsawpuzzle.util.DimenUtil;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.L;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * * DATE: 2022/9/14
 * * Author:tangerine
 * * Description: For RecyclerView
 **/
public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {
    private List<BitMapInfo> mapInfos;
    private Context mContext;
    private ItemClickListener mItemClickListener;

    public TemplateAdapter(List<BitMapInfo> size, Context context, ItemClickListener itemClickListener) {
        this.mapInfos = size;
        this.mContext = context;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BitMapInfo bitMapInfo = mapInfos.get(position);
        holder.textView.setText(bitMapInfo.getName());
        Bitmap bitmap = FileUtil.getBitmapFromCache(bitMapInfo.getName() + "bitmap");
        assert bitmap != null;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int targetWidth = DimenUtil.getScreenWidth() * 3/7;
        BigDecimal bigDecimal = new BigDecimal(targetWidth);
        float scaleSize = bigDecimal.divide(new BigDecimal(width),2,RoundingMode.HALF_UP).floatValue();
        int targetHeight = (int) (height * scaleSize );
        ViewGroup.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(targetWidth,targetHeight);
        holder.imageView.setLayoutParams(layoutParams);
        Glide.with(mContext).load(bitmap).into(holder.imageView);
        String valueJson = JSONObject.toJSONString(bitMapInfo);
        holder.layoutContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.itemClick(valueJson);
            }
        });
    }

    public interface ItemClickListener {
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
