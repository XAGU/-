package com.xiaolian.amigo.util;

public final class Constant {

    private static final String TAG = "Constant";

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
    public static final String SERVER_TEST = "http://116.62.236.67:5081";

    // 当前使用的服务器
    public static final String SERVER = SERVER_TEST;

    // activity之间跳转携带的bundle id key
    public static final String BUNDLE_ID = "id";

    // 寝室楼
    public static final int ROOM_BUILDING_TYPE = 1;

}
