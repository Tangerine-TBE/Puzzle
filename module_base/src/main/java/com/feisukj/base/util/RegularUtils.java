package com.feisukj.base.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtils {

    /**
     * 验证手机号码是否合法
     */
    public static boolean validatePhoneNumber(String mobiles) {
//        String telRegex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147|145|149|166|167|191|193|198|199))\\d{8}$";
        String telRegex = "^1[3456789][0-9]{9}$";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    /**
     * 验证密码是否合法
     * */
    public static boolean isPassword(String password){
        String regex="^[a-zA-z0-9]{6,18}$";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(password);
        boolean isMatch=m.matches();
        return isMatch;
    }
}
