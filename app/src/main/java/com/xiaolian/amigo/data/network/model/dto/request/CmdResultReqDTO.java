package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 对应"上一次"指令下发至设备的响应结果
 * <p>
 * Created by caidong on 2017/10/9.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdResultReqDTO {

    // 设备mac地址
    @NonNull
    String macAddress;

    // 指令响应内容
    @NonNull
    String data;
}
