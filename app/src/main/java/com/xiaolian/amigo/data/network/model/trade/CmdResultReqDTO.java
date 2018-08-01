package com.xiaolian.amigo.data.network.model.trade;

import lombok.Data;

/**
 * 对应"上一次"指令下发至设备的响应结果
 *
 * @author caidong
 * @date 17/10/9
 */
@Data
public class CmdResultReqDTO {

    /**
     * 设备mac地址
     */
    String macAddress;

    /**
     * 指令响应内容
     */
    String data;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
