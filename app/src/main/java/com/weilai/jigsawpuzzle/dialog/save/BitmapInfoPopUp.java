package com.weilai.jigsawpuzzle.dialog.save;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.weilai.jigsawpuzzle.R;

import razerdp.basepopup.BasePopupWindow;

public class BitmapInfoPopUp extends BasePopupWindow {
    private TextView tvSize;
    private TextView tvDate;
    private TextView tvPath;
    private boolean isOpen;

    public BitmapInfoPopUp(Context context) {
        super(context);
        setContentView(R.layout.layout_save_pop);
        setBackgroundColor(Color.TRANSPARENT);
        setPopupGravityMode(GravityMode.RELATIVE_TO_ANCHOR);
        setOutSideTouchable(true);
        setOutSideDismiss(false);
        initView();
    }

    private void initView() {
        tvSize = findViewById(R.id.tv_size);
        tvDate = findViewById(R.id.tv_date);
        tvPath = findViewById(R.id.tv_path);
        ImageView ivOpen = findViewById(R.id.iv_open);
        ivOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    isOpen = false;
                    tvDate.setVisibility(View.GONE);
                    tvPath.setVisibility(View.GONE);
                } else {
                    isOpen = true;
                    tvDate.setVisibility(View.VISIBLE);
                    tvPath.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public final void show(View item,String size, String date, String path) {
        tvSize.setText(size);
        tvDate.setText(date);
        tvPath.setText(path);
        setPopupGravity( Gravity.BOTTOM | Gravity.CENTER);
        showPopupWindow(item);
    }
}
