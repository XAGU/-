package com.xiaolian.amigo.data.network.model.bathroom;

import android.os.Parcel;
import android.os.Parcelable;

public class BathRouteRespDTO implements Parcelable {
        /**
         * buildingId : 0
         * existHistory : false
         * isPubBath : false
         * macAddress : string
         * residenceId : 0
         * residenceType : 0
         */

        private long buildingId;   // 楼栋id
        private boolean existHistory;   //  是否存有历史记录
        private boolean isPubBath;   //  是否是公共浴室
        private String macAddress;  // 设备地址
        private long residenceId;   // 建筑id
        private int residenceType;  //位置类别：1、楼栋 2、楼层 3、宿舍房间/设备位置 4、公共浴室组
        private long  supplierId ;  // 设备商id
        private String residenceName ;

    protected BathRouteRespDTO(Parcel in) {
        buildingId = in.readLong();
        existHistory = in.readByte() != 0;
        isPubBath = in.readByte() != 0;
        macAddress = in.readString();
        residenceId = in.readLong();
        residenceType = in.readInt();
        supplierId = in.readLong();
        residenceName = in.readString();
    }

    public static final Creator<BathRouteRespDTO> CREATOR = new Creator<BathRouteRespDTO>() {
        @Override
        public BathRouteRespDTO createFromParcel(Parcel in) {
            return new BathRouteRespDTO(in);
        }

        @Override
        public BathRouteRespDTO[] newArray(int size) {
            return new BathRouteRespDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(buildingId);
        dest.writeByte((byte) (existHistory ? 1 : 0));
        dest.writeByte((byte) (isPubBath ? 1 : 0));
        dest.writeString(macAddress);
        dest.writeLong(residenceId);
        dest.writeInt(residenceType);
        dest.writeLong(supplierId);
        dest.writeString(residenceName);
    }


    public String getResidenceName() {
        return residenceName;
    }

    public void setResidenceName(String residenceName) {
        this.residenceName = residenceName;
    }

    public boolean isPubBath() {
        return isPubBath;
    }

    public void setPubBath(boolean pubBath) {
        isPubBath = pubBath;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(long buildingId) {
            this.buildingId = buildingId;
        }

        public boolean isExistHistory() {
            return existHistory;
        }

        public void setExistHistory(boolean existHistory) {
            this.existHistory = existHistory;
        }

        public boolean isIsPubBath() {
            return isPubBath;
        }

        public void setIsPubBath(boolean isPubBath) {
            this.isPubBath = isPubBath;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public long getResidenceId() {
            return residenceId;
        }

        public void setResidenceId(long residenceId) {
            this.residenceId = residenceId;
        }

        public int getResidenceType() {
            return residenceType;
        }

        public void setResidenceType(int residenceType) {
            this.residenceType = residenceType;
        }
}
