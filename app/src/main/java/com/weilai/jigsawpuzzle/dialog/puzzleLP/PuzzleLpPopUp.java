package com.weilai.jigsawpuzzle.dialog.puzzleLP;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.util.DimenUtil;
import com.weilai.jigsawpuzzle.util.L;

import razerdp.basepopup.BasePopupWindow;

/**
 * * DATE: 2022/10/8
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleLpPopUp extends BasePopupWindow implements View.OnClickListener {
    private String[] titles;
    private int[] mipmaps;
    private LinearLayoutCompat llTips;
    @Override
    public void onClick(View v) {
        onPopUpDismiss.clicked(v,position);
    }

    public interface OnPopUpDismiss {
        void dismiss();
        void clicked(View view,int position);
    }

    private OnPopUpDismiss onPopUpDismiss;

    public PuzzleLpPopUp(Context context, OnPopUpDismiss onPopUpDismiss, String[] titles, int[] mipmaps) {
        super(context);
        this.onPopUpDismiss = onPopUpDismiss;
        this.titles = titles;
        this.mipmaps = mipmaps;
        setContentView(R.layout.layout_edit_tool);
        initView();
        setOutSideTouchable(true);
        setOutSideDismiss(false);
        setBackgroundColor(Color.TRANSPARENT);
        setPopupGravityMode(GravityMode.RELATIVE_TO_ANCHOR);
    }
    private void initView() {
         llTips = findViewById(R.id.tips);
        llTips.setLayoutParams(new RelativeLayout.LayoutParams(DimenUtil.getScreenWidth() * 3 / 4, WRAP_CONTENT));
        for (int i = 0; i < titles.length; i++) {
            LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1);
            LinearLayoutCompat llSplitTop = new LinearLayoutCompat(getContext());
            llSplitTop.setLayoutParams(layoutParams);
            llSplitTop.setOrientation(LinearLayoutCompat.VERTICAL);
            llSplitTop.setGravity(Gravity.CENTER);
            llSplitTop.setWeightSum(1);
            AppCompatImageView appCompatImageView = new AppCompatImageView(getContext());
            appCompatImageView.setImageResource(mipmaps[i]);
            llSplitTop.addView(appCompatImageView, layoutParams);
            AppCompatTextView appCompatTextView = new AppCompatTextView(getContext());
            appCompatTextView.setText(titles[i]);
            appCompatTextView.setPadding(0, 5, 0, 0);
            appCompatTextView.setTextColor(Color.WHITE);
            llSplitTop.addView(appCompatTextView, layoutParams);
            llSplitTop.setId(i);
            llSplitTop.setTag(i);
            llSplitTop.setOnClickListener(this);
            llTips.addView(llSplitTop);
        }
        setWidth(llTips.getWidth());
        setHeight(llTips.getHeight());
    }

    public final void show(View item, boolean isShowTop) {
        llTips.setVisibility(View.VISIBLE);
        setPopupGravity(isShowTop ? Gravity.TOP | Gravity.CENTER : Gravity.BOTTOM | Gravity.CENTER);
        showPopupWindow(item);
    }
    private int position;
    public final void show(View item,boolean isShowTop,int position){
        llTips.setVisibility(View.VISIBLE);
        this.position = position;
        if (position > 0){
            getContentView().findViewWithTag(0).setVisibility(View.GONE);
        }else{
            getContentView().findViewWithTag(0).setVisibility(View.VISIBLE);
        }
        setPopupGravity(isShowTop ? Gravity.TOP | Gravity.CENTER : Gravity.BOTTOM | Gravity.CENTER);
        showPopupWindow(item);
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        if (onPopUpDismiss != null) {
            onPopUpDismiss.dismiss();
        }
    }
    public final void setVisibility(){
        llTips.setVisibility(View.INVISIBLE);
    }
}
