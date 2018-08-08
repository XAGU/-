package com.xiaolian.amigo.data.network.model.bathroom;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * 公共浴室预约
 *
 * @author zcd
 * @date 18/7/12
 */
@Data
public class BathOrderRespDTO implements Parcelable {

        /**
         * bathBookingId : 0
         * bathOrderId : 0
         * createTime : 0
         * deviceNo : 0
         * expiredTime : 0
         * location : string
         * missedTimes : 0
         * prepayInfo : {"balance":0,"minPrepay":0,"prepay":0}
         * status : 0
         * totalMissTimes : 0
         */

        private long bathBookingId;
        private long bathOrderId;
        private long createTime;
        private long deviceNo;
        private long expiredTime;
        private String location;
        private int missedTimes;
        private PrepayInfoBean prepayInfo;
        private int status;
        private int totalMissTimes;

    protected BathOrderRespDTO(Parcel in) {
        bathBookingId = in.readLong();
        bathOrderId = in.readLong();
        createTime = in.readLong();
        deviceNo = in.readLong();
        expiredTime = in.readLong();
        location = in.readString();
        missedTimes = in.readInt();
        prepayInfo =in.readParcelable(PrepayInfoBean.class.getClassLoader());
        status = in.readInt();
        totalMissTimes = in.readInt();
    }

    public static final Creator<BathOrderRespDTO> CREATOR = new Creator<BathOrderRespDTO>() {
        @Override
        public BathOrderRespDTO createFromParcel(Parcel in) {
            return new BathOrderRespDTO(in);
        }

        @Override
        public BathOrderRespDTO[] newArray(int size) {
            return new BathOrderRespDTO[size];
        }
    };

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
        dest.writeLong(expiredTime);
        dest.writeString(location);
        dest.writeInt(missedTimes);
        dest.writeParcelable(prepayInfo ,flags);
        dest.writeInt(status);
        dest.writeInt(totalMissTimes);
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
