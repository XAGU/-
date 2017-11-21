package com.xiaolian.amigo.util;

import com.xiaolian.amigo.BuildConfig;

public final class Constant {

    private static final String TAG = "Constant";
    public static final int DEFAULT_TIMEOUT = 10;
    public static final int VERIFY_CODE_TIME = 30;

    private Constant() {
    }

    // 页内内容条数
    public static final int PAGE_SIZE = 10;

    // 起始页数
    public static final int PAGE_START_NUM = 1;

    // activity通过intent跳转之间传递的extra key
    public static final String EXTRA_KEY = "detail";

    // 中文冒号分隔符
    public static final String CHINEASE_COLON = "：";

    // 测试服务器
//    public static final String SERVER_TEST = "http://116.62.236.67:5081";
    public static final String SERVER_TEST = BuildConfig.SERVER;
//    public static final String SERVER_TEST = "http://10.0.0.4:8081";
//    public static final String SERVER_TEST = "http://10.0.0.2:5081";

    // 当前使用的服务器
    public static final String SERVER = SERVER_TEST;

    // 图片地址前缀
    public static String IMAGE_PREFIX = BuildConfig.FILE_PREFIX;

    // H5地址
    public static final String H5_SERVER = BuildConfig.H5_SERVER;
//    public static final String H5_SERVER = "http://10.0.0.5:5000";
    // H5帮助中心
    public static final String H5_HELP = H5_SERVER + "/help/list";
    // 设备未找零
    public static final String H5_PREPAY_HELP = H5_SERVER + "/help?first=1&last=1";

    // H5用户协议
    public static final String H5_AGREEMENT = H5_SERVER + "/agreement";
//    public static final String H5_AGREEMENT = "http://www.script-tutorials.com/demos/199/index.html";

    // H5投诉建议
    public static final String H5_COMPLAINT = H5_SERVER + "/complaint";

    // H5意见反馈
    public static final String H5_FEEDBACK = H5_SERVER + "/feedback";

    // activity之间跳转携带的bundle id key
    public static final String BUNDLE_ID = "id";

    public static final Long INVALID_ID = -1L;

    // 寝室楼
    public static final int ROOM_BUILDING_TYPE = 1;

    // 跳转至ListChooseActivity的来源
    public static final String REPAIR_APPLY_ACTIVITY_SRC = "repair_apply";
    public static final String EDIT_PROFILE_ACTIVITY_SRC = "edit_profile";
    public static final String MAIN_ACTIVITY_SRC = "main_activity";

    // 位置
    public static final String LOCATION = "location";

    // 位置id
    public static final String LOCATION_ID = "location_id";

    // intent action
    public static final String INTENT_ACTION = "intent_action";

    // 设备类型
    public static final String DEVICE_TYPE = "device_type";

    // token
    public static final String TOKEN = "token";

    // 上传文件content type
    public static final String UPLOAD_FILE_CONTENT_TYPE = "multipart/form-data";

    // 上传图片content type
    public static final String UPLOAD_IMAGE_CONTENT_TYPE = "image/jpeg";

    // 列数
    public static final int COLUMN_NUM = 3;

    // 设备mac
    public static final String MAC = "mac";

    // 等比缩放
    public static final String OSS_IMAGE_RESIZE = "?x-oss-process=image/resize,m_mfit,h_%d,w_%d";

    // md5签名
    public static final String MD5_SIG = "crmOiRz1Bmv0b6f6Zx3BdbrGpLnvdYk";

}
