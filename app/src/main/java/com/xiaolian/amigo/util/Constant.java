package com.xiaolian.amigo.util;

import com.xiaolian.amigo.BuildConfig;

/**
 * app 常量
 *
 * @author caidong
 * @date 17/9/15
 */
public final class Constant {
    private Constant() {
    }

    private static final String TAG = "Constant";
    /**
     * 默认超时
     */
    public static final int DEFAULT_TIMEOUT = 10;
    /**
     * 验证码时间
     */
    public static final int VERIFY_CODE_TIME = 30;
    /**
     * 温馨提示最大提醒次数
     */
    public static final int REMIND_ALERT_COUNT = 3;
    /**
     * 设备扫描超时
     */
    public static final int DEVICE_SCAN_TIMEOUT = 20;
    /**
     * 微信app id
     */
//    public static final String WECHAT_APP_ID = "";

    /**
     * 页内内容条数
     */
    public static final int PAGE_SIZE = 10;

    /**
     * 起始页数
     */
    public static final int PAGE_START_NUM = 1;

    /**
     * activity通过intent跳转之间传递的extra key
     */
    public static final String EXTRA_KEY = "detail";

    /**
     * 通过bundle传递数据时的key
     */
    public static final String DATA_BUNDLE = "data_bundle";

    /**
     * 中文冒号分隔符
     */
    public static final String CHINEASE_COLON = "：";

    /**
     * 测试服务器
     */
    public static final String SERVER_TEST = BuildConfig.SERVER;

    /**
     * 当前使用的服务器
     */
    public static final String SERVER = SERVER_TEST;
    public static final String SERVER_BATHROOM = BuildConfig.SERVER_BATHROOM;

    /**
     * 图片地址前缀
     */
    public static String IMAGE_PREFIX = BuildConfig.FILE_PREFIX;

    /**
     * H5地址
     */
    public static String H5_SERVER = BuildConfig.H5_SERVER;
    /**
     * H5帮助中心
     */
    public static final String H5_HELP = H5_SERVER + "/help/list";
    /**
     * H5门禁
     */
    public static final String H5_GATE = H5_SERVER + "/entrance";
    /**
     * 设备未找零
     */
    public static final String H5_PREPAY_HELP = H5_SERVER + "/help?first=1&last=1";
    /**
     * H5用户协议
     */
    public static final String H5_AGREEMENT = H5_SERVER + "/agreement";
    /**
     * H5投诉建议
     */
    public static final String H5_COMPLAINT = H5_SERVER + "/complaint";
    /**
     * H5意见反馈
     */
    public static final String H5_FEEDBACK = H5_SERVER + "/feedback";
    /**
     * H5账户迁移
     */
    public static final String H5_MIGRATE = H5_SERVER + "/migration/home";
    /**
     * 账单里没用水却扣钱的引导页
     */
    public static final String H5_NO_USE_HELP = H5_SERVER + "/help/detail?toNative=1";

    /**
     * activity之间跳转携带的bundle id key
     */
    public static final String BUNDLE_ID = "id";

    /**
     * 非法id
     */
    public static final Long INVALID_ID = -1L;

    /**
     * 寝室楼
     */
    public static final int ROOM_BUILDING_TYPE = 1;

    /**
     * 跳转至ListChooseActivity的来源
     */
    public static final String REPAIR_APPLY_ACTIVITY_SRC = "repair_apply";
    public static final String EDIT_PROFILE_ACTIVITY_SRC = "edit_profile";
    public static final String MAIN_ACTIVITY_SRC = "main_activity";
    public static final String USER_INFO_ACTIVITY_SRC = "user_info";
    public static final String COMPLETE_INFO_ACTIVITY_SRC = "complete_info"; //新增编辑个人信息方式
    public static final String MAIN_ACTIVITY_BATHROOM_SRC = "main_activity_bathroom_src";  //  公共浴室方式
    public static final String ADD_BATHROOM_SRC = "add_bathroom_src" ;  //  新增公共浴室


    /**
     * 位置
     */
    public static final String LOCATION = "location";

    /**
     * 位置id
     */
    public static final String LOCATION_ID = "location_id";

    /**
     * intent action
     */
    public static final String INTENT_ACTION = "intent_action";

    /**
     * 设备类型
     */
    public static final String DEVICE_TYPE = "device_type";

    /**
     * token
     */
    public static final String TOKEN = "token";

    /**
     * 上传文件content type
     */
    public static final String UPLOAD_FILE_CONTENT_TYPE = "multipart/form-data";

    /**
     * 上传图片content type
     */
    public static final String UPLOAD_IMAGE_CONTENT_TYPE = "image/jpeg";

    /**
     * 列数
     */
    public static final int COLUMN_NUM = 3;

    /**
     * 设备mac
     */
    public static final String MAC = "mac";

    /**
     * 等比缩放
     */
    public static final String OSS_IMAGE_RESIZE = "?x-oss-process=image/resize,h_%d";

    /**
     * md5签名
     */
    public static final String MD5_SIG = "crmOiRz1Bmv0b6f6Zx3BdbrGpLnvdYk";

    /**
     * 默认banner
     */
    public static final int DEFAULT_BANNER_TYPE = 3;
    public static final String DEFAULT_BANNER_IMAGE = "system/123.jpg";
    public static final String DEFAULT_BANNER_LINK = "https://c.h5.xiaolian365.com/manual";

    /**
     * 分割符
     */
    public static final String DIVIDER = ":";

    /**
     * 推送 building tag
     */
    public static final String MD5_BUILDING_STR = "_GpiGd3pl3Zr2ZXC0";
    /**
     * 推送 school tag
     */
    public static final String MD5_SCHOOL_STR = "_MTxQd1buFokZayzT";
    /**
     * 推送 uid account
     */
    public static final String MD5_UID_STR = "_jtL2T8nYY5D0klEm";



    /**
     *  预约状态
     */
    public static final int INIT = 1 ;  // 初始化
    public static final int ACCEPTED = 2 ;  // 已处理
    public static final int FAIL = 3 ;  // 预约失败
    public static final int CANCELED = 4 ;  // 取消
    public static final int EXPIRED = 5 ;  // 过期
    public static final int OPENED = 6 ;  // 已经开阀，同时支付
    public static final int FINISHED = 7 ;  // 已经结算


    /**
     * 预约的方式状态
     */

    public static final int BOOKING =  1  ;  // 预约

    public static final int BUY_CODE = 2 ;  //  购买编码

    public static final int SCANNING = 3 ;  //  扫一扫

    public static final int USING = 4 ;  //  直接使用

    /**
     * 上一笔订单状态
     */
    public static final int WAIT_STATUS = 2 ;  // 正在排队

    public static final int TIMEOUT_STATUS = 3 ;  // 等待洗浴

    public static final int USING_STATUS = 4 ; //  正在洗浴


    /**
     * 预览图公共浴室状态
     */
    public static final int NONE =  0 ;   // 没有

    public static final int AVAILABLE =  1 ;   //  可用

    public static final int BATH_USING = 2 ;   // 正在使用

    public static final int ERROR = 3 ;    //  报错


    /**
     * 业务状态
     */


    public static final int SHOWER = 1 ;

    public static final int WATER = 2 ;

    public static final int HAIR_DRYER = 3 ;

    public static final int WASHING = 4 ;

    public static final int PUB_BATH = 5 ;


    /**
     * 上一个订单的状态
     */
    public static final int EMPTY = 1 ;  // 空闲

    public static final int QUEUEING = 2 ;  //  排队中

    public static final int BOOKING_SUCCESS = 3 ; // 进入预约流程

    public static final  int PRE_USING = 4 ; // 进入使用流程


    /**
     * 预约状态
     */
    public static final int BOOKING_DEVICE = 1 ;  //  预约楼层

    public static final int BOOKING_FLOOR = 2 ;  //  预约设备


    /**
     * order  流程
     */
    public static final int ORDER_USING = 1 ;  // 使用中

    public static final int ORDER_SETTLE = 2 ;   //  已结算
}
