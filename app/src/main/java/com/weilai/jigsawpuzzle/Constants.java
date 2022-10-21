package com.weilai.jigsawpuzzle;

public class Constants {
    public static String BaseUrl = "https://catapi.aisou.club";
    public static String TOKEN_VALUE = "x389fh^feahykge";
    public static String NONCE_VALUE = "523146";
    public static String TIMESTAMP = "timestamp";
    public static String SIGNATURE = "signature";

    public static String IMAGE_LIST = "lockscreenclock/imageList";

    public static String SEE_VIDEO_TIME = "see_video_time";

    //验证码倒计时时间
    public static final int SECOND = 30;

    public static final String TOKEN = "^x389fhfeahykge";

    ///////////////////SharedPreferences_key////////////////////
    //是否手机登录
    public static final String IS_LOGIN = "islogin";
    //是否第三方登录
    public static final String IS_LOGIN_THIRD = "isloginbythird";
    //是否为VIP
    public static final String IS_VIP = "isvip";
    //用户名称
    public static final String USER_NAME = "username";
    //第三方名称
    public static final String OTHER_NAME = "othername";
    //密码
    public static final String USER_PWD = "userpwd";
    //是否发送验证码
    public static final String IS_CODE = "iscode";
    //记住账号
    public static final String IS_REMEMBERUSER = "isrememberuser";
    //记住密码
    public static final String IS_REMEMBERPWD = "isrememberpwd";
    //用户信息
    public static final String USER_BEAN = "userbean";
    //支付宝包名
    public static final String PACKAGE_NAME_ALI = "com.eg.android.AlipayGphone";
    //微信的包名
    public static final String PACKAGE_NAME_WX = "com.tencent.mm";
    //QQ的包名
    public static final String PACKAGE_NAME_QQ = "com.tencent.mobileqq";

    public static final String PAY_TYPE_ALI = "ALI";

    public static final String PAY_TYPE_WX = "WX";
    //微信APP_ID
    public static final String WX_APP_ID = "wx05504ec4879504a6";
    //QQ APP_ID
    public static final String QQ_APP_ID = "102021526";
    //微信AppSecret
    public static final String WX_SECRET = "e38b91481ce75220c1c8cf0e9bc1666b";
    //第三方登录类型
    public static final String LOGIN_TYPE = "logintype";
    //openID
    public static final String OPENID = "openid";



    //域名
    public static final String URL = "http://www.aisou.club/api.php";
    //支付
    public static final String PAY_WX_URL = "http://www.aisou.club/pay/wxh5/dafa.php?";
    public static final String PAY_ALI_URL = "http://www.aisou.club/pay/aliv2/wappay/pay.php?";
    ////////////////////SeverName///////////////////
    //注册验证码
    public static final String GET_CODE = "passport.regcode";
    //找回密码验证码
    public static final String GET_FIND_PWD_CODE = "passport.findPassword";
    //校验验证码
    public static final String CHECK_CODE = "passport.checkcode";
    //注册
    public static final String ADD_USER = "passport.registerByMobile";
    //登录
    public static final String LOGIN = "passport.loginMobile";
    //找回密码
    public static final String FIND_PWD = "passport.setPassByFind";
    //ali_pay
    public static final String ALI_PAY = "";
    //wx_pay
    public static final String WX_PAY = "";
    //验证QQ微信是否注册
    public static final String CHECK_THIRD = "passport.checkThird";
    //第三方注册
    public static final String REGISTER_BY_THIRD = "passport.registerByThird";
    //第三方登录
    public static final String LOGIN_THIRD = "passport.loginThird";
    //删除账号
    public static final String DELETE_USER = "passport.unregister";



    //
    public static final Long MAX_PIC_FILE = 1024*1024*5L;

    //倒计时时间
    public static final Long DOWN_COUNT = 1000*60*60L-1000;
    public static final String DOWN_COUNT_START_TIME = "down_count_time";


}
