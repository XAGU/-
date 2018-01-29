package com.xiaolian.amigo.data.network.model.trade;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 18/1/17.
 */
@Data
public class QrCodeScanReqDTO {
    private String qrCodeData;
    private Integer deviceType;
    // 用途 1-绑定 2-扫描结账
    private Integer purpose;
}
