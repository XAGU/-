package com.xiaolian.amigo.data.network.model.trade;

import lombok.Data;

/**
 * 对应"上一次"指令下发至设备的响应结果
 * <p>
 * Created by caidong on 2017/10/9.
 */
@Data
public class CmdResultReqDTO {

    // 设备mac地址
    String macAddress;

    // 指令响应内容
    String data;
}
