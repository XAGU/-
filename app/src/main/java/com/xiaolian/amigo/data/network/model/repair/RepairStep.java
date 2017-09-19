package com.xiaolian.amigo.data.network.model.repair;

import lombok.Data;

/**
 * 报修进度
 * <p>
 * Created by caidong on 2017/9/19.
 */
@Data
public class RepairStep {

    // 进度描述
    private String content;
    // 报修状态
    private Integer status;
    // 该步骤操作发生时间
    private Long time;

}
