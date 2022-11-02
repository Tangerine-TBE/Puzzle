package com.feisukj.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.feisukj.base.util.LogUtils;
import com.feisukj.base.util.PackageUtils;

public class DisplayLaunchTipDialog extends DialogFragment implements View.OnClickListener {

    private Context context;
    private DisplayLaunchTipListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_display_launch_tip,container,false);
        setCancelable(false);
        TextView textView1 = view.findViewById(R.id.tv_dialog_launch_no);
        TextView textView2 = view.findViewById(R.id.tv_dialog_launch_yes);
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        title((TextView) view.findViewById(R.id.tv_dialog_launch_title));
        body((TextView) view.findViewById(R.id.tv_dialog_launch_body));
        return view;
    }

    private void title(TextView view){
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("用户服务协议和隐私政策");
        //setSpan可多次使用
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);//粗体
        spannableString.setSpan(styleSpan, 0,11, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        StyleSpan styleSpan2 = new StyleSpan(Typeface.ITALIC);//斜体
//        spannableString.setSpan(styleSpan2, 3, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        StyleSpan styleSpan3 = new StyleSpan(Typeface.BOLD_ITALIC);//粗斜体
//        spannableString.setSpan(styleSpan3, 6, 9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        view.setText(spannableString);
    }
    private void body(TextView view){
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        SpannableString fuwu = new SpannableString("《用户服务协议》");
        SpannableString yinsi = new SpannableString("《隐私政策》");
        SpannableString qx1 = new SpannableString("读取手机状态和身份");
        SpannableString qx3 = new SpannableString("访问手机存储设备");
        SpannableString qx4 = new SpannableString("相机权限");

        fuwu.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                LogUtils.INSTANCE.e("点击了服务");
                Intent intent = new Intent(context, AgreementContentActivity.class);
                intent.putExtra(AgreementContentActivity.FLAG,AgreementContentActivity.FLAG_FUWU);
                startActivity(intent);
            }
        },0,fuwu.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        yinsi.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                LogUtils.INSTANCE.e("点击了隐私");
                Intent intent = new Intent(context, AgreementContentActivity.class);
                intent.putExtra(AgreementContentActivity.FLAG,AgreementContentActivity.FLAG_YINSI);
                startActivity(intent);
            }
        },0,yinsi.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        fuwu.setSpan(new ForegroundColorSpan(Color.parseColor("#FFD23D")),0,fuwu.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        yinsi.setSpan(new ForegroundColorSpan(Color.parseColor("#FFD23D")),0,yinsi.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);//粗体
        ForegroundColorSpan styleSpan2 = new ForegroundColorSpan(Color.parseColor("#000000"));

//        qx1.setSpan(styleSpan1,0,qx1.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        qx2.setSpan(styleSpan1,0,qx2.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        qx3.setSpan(styleSpan1,0,qx3.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        qx4.setSpan(styleSpan1,0,qx4.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        qx1.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),0,qx1.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        qx3.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),0,qx3.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        qx4.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),0,qx4.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



        stringBuilder.append("\t\t\t\t尊敬的用户欢迎使用“").append(PackageUtils.getAppName(context)).append("”在您使用前请仔细阅读")
                .append(fuwu)
                .append("和")
                .append(yinsi)
                .append(",包括将严格遵守您同意的各项条款使用您的信息,以便为您提供更好的服务。\n\t\t\t\t点击“同意”意味着您以阅读并同意。\n\n")
                .append(qx1)
                .append("\n友盟统计需要，获取设备唯一标识信息，用于统计用户使用情况\n\n")
                .append(qx3).append("\n").append(PackageUtils.getAppName(context)).append("需要访问存储设备以存储文件\n\n")
        .append(qx4)
        .append("\n用户为画板添加背景或反馈问题时上传图片会调用相机拍摄图片");
        view.setText(stringBuilder);
        view.setMovementMethod(LinkMovementMethod.getInstance());//必须设置才能响应点击事件
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if(listener == null)
            return;
        if(i == R.id.tv_dialog_launch_no){
            listener.no();
        }else if(i == R.id.tv_dialog_launch_yes){
            listener.yes();
        }
    }

    public void setDisplayLaunchTipListener(DisplayLaunchTipListener listener){
        this.listener = listener;
    }

    public interface DisplayLaunchTipListener {
        void yes();
        void no();
    }
}
