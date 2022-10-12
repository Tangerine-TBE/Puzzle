package com.xinlan.imageeditlibrary.editimage.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinlan.imageeditlibrary.BaseActivity;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.ModuleConfig;
import com.xinlan.imageeditlibrary.editimage.fliter.PhotoProcessing;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouchBase;


/**
 * 滤镜列表fragment
 *
 * @author panyi
 */
public class FilterListFragment extends BaseEditFragment {
    public static final int INDEX = ModuleConfig.INDEX_FILTER;
    public static final String TAG = FilterListFragment.class.getName();
    private View mainView;
    private View backBtn;// 返回主菜单按钮

    private Bitmap fliterBit;// 滤镜处理后的bitmap

    private LinearLayout mFilterGroup;// 滤镜列表
    private String[] fliters;
    private int icons[];
    private Bitmap currentBitmap;// 标记变量

    public static FilterListFragment newInstance() {
        return new FilterListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_image_fliter, null);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        backBtn = mainView.findViewById(R.id.back_to_main);
        mFilterGroup = (LinearLayout) mainView.findViewById(R.id.filter_group);
        mainView.findViewById(R.id.iv_save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilterImage();

            }
        });
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
        setUpFliters();
    }

    @Override
    public void onShow() {
        activity.mode = EditImageActivity.MODE_FILTER;
        activity.mFilterListFragment.setCurrentBitmap(activity.getMainBit());
        activity.mainImage.setImageBitmap(activity.getMainBit());
        activity.mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        activity.mainImage.setScaleEnabled(false);
        activity.findViewById(R.id.tv_save).setVisibility(View.INVISIBLE);

    }

    /**
     * 返回主菜单
     */
    @Override
    public void backToMain() {
        currentBitmap = activity.getMainBit();
        fliterBit = null;
        activity.mainImage.setImageBitmap(activity.getMainBit());// 返回原图
        activity.mode = EditImageActivity.MODE_NONE;
        activity.bottomGallery.setCurrentItem(0);
        activity.mainImage.setScaleEnabled(true);
        activity.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);

    }

    /**
     * 保存滤镜处理后的图片
     */
    public void applyFilterImage() {
        // System.out.println("保存滤镜处理后的图片");
        if (currentBitmap == activity.getMainBit()) {// 原始图片
            // System.out.println("原始图片");
        } else {// 经滤镜处理后的图片
            // System.out.println("滤镜图片");
            activity.changeMainBitmap(fliterBit,true);
        }// end if
        backToMain();
    }

    /**
     * 装载滤镜
     */
    private void setUpFliters() {
        mFilterGroup.removeAllViews();
        fliters = getResources().getStringArray(R.array.filters);
        icons=new int[]{R.mipmap.yuantu,R.mipmap.ruanhua,R.mipmap.heibai
                ,R.mipmap.jindian,R.mipmap.huali,R.mipmap.fugu
                ,R.mipmap.youya,R.mipmap.dianying,R.mipmap.huiyi,R.mipmap.youge,R.mipmap.liunian,R.mipmap.faguang,R.mipmap.masaike,R.mipmap.manhua,R.mipmap.rgb};
        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(150,150);
        LinearLayout.LayoutParams parentViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewParams.gravity = Gravity.CENTER;
        imageViewParams.gravity = Gravity.CENTER;
        parentViewParams.leftMargin = 10;
        parentViewParams.rightMargin = 10;
        parentViewParams.gravity =Gravity.CENTER;

        for (int i = 0, len = fliters.length; i < len; i++) {
            TextView text = new TextView(activity);
            text.setTextColor(Color.BLACK);
            text.setTextSize(15);
            text.setText(fliters[i]);
            ImageView imageView = new ImageView(activity);
            imageView.setImageResource(icons[i]);
            imageView.setLayoutParams(imageViewParams);
            imageView.setId(0);
            LinearLayout linearLayout = new LinearLayout(activity);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(imageView);
            linearLayout.addView(text,textViewParams);
            linearLayout.setLayoutParams(parentViewParams);
            linearLayout.setTag(i);
            mFilterGroup.addView(linearLayout);
            linearLayout.setOnClickListener(new FliterClick());
        }// end for i
    }

    @Override
    public void onDestroy() {
        if (fliterBit != null && (!fliterBit.isRecycled())) {
            fliterBit.recycle();
        }
        super.onDestroy();
    }

    /**
     * 选择滤镜效果
     */
    private final class FliterClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            int position = ((Integer) v.getTag()).intValue();
            if (position == 0) {// 原始图片效果
                activity.mainImage.setImageBitmap(activity.getMainBit());
                currentBitmap = activity.getMainBit();
                return;
            }
            // 滤镜处理
            ProcessingImage task = new ProcessingImage();
            task.execute(position);
        }
    }// end inner class

    /**
     * 图片滤镜处理任务
     *
     * @author panyi
     */
    private final class ProcessingImage extends AsyncTask<Integer, Void, Bitmap> {
        private Dialog dialog;
        private Bitmap srcBitmap;

        @Override
        protected Bitmap doInBackground(Integer... params) {
            int type = params[0];
            if (srcBitmap != null && !srcBitmap.isRecycled()) {
                srcBitmap.recycle();
            }

            srcBitmap = Bitmap.createBitmap(activity.getMainBit().copy(
                    Bitmap.Config.ARGB_8888, true));
            return PhotoProcessing.filterPhoto(srcBitmap, type);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(Bitmap result) {
            super.onCancelled(result);
            dialog.dismiss();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result == null)
                return;
            if (fliterBit != null && (!fliterBit.isRecycled())) {
                fliterBit.recycle();
            }
            fliterBit = result;
            activity.mainImage.setImageBitmap(fliterBit);
            currentBitmap = fliterBit;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = BaseActivity.getLoadingDialog(getActivity(), R.string.handing,
                    false);
            dialog.show();
        }

    }// end inner class

    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    public void setCurrentBitmap(Bitmap currentBitmap) {
        this.currentBitmap = currentBitmap;
    }
}// end class
