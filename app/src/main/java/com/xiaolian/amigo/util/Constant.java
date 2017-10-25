package com.xiaolian.amigo.util;

public final class Constant {

    private static final String TAG = "Constant";
    public static final int DEFAULT_TIMEOUT = 10;

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
//    public static final String SERVER_TEST = "https://c.api.xiaolian365.com";
//    public static final String SERVER_TEST = "http://10.0.0.4:8081";
    public static final String SERVER_TEST = "http://10.0.0.2:5081";

    // 当前使用的服务器
    public static final String SERVER = SERVER_TEST;

    // 图片地址前缀
    public static final String IMAGE_PREFIX = SERVER + "/images/";

    // H5地址
    public static final String H5_SERVER = "https://c.h5.xiaolian365.com";
//    public static final String H5_SERVER = "http://10.0.0.5:5000";
    // H5帮助中心
    public static final String H5_HELP = H5_SERVER + "/help/list";

    // H5用户协议
    public static final String H5_AGREEMENT = H5_SERVER + "/agreement";
//    public static final String H5_AGREEMENT = "http://www.script-tutorials.com/demos/199/index.html";

    // H5投诉建议
    public static final String H5_COMPLAINT = H5_SERVER + "/complaint";

    // H5意见反馈
    public static final String H5_FEEDBACK = H5_SERVER + "/feedback";

    // activity之间跳转携带的bundle id key
    public static final String BUNDLE_ID = "id";

    // 寝室楼
    public static final int ROOM_BUILDING_TYPE = 1;

    // 跳转至ListChooseActivity的来源
    public static final String REPAIR_APPLY_ACTIVITY_SRC = "repair_apply";

    // 位置
    public static final String LOCATION = "location";

    // 位置id
    public static final String LOCATION_ID = "location_id";

    // 设备类型
    public static final String DEVICE_TYPE = "device_type";

    // 上传文件content type
    public static final String UPLOAD_FILE_CONTENT_TYPE = "multipart/form-data";

    // 上传图片content type
    public static final String UPLOAD_IMAGE_CONTENT_TYPE = "image/jpeg";

    // 列数
    public static final int COLUMN_NUM = 3;

    // 设备mac
    public static final String MAC = "mac";

}
