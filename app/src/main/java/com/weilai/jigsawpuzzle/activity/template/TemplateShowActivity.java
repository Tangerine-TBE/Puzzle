package com.weilai.jigsawpuzzle.activity.template;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseActivity;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;

import java.io.ByteArrayOutputStream;

/**
 * * DATE: 2022/9/14
 * * Author:tangerine
 * * Description: @TemplateActivity's RecyclerViewItem click join this Activity
 **/
public class TemplateShowActivity extends BaseActivity {
    private AppCompatTextView mTvConfirm;

    @Override
    protected Object setLayout() {
        return R.layout.activity_template_show;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(TemplateShowActivity.this, 0, null);

    }

    @Override
    protected void initView(View view) {
        AppCompatImageView imageView = view.findViewById(R.id.iv_img);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        imageView.setImageBitmap(bitmap);
        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), TemplateEditActivity.class).putExtra("template",baos.toByteArray() ));
            }
        });
    }

    @Override
    protected void initListener(View view) {

    }
}
