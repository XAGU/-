package com.xiaolian.amigo.data.network.model.bathroom;


import android.os.Parcel;
import android.os.Parcelable;

public class BathOrderPreconditionRespDTO implements Parcelable {
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
        private long createTime;
        private long deviceNo;
        private boolean existUsingOrder;
        private long expiredTime;
        private String location;
        private int maxMissAbleTimes;
        private int missedTimes;
        private int prepayAmount;
        private PrepayInfoBean prepayInfo;
        private int status;
        private int type;

    protected BathOrderPreconditionRespDTO(Parcel in) {
        bathBookingId = in.readLong();
        bathOrderId = in.readLong();
        createTime = in.readLong();
        deviceNo = in.readLong();
        existUsingOrder = in.readByte() != 0;
        expiredTime = in.readLong();
        location = in.readString();
        maxMissAbleTimes = in.readInt();
        missedTimes = in.readInt();
        prepayAmount = in.readInt();
        prepayInfo = in.readParcelable(PrepayInfoBean.class.getClassLoader());
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

    public long getBathBookingId() {
            return bathBookingId;
        }

        public void setBathBookingId(long bathBookingId) {
            this.bathBookingId = bathBookingId;
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

        public long getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(long deviceNo) {
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

        public void setExpiredTime(long expiredTime) {
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

        public int getPrepayAmount() {
            return prepayAmount;
        }

        public void setPrepayAmount(int prepayAmount) {
            this.prepayAmount = prepayAmount;
        }

        public PrepayInfoBean getPrepayInfo() {
            return prepayInfo;
        }

        public void setPrepayInfo(PrepayInfoBean prepayInfo) {
            this.prepayInfo = prepayInfo;
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
        dest.writeLong(bathBookingId);
        dest.writeLong(bathOrderId);
        dest.writeLong(createTime);
        dest.writeLong(deviceNo);
        dest.writeByte((byte) (existUsingOrder ? 1 : 0));
        dest.writeLong(expiredTime);
        dest.writeString(location);
        dest.writeInt(maxMissAbleTimes);
        dest.writeInt(missedTimes);
        dest.writeInt(prepayAmount);
        dest.writeParcelable(prepayInfo ,flags);
        dest.writeInt(status);
        dest.writeInt(type);

    }

    public static class PrepayInfoBean implements Parcelable {
            /**
             * balance : 0
             * minPrepay : 0
             * prepay : 0
             */

            private double balance;
            private double minPrepay;
            private double prepay;

        protected PrepayInfoBean(Parcel in) {
            balance = in.readDouble();
            minPrepay = in.readDouble();
            prepay = in.readDouble();
        }

        public static final Creator<PrepayInfoBean> CREATOR = new Creator<PrepayInfoBean>() {
            @Override
            public PrepayInfoBean createFromParcel(Parcel in) {
                return new PrepayInfoBean(in);
            }

            @Override
            public PrepayInfoBean[] newArray(int size) {
                return new PrepayInfoBean[size];
            }
        };

        public double getBalance() {
                return balance;
            }

            public void setBalance(double balance) {
                this.balance = balance;
            }

            public double getMinPrepay() {
                return minPrepay;
            }

            public void setMinPrepay(double minPrepay) {
                this.minPrepay = minPrepay;
            }

            public double getPrepay() {
                return prepay;
            }

            public void setPrepay(double prepay) {
                this.prepay = prepay;
            }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(balance);
            dest.writeDouble(minPrepay);
            dest.writeDouble(prepay);
        }
    }
    }

