package com.xiaolian.amigo.data.vo;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * 代金券
 *
 * @author zcd
 * @date 17/9/18
 */
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

    public Bonus() {
    }

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public static Creator<Bonus> getCREATOR() {
        return CREATOR;
    }
}
