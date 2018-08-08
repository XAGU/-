package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

@Data
public class BathOrderCurrentRespDTO {

        /**
         * balance : 0
         * bathOrderId : 0
         * createTime : 0
         * location : string
         * minPrepay : 0
         * missedTimes : 0
         * prepay : 0
         * prepayAmount : 0
         * status : 0
         * totalMissTimes : 0
         * tradeOrderId : 0
         */

        private double balance;
        private long bathOrderId;
        private long createTime;
        private String location;
        private double minPrepay;
        private int missedTimes;
        private double prepay;
        private double prepayAmount;
        private int status;
        private int totalMissTimes;
        private long tradeOrderId;

}
