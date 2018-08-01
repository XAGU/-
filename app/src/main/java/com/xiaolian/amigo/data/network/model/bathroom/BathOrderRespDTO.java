package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * 公共浴室预约
 *
 * @author zcd
 * @date 18/7/12
 */
@Data
public class BathOrderRespDTO {
        /**
         * balance : 0     // 用户金额
         * bathOrderId : 0   预约Id, 购买编码不会有
         * createTime : 0  创建预约时间
         * expiredTime : 0  过期时间
         * location : string 位置
         * minPrepay : 0  最小预约金额
         * missedTimes : 0  失约次数
         * prepay : 0   预付金额
         * prepayAmount : 0
         * status : 0  订单状态
         * totalMissTimes : 0   总共可失约次数，只有预约才会返回
         * tradeOrderId : 0
         */

        private int balance;
        private Long bathOrderId;
        private Long createTime;
        private long expiredTime;
        private String location;
        private int minPrepay;
        private int missedTimes;
        private int prepay;
        private int prepayAmount;
        private int status;
        private int totalMissTimes;
        private Long tradeOrderId;

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }

        public Long getBathOrderId() {
            return bathOrderId;
        }

        public void setBathOrderId(Long bathOrderId) {
            this.bathOrderId = bathOrderId;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
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

        public int getMinPrepay() {
            return minPrepay;
        }

        public void setMinPrepay(int minPrepay) {
            this.minPrepay = minPrepay;
        }

        public int getMissedTimes() {
            return missedTimes;
        }

        public void setMissedTimes(int missedTimes) {
            this.missedTimes = missedTimes;
        }

        public int getPrepay() {
            return prepay;
        }

        public void setPrepay(int prepay) {
            this.prepay = prepay;
        }

        public int getPrepayAmount() {
            return prepayAmount;
        }

        public void setPrepayAmount(int prepayAmount) {
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

        public Long getTradeOrderId() {
            return tradeOrderId;
        }

        public void setTradeOrderId(Long tradeOrderId) {
            this.tradeOrderId = tradeOrderId;
        }
//    /**
//     * 预约订单id，购买编码不会生成
//     */
//    private Long bathOrderId;
//    /**
//     * 设备位置
//     */
//    private String location;
//    /**
//     * 过期时间
//     */
//    private long expiredTime;
//    /**
//     * 预付金额
//     */
//    private Double amount;
//    /**
//     * 用户余额
//     */
//    private Double balance;
//    /**
//     * 编码:购买编码方式会生成
//     */
//    private String code;
//    /**
//     * 一个随机红包
//     */
//    private BriefBonusDTO bonus;
//    /**
//     * 1 预约中（未支付） 2 预约成功  3 预约失败
//     */
//    private Integer status;
//    /**
//     * 失约次数 只有预约才会返回
//     */
//    private Integer missTimes;
//
//    /**
//     * 预约创建时间
//     */
//    private Long createTime ;



}
