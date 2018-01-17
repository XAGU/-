package com.xiaolian.amigo.data.network.model.trade;

import lombok.Data;

/**
 * 扫描二维码结账
 * <p>
 * Created by zcd on 18/1/17.
 */
@Data
public class QrCodeScanRespDTO {
    private String deviceToken;
    private String macAddress;
}
