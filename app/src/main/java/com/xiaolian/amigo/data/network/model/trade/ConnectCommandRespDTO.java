package com.xiaolian.amigo.data.network.model.trade;

import lombok.Data;

/**
 * @author caidong
 * @date 17/10/9
 */
@Data
public class ConnectCommandRespDTO {

    /**
     * 连接设备指令内容
     */
    private String connectCmd;
    String macAddress;
    String deviceToken;
}
