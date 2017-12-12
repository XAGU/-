package com.xiaolian.amigo.data.network.model.bonus;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import lombok.Data;

/**
 * 代金券
 * @author zcd
 */
@Data
public class Bonus implements Parcelable {

    private Double amount;
    private String createTime;
    private Integer deviceType;
    private Long endTime;
    private Long id;
    private String name;
    private String remarks;
    private String description;
    private Long timeLimit;
    private String updateTime;
    private Integer useStatus;
    private Integer validStatus;
    // 生效时间
    private Long startTime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.amount);
        dest.writeString(this.createTime);
        dest.writeValue(this.deviceType);
        dest.writeValue(this.endTime);
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.remarks);
        dest.writeString(this.description);
        dest.writeValue(this.timeLimit);
        dest.writeString(this.updateTime);
        dest.writeValue(this.useStatus);
        dest.writeValue(this.validStatus);
        dest.writeValue(this.startTime);
    }

    protected Bonus(Parcel in) {
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.createTime = in.readString();
        this.deviceType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.endTime = (Long) in.readValue(Long.class.getClassLoader());
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.remarks = in.readString();
        this.description = in.readString();
        this.timeLimit = (Long) in.readValue(Long.class.getClassLoader());
        this.updateTime = in.readString();
        this.useStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.validStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.startTime = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<Bonus> CREATOR = new Parcelable.Creator<Bonus>() {
        @Override
        public Bonus createFromParcel(Parcel source) {
            return new Bonus(source);
        }

        @Override
        public Bonus[] newArray(int size) {
            return new Bonus[size];
        }
    };
}
