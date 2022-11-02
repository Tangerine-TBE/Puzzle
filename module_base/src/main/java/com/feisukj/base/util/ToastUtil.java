package com.feisukj.base.util;

import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feisukj.base.BaseApplication;
import com.feisukj.base.R;

/**
 * 描述:Toast工具
 */
public class ToastUtil {
    public static void showShortToast(int rId) {
        showShortToast(BaseApplication.getApplication().getString(rId));
    }

    public static void showLongToast(int rId) {
        showLongToast(BaseApplication.getApplication().getString(rId));
    }

    public static void showShortToast(String content) {
        Toast.makeText(BaseApplication.getApplication(),content, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String content) {
        Toast.makeText(BaseApplication.getApplication(),content, Toast.LENGTH_LONG).show();
    }

    public static void showImgToast(){
        Toast toast = new Toast(BaseApplication.getApplication());
        View view = LayoutInflater.from(BaseApplication.getApplication()).inflate(R.layout.toast,null,false);
        TextView textView = view.findViewById(R.id.tv_toast);
        ImageView imageView = view.findViewById(R.id.iv_toast);
        textView.setText("转写成功");
        imageView.setImageResource(R.drawable.ic_transfer_to_complete);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static void showImgToast(int res,String title){
        Toast toast = new Toast(BaseApplication.getApplication());
        View view = LayoutInflater.from(BaseApplication.getApplication()).inflate(R.layout.toast,null,false);
        TextView textView = view.findViewById(R.id.tv_toast);
        ImageView imageView = view.findViewById(R.id.iv_toast);
        textView.setText(title);
        imageView.setImageResource(res);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showCenterToast(String s){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.R){
            try{
                Toast toast = new Toast(BaseApplication.getApplication());
                View view = LayoutInflater.from(BaseApplication.getApplication()).inflate(R.layout.toast_,null,false);
                TextView textView = view.findViewById(R.id.tv_toast);
                textView.setText(s);
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(BaseApplication.getApplication(),s,Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(BaseApplication.getApplication(),s,Toast.LENGTH_SHORT).show();
        }

    }

    /*
     * 防止弹出多次吐司
     * */
    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showToast(String s) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getApplication(), s, Toast.LENGTH_SHORT);
            toast.show();
            oldMsg = s;
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

}
