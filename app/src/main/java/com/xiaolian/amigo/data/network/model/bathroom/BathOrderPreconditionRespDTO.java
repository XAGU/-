package com.xiaolian.amigo.data.network.model.bathroom;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class BathOrderPreconditionRespDTO implements Parcelable {

        /**
         * balance : 0
         * bathOrderId : 0
         * createTime : 0
         * deviceNo : 0
         * existUsingOrder : false
         * expiredTime : 0
         * location : string
         * maxMissAbleTimes : 0
         * minPrepay : 0
         * missedTimes : 0
         * prepay : 0
         * prepayAmount : 0
         * status : 0
         * type : 0
         */

        private Double balance;   // 用户余额
        private Long bathOrderId;    // 订单id
        private Long createTime;   // 创建时间
        private Long deviceNo;   //  设备号
        private boolean existUsingOrder;  // 存在上一次订单
        private Long expiredTime;  // 过期时间
        private String location;   // 位置
        private int maxMissAbleTimes;   // 最多失约次数
        private Double minPrepay;   // 最小预付金额
        private int missedTimes;   //  失约次数
        private Double prepay;   // 预付金额
        private Double prepayAmount;   // 用户实际预付金额
        private int status;   //  1-取消预约/购买洗澡卷 2-已支付 3-预约成功 4-预约失败 5-已经过期 6-使用中 7-已完成 ,
        private int type;    //  预约订单类型:1-预约购买，2-洗澡劵

    protected BathOrderPreconditionRespDTO(Parcel in) {
        if (in.readByte() == 0) {
            balance = null;
        } else {
            balance = in.readDouble();
        }
        if (in.readByte() == 0) {
            bathOrderId = null;
        } else {
            bathOrderId = in.readLong();
        }
        if (in.readByte() == 0) {
            createTime = null;
        } else {
            createTime = in.readLong();
        }
        if (in.readByte() == 0) {
            deviceNo = null;
        } else {
            deviceNo = in.readLong();
        }
        existUsingOrder = in.readByte() != 0;
        if (in.readByte() == 0) {
            expiredTime = null;
        } else {
            expiredTime = in.readLong();
        }
        location = in.readString();
        maxMissAbleTimes = in.readInt();
        if (in.readByte() == 0) {
            minPrepay = null;
        } else {
            minPrepay = in.readDouble();
        }
        missedTimes = in.readInt();
        prepay = in.readDouble();
        if (in.readByte() == 0) {
            prepayAmount = null;
        } else {
            prepayAmount = in.readDouble();
        }
        status = in.readInt();
        type = in.readInt();
    }

    public static final Creator<BathOrderPreconditionRespDTO> CREATOR = new Creator<BathOrderPreconditionRespDTO>() {
        @Override
        public BathOrderPreconditionRespDTO createFromParcel(Parcel in) {
            return new BathOrderPreconditionRespDTO(in);
        }

        @Override
        public BathOrderPreconditionRespDTO[] newArray(int size) {
            return new BathOrderPreconditionRespDTO[size];
        }
    };

    public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
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

        public Long getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(Long deviceNo) {
            this.deviceNo = deviceNo;
        }

        public boolean isExistUsingOrder() {
            return existUsingOrder;
        }

        public void setExistUsingOrder(boolean existUsingOrder) {
            this.existUsingOrder = existUsingOrder;
        }

        public Long getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(Long expiredTime) {
            this.expiredTime = expiredTime;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getMaxMissAbleTimes() {
            return maxMissAbleTimes;
        }

        public void setMaxMissAbleTimes(int maxMissAbleTimes) {
            this.maxMissAbleTimes = maxMissAbleTimes;
        }

        public Double getMinPrepay() {
            return minPrepay;
        }

        public void setMinPrepay(Double minPrepay) {
            this.minPrepay = minPrepay;
        }

        public int getMissedTimes() {
            return missedTimes;
        }

        public void setMissedTimes(int missedTimes) {
            this.missedTimes = missedTimes;
        }

        public Double getPrepay() {
            return prepay;
        }

        public void setPrepay(Double prepay) {
            this.prepay = prepay;
        }

        public Double getPrepayAmount() {
            return prepayAmount;
        }

        public void setPrepayAmount(Double prepayAmount) {
            this.prepayAmount = prepayAmount;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (balance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(balance);
        }
        if (bathOrderId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(bathOrderId);
        }
        if (createTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(createTime);
        }
        if (deviceNo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(deviceNo);
        }
        dest.writeByte((byte) (existUsingOrder ? 1 : 0));
        if (expiredTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(expiredTime);
        }
        dest.writeString(location);
        dest.writeInt(maxMissAbleTimes);
        if (minPrepay == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(minPrepay);
        }
        dest.writeInt(missedTimes);
        dest.writeDouble(prepay);
        if (prepayAmount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(prepayAmount);
        }
        dest.writeInt(status);
        dest.writeInt(type);
    }
}

