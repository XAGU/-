package com.xiaolian.amigo.data.network.model.bathroom;


import lombok.Data;

@Data
public class BathOrderPreconditionRespDTO {
        /**
         * bathBookingId : 0
         * bathOrderId : 0
         * createTime : 0
         * deviceNo : 0
         * existUsingOrder : false
         * expiredTime : 0
         * location : string
         * maxMissAbleTimes : 0
         * missedTimes : 0
         * prepayAmount : 0
         * prepayInfo : {"balance":0,"minPrepay":0,"prepay":0}
         * status : 0
         * type : 0
         */

        private long bathBookingId;
        private long bathOrderId;
        private long bathQueueId;  //  排队id
        private int maxMissAbleTimes ;
        private int missedTimes ;
        private PrepayInfoBean prepayInfo;
        private int status;



    @Data
    public static class PrepayInfoBean {
            /**
             * balance : 0
             * minPrepay : 0
             * prepay : 0
             */

            private double balance;
            private double minPrepay;
            private double prepay;


    }
    }

