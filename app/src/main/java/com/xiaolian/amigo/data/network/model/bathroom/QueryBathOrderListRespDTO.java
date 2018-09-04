package com.xiaolian.amigo.data.network.model.bathroom;

import java.util.List;

import lombok.Data;


public class QueryBathOrderListRespDTO {
    private String missedTimes;   //  失约次数
    private String successTimes;   // 预约成功次数
    private int total;         // 总共次数
    private int totalMissTimes;   // 总共可失约次数
    private java.util.List<OrdersBean> orders;

    public String getMissedTimes() {
        return missedTimes;
    }

    public void setMissedTimes(String missedTimes) {
        this.missedTimes = missedTimes;
    }

    public String getSuccessTimes() {
        return successTimes;
    }

    public void setSuccessTimes(String successTimes) {
        this.successTimes = successTimes;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalMissTimes() {
        return totalMissTimes;
    }

    public void setTotalMissTimes(int totalMissTimes) {
        this.totalMissTimes = totalMissTimes;
    }

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

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

        public int getBathOrderId() {
            return bathOrderId;
        }

        public void setBathOrderId(int bathOrderId) {
            this.bathOrderId = bathOrderId;
        }

        public int getConsume() {
            return consume;
        }

        public void setConsume(int consume) {
            this.consume = consume;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(long expiredTime) {
            this.expiredTime = expiredTime;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

}
