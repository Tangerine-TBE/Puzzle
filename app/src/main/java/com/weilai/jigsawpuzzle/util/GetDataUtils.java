package com.weilai.jigsawpuzzle.util;

import android.text.TextUtils;

import com.feisukj.base.bean.ad.ADConstants;
import com.feisukj.base.bean.ad.TypeBean;
import com.weilai.jigsawpuzzle.Constants;
import com.weilai.jigsawpuzzle.ad.UserBean;

public class GetDataUtils {

    /**
     * 是否为VIP
     * */
    public static boolean isVip(){
        int vipNum;
        try{
            vipNum = GsonUtils.parseObject(SPUtil.getInstance().getString(Constants.USER_BEAN), UserBean.class).getData().getVip();
        }catch (Exception e){
            vipNum = 0;
        }
        SPUtil.getInstance().putBoolean(Constants.IS_VIP,vipNum!=0);
        return vipNum!=0;
    }

    /**
     * VIP到期时间
     * */
    public static String vipExpireTime(){
        try {
            return GsonUtils.parseObject(SPUtil.getInstance().getString(Constants.USER_BEAN), UserBean.class).getData().getVipexpiretime();
        }catch (Exception e){
            return "";
        }
    }

    /**
     * 是否有广告
     * */
    public static boolean isAd(){
        TypeBean bean = null;
        String string = SPUtil.getInstance().getString(ADConstants.START_PAGE);
        if(!TextUtils.isEmpty(string))
            bean = GsonUtils.parseObject(string,TypeBean.class);
        if(bean == null){
            return false;
        }else{
            return bean.getBanner_screen().getStatus();
        }
    }
}
