package com.xiaolian.amigo.data.network.model.notify;

import java.util.List;

import lombok.Data;

@Data
public class RollingNotifyRespDTO {

    /**
     * 滚动公告
     */
    private List<String> rollingNotifyList ;
}
