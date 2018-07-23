package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

@Data
public class QueryBathOrderListRespDTO {
    private int missedTimes;   //  失约次数
    private int successTimes;   // 预约成功次数
    private int total;         // 总共次数
    private int totalMissTimes;   // 总共可失约次数
    private java.util.List<OrdersBean> orders;

    @Data
    public static class OrdersBean {
        /**
         * bathOrderId : 0
         * consume : 0
         * createTime : 0
         * expiredTime : 0
         * location : string
         * status : 0
         */

        private int bathOrderId;   // 订单Id
        private int consume;     //  消费金额
        private long createTime;   // 创建时间
        private long expiredTime;   // 过期时间
        private String location;  // 设备位置
        private int status;
    }

}
