package com.xiaolian.amigo.data.network.model.notify;

import lombok.Data;

/**
 * 通知模型
 * <p>
 * Created by zcd on 9/22/17.
 */
@Data
public class Notify {
    private String content;
    private Long createTime;
    private Long id;
    // 1 紧急公告 2 系统公告 3 客服消息
    private Integer type;
    // 1 未读 2 已读
    private Integer readStatus;
}
