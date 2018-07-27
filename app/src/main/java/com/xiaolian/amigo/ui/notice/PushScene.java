package com.xiaolian.amigo.ui.notice;


/**
 * 推送场景
 * Created by szy on 2018/5/29
 *
 * @author shizhouyong
 */
public enum PushScene {
    /**
     * 需要返回首页
     */
    DEFAULT(1),
    /**
     * 维修员收到新的工单任务
     */
    REPAIRMAN_RECEIVE_TASK(2),
    /**
     * 工单完结
     */
    WORK_ORDER_FINISHED(3),
    /**
     * 维修员维修提醒：接单提醒
     */
    REPAIRMAN_TASK_ACCEPT_REMIND(4),
    /**
     * 维修员维修提醒：完结提醒
     */
    REPAIRMAN_TASK_FINISH_REMIND(5),
    /**
     * 通知详情页
     */
    NOTICE_DETAIL(6);

    private int code;

    PushScene(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PushScene getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (PushScene pushScene : PushScene.values()) {
            if (code == pushScene.getCode()) {
                return pushScene;
            }
        }
        return null;
    }
}
