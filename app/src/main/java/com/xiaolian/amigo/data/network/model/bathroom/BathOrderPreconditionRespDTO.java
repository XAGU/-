package com.xiaolian.amigo.data.network.model.bathroom;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class BathOrderPreconditionRespDTO implements Parcelable {

        /**
         * existUsingOrder : false
         * expiredTime : 0
         * id : 0
         * location : string
         * status : 0
         * type : 0
         */

        private boolean existUsingOrder;  // 是否存在历史订单
        private long expiredTime;   // 过期时间
        private long id;
        private String location;  // 浴室位置
        private int status;   // 订单状态
        private int type;    //  预约订单类型

    protected BathOrderPreconditionRespDTO(Parcel in) {
        existUsingOrder = in.readByte() != 0;
        expiredTime = in.readLong();
        id = in.readLong();
        location = in.readString();
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

    public boolean isExistUsingOrder() {
            return existUsingOrder;
        }

        public void setExistUsingOrder(boolean existUsingOrder) {
            this.existUsingOrder = existUsingOrder;
        }

        public long getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(long expiredTime) {
            this.expiredTime = expiredTime;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
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
        dest.writeByte((byte) (existUsingOrder ? 1 : 0));
        dest.writeLong(expiredTime);
        dest.writeLong(id);
        dest.writeString(location);
        dest.writeInt(status);
        dest.writeInt(type);
    }
}

