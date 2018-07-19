package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

@Data
public class QueryBathOrderListRespDTO {
    private int missedTimes;
    private int successTimes;
    private int total;
    private int totalMissTimes;
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

        private int bathOrderId;
        private int consume;
        private int createTime;
        private int expiredTime;
        private String location;
        private int status;
    }

}
