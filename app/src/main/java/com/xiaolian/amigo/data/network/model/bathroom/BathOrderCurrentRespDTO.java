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

        public double getBalance() {
                return balance;
        }

        public void setBalance(double balance) {
                this.balance = balance;
        }

        public long getBathOrderId() {
                return bathOrderId;
        }

        public void setBathOrderId(long bathOrderId) {
                this.bathOrderId = bathOrderId;
        }

        public long getCreateTime() {
                return createTime;
        }

        public void setCreateTime(long createTime) {
                this.createTime = createTime;
        }

        public String getLocation() {
                return location;
        }

        public void setLocation(String location) {
                this.location = location;
        }

        public double getMinPrepay() {
                return minPrepay;
        }

        public void setMinPrepay(double minPrepay) {
                this.minPrepay = minPrepay;
        }

        public int getMissedTimes() {
                return missedTimes;
        }

        public void setMissedTimes(int missedTimes) {
                this.missedTimes = missedTimes;
        }

        public double getPrepay() {
                return prepay;
        }

        public void setPrepay(double prepay) {
                this.prepay = prepay;
        }

        public double getPrepayAmount() {
                return prepayAmount;
        }

        public void setPrepayAmount(double prepayAmount) {
                this.prepayAmount = prepayAmount;
        }

        public int getStatus() {
                return status;
        }

        public void setStatus(int status) {
                this.status = status;
        }

        public int getTotalMissTimes() {
                return totalMissTimes;
        }

        public void setTotalMissTimes(int totalMissTimes) {
                this.totalMissTimes = totalMissTimes;
        }

        public long getTradeOrderId() {
                return tradeOrderId;
        }

        public void setTradeOrderId(long tradeOrderId) {
                this.tradeOrderId = tradeOrderId;
        }
}
