package com.xiaolian.amigo.data.network.model.bathroom;


import lombok.Data;

/**
 * 预约信息
 */
@Data
public class BookingInfo {

    private long bathOrderId;
    private long createTime;
    private long deviceNo;
    private long expiredTime;
    private long  id;
    private String location;
    private int status;   // 订单状态

}
