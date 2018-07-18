package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * @author zcd
 * @date 18/7/4
 */
@Data
public class BathBookingReqDTO {
    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * 交易类型 1预约 2购买编码 3 扫一扫 4 直接使用
     */
    private Integer type;
}
