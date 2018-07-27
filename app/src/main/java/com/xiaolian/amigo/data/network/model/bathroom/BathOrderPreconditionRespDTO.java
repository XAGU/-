package com.xiaolian.amigo.data.network.model.bathroom;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class BathOrderPreconditionRespDTO implements Parcelable {

    /**
     * bathOrderId : 0
     * createTime : 0
     * deviceNo : 0
     * existUsingOrder : false
     * expiredTime : 0
     * location : string
     * maxMissAbleTimes : 0
     * missedTimes : 0
     * status : 0
     * type : 0
     */

    private long bathOrderId;   //id
    private long createTime;   //  创建时间
    private long deviceNo;    // deviceNO
    private boolean existUsingOrder;
    private long expiredTime;   //  过期时间
    private String location;
    private int maxMissAbleTimes;   // 总共预约次数
    private int missedTimes;   // 失约次数
    private int status;
    private int type;  //  预约类型

    protected BathOrderPreconditionRespDTO(Parcel in) {
        bathOrderId = in.readLong();
        createTime = in.readLong();
        deviceNo = in.readLong();
        existUsingOrder = in.readByte() != 0;
        expiredTime = in.readLong();
        location = in.readString();
        maxMissAbleTimes = in.readInt();
        missedTimes = in.readInt();
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

    public long getBathOrderId() {
        return bathOrderId;
    }

    public void setBathOrderId(int bathOrderId) {
        this.bathOrderId = bathOrderId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public long getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(int deviceNo) {
        this.deviceNo = deviceNo;
    }

    public boolean isExistUsingOrder() {
        return existUsingOrder;
    }

    public void setExistUsingOrder(boolean existUsingOrder) {
        this.existUsingOrder = existUsingOrder;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
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

    public int getMissedTimes() {
        return missedTimes;
    }

    public void setMissedTimes(int missedTimes) {
        this.missedTimes = missedTimes;
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
        dest.writeLong(bathOrderId);
        dest.writeLong(createTime);
        dest.writeLong(deviceNo);
        dest.writeByte((byte) (existUsingOrder ? 1 : 0));
        dest.writeLong(expiredTime);
        dest.writeString(location);
        dest.writeInt(maxMissAbleTimes);
        dest.writeInt(missedTimes);
        dest.writeInt(status);
        dest.writeInt(type);
    }
}

