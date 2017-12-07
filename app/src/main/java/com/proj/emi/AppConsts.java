package com.proj.emi;

public class AppConsts {

    public static final boolean DEBUG = true;

    private static final String HOST_TEST = "http://116.62.242.69:10131/";
    private static final String HOST_RELEASE = "XXXXX";
    public static final String HOST =
            DEBUG ? HOST_TEST :
                    HOST_RELEASE;
    public static final String HOST_IMAGE = HOST;

    public static final String KEY =
            DEBUG ? "e1a6de148eacbbfa22a852d004d30aad556213cedd79708339f47cb8ee24b150"
                    : "1d2163e0ad11007f1684daf80716098e2b56f1745510c27c526dcafaf4d25afc";

    //------------------服务协议--------------------
    public final static String SERVICE_AGREEMENTS_URL
            = "xxx";
    public final static String SERVICE_PHONE = "0571-xxx";

    public final static String CACHE_ROOT = "life_museum";
    public final static String FIT_PWD_KEY = "living";
    public final static String CHAR_SET = "utf-8";

    public final static int ACTREQ_CAMERA = 100;
    public final static int ACTREQ_PICK_IMAGE = 101;
    public final static int ACTREQ_CLIP_AVATAR = 102;
    public final static int ACTREQ_PICK_PLACE = 110;

    public final static String EX_USERID = "user_uuid";
    public final static String EX_PLACE = "pick_place";
    public final static String EX_COOR_LAT = "latitude";
    public final static String EX_COOR_LNG = "longitude";
    public final static String EX_ADDRESS = "address";
    public final static String EX_INDEX = "index";


    public static final String APP_ID = "";//微信平台申请的ID

}
