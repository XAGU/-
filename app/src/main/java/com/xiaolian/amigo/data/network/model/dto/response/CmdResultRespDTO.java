package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by caidong on 2017/10/9.
 */
@Data
public class CmdResultRespDTO {

    /**
     * 下一步指令允许为空，如处理握手连接指令后，有以下两种场景
     * <br>
     * 1、如果该设备上存在未结算订单，这里会生成结算指令
     * <br>
     * 2、如果不存在未结算订单，改字段为空
     */
    String nextCommand;

    // 下一步指令类型
    Integer nextCommandType;

    // 原指令类型
    Integer srcCommandType;
}
