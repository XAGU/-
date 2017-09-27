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
    private Integer type;
}
