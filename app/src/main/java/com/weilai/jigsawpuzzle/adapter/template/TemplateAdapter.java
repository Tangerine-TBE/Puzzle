package com.weilai.jigsawpuzzle.adapter.template;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.feisukj.ad.adapter.BaseAdAdapter;
import com.kuaishou.weapon.p0.jni.A;
import com.kwad.sdk.api.KsAdVideoPlayConfig;
import com.kwad.sdk.api.KsFeedAd;
import com.kwad.sdk.utils.ad;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;
import com.weilai.jigsawpuzzle.util.DimenUtil;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.L;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * * DATE: 2022/9/14
 * * Author:tangerine
 * * Description: For RecyclerView
 **/
public class TemplateAdapter extends BaseAdAdapter {
    private List<Object> mapInfos = new ArrayList<>();
    private Activity mContext;
    private ItemClickListener mItemClickListener;
    private SmartRefreshLayout smartRefreshLayout;
    private static final int VIEW_TYPE_ONE = 1;
    private static final int VIEW_TYPE_TWO = 2;

    public TemplateAdapter(Activity context, ItemClickListener itemClickListener, SmartRefreshLayout refreshLayout) {
        this.mContext = context;
        this.mItemClickListener = itemClickListener;
        this.smartRefreshLayout = refreshLayout;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_ONE:
                return new TwoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad, parent, false));
            default:
                return new OneViewHolder( LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template, parent, false));
        }


    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_TWO:
                OneViewHolder oneViewHolder = (OneViewHolder) holder;
                BitMapInfo bitMapInfo = (BitMapInfo) mapInfos.get(position);
                oneViewHolder.textView.setText(bitMapInfo.getName());
                Bitmap bitmap = FileUtil.getBitmapFromCache(bitMapInfo.getName() + "bitmap");
                assert bitmap != null;
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                int targetWidth = DimenUtil.getScreenWidth() * 3 / 7;
                BigDecimal bigDecimal = new BigDecimal(targetWidth);
                float scaleSize = bigDecimal.divide(new BigDecimal(width), 2, RoundingMode.HALF_UP).floatValue();
                int targetHeight = (int) (height * scaleSize);
                ViewGroup.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(targetWidth, targetHeight);
                oneViewHolder.imageView.setLayoutParams(layoutParams);
                Glide.with(mContext).load(bitmap).into(oneViewHolder.imageView);
                String valueJson = JSONObject.toJSONString(bitMapInfo);
                oneViewHolder.layoutContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.itemClick(valueJson);
                    }
                });
                break;
            case VIEW_TYPE_ONE:
                TwoViewHolder twoViewHolder = (TwoViewHolder) holder;
                Object object = mapInfos.get(position);
                if (object instanceof TTNativeExpressAd) {
                    TTNativeExpressAd ad = (TTNativeExpressAd) object;
                    ad.render();
                    View view = ad.getExpressAdView();
                    if (view.getParent() instanceof ViewGroup){
                        ((ViewGroup) view.getParent()).removeAllViews();
                    }
                    twoViewHolder.frameLayout.removeAllViews();
                    twoViewHolder.frameLayout.addView(view);

                } else {
                    KsFeedAd ad = (KsFeedAd) object;
                    KsAdVideoPlayConfig videoPlayConfig = new KsAdVideoPlayConfig.Builder()
                            .videoSoundEnable(false) // 是否有声播放
                            .videoAutoPlayType(KsAdVideoPlayConfig.VideoAutoPlayType.AUTO_PLAY) // 设置有网时自动播放
                            .dataFlowAutoStart(false)
                            .build();
                    ad.setVideoPlayConfig(videoPlayConfig);
                    View view = ad.getFeedView(mContext);
                    if (view !=null && view.getParent() == null ){
                        twoViewHolder.frameLayout.removeAllViews();
                        twoViewHolder.frameLayout.addView(view);
                    }
                }
                break;
            default:
                break;
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (mapInfos.get(position) instanceof BitMapInfo) {
            return VIEW_TYPE_TWO;
        } else {
            return VIEW_TYPE_ONE;
        }
    }

    @Override
    public void addAdList(List<?> list) {
        ArrayList<Object> reBuildLists = new ArrayList<>();
        //先请求一次BitmapInfo 后再进行广告list重组
        if (list != null) {
            int nextAdPosition = 0;
            int lastAdPosition = 0;
            for (int i = 0; i < mapInfos.size(); i++) {
                reBuildLists.add(mapInfos.get(i));
                if (!list.isEmpty() && nextAdPosition == i) {
                    if (lastAdPosition > list.size() - 1) {
                        lastAdPosition = 0;
                    }
                    if (i != 0) {
                        reBuildLists.add(list.get(lastAdPosition));
                    }
                    lastAdPosition += 1;
                    nextAdPosition += 4;
                }
            }
            mapInfos = reBuildLists;
        }
        notifyDataSetChanged();
        smartRefreshLayout.finishRefresh(true);
    }

    public final void setMapInfos(List<Object> list) {
        if (!mapInfos.isEmpty()) {
            mapInfos.clear();
        }
        mapInfos.addAll(list);
//                notifyDataSetChanged();
//        smartRefreshLayout.finishRefresh(true);

    }

    public interface ItemClickListener {
        void itemClick(String jsonValue);
    }

    @Override
    public int getItemCount() {
        return mapInfos.size();
    }

    public static class OneViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayoutCompat layoutContent;

        public OneViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutContent = itemView.findViewById(R.id.layout_content);
            imageView = itemView.findViewById(R.id.aiv_image);
            textView = itemView.findViewById(R.id.tv_name);
        }
    }

    public static class TwoViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameLayout;

        public TwoViewHolder(View item) {
            super(item);
            frameLayout = item.findViewById(R.id.layout_content);
        }
    }
}
