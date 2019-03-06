package com.xiaolian.amigo.data.network.model.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.xiaolian.amigo.data.vo.Bonus;

import java.util.List;

import lombok.Data;

/**
 * 首页设备用水校验
 *
 * @author zcd
 * @date 17/10/12
 */
@Data
public class DeviceCheckRespDTO implements Parcelable {
    /**
     * 默认宿舍设备的macAddress
     */
    private String defaultMacAddress;
    private Long defaultSupplierId;
    /**
     * 是否存在2小时内未找零的账单
     */
    private Boolean existsUnsettledOrder;
    private String extra;
    /**
     * 未找零设备的macAddress
     */
    private String unsettledMacAddress;
    private Long unsettledSupplierId;
    private String location;
    private String remark;
    private Boolean timeValid;
    private String startTime;
    private String title;
    private Long residenceId;
    /**
     * 最低费率 单位分
     */
    private Integer price;
    /**
     * 是否收藏
     */
    private Boolean favor;
    /**
     * 水温
     */
    private Integer usefor;
    /**
     * 饮水机类型 普通 三合一
     */
    private Integer category;
    /**
     * 代金券
     */
    private Bonus bonus;
    /**
     * 预付金额
     */
    private Double prepay;
    /**
     * 最小预付金额
     */
    private Double minPrepay;
    /**
     * 余额
     */
    private Double balance;
    /**
     * 客服电话
     */
    private String csMobile;
    /**
     * 供应商相关模型
     */
    private List<DeviceCategoryBO> devices;

    protected DeviceCheckRespDTO(Parcel in) {
        defaultMacAddress = in.readString();
        if (in.readByte() == 0) {
            defaultSupplierId = null;
        } else {
            defaultSupplierId = in.readLong();
        }
        byte tmpExistsUnsettledOrder = in.readByte();
        existsUnsettledOrder = tmpExistsUnsettledOrder == 0 ? null : tmpExistsUnsettledOrder == 1;
        extra = in.readString();
        unsettledMacAddress = in.readString();
        if (in.readByte() == 0) {
            unsettledSupplierId = null;
        } else {
            unsettledSupplierId = in.readLong();
        }
        location = in.readString();
        remark = in.readString();
        byte tmpTimeValid = in.readByte();
        timeValid = tmpTimeValid == 0 ? null : tmpTimeValid == 1;
        startTime = in.readString();
        title = in.readString();
        if (in.readByte() == 0) {
            residenceId = null;
        } else {
            residenceId = in.readLong();
        }
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readInt();
        }
        byte tmpFavor = in.readByte();
        favor = tmpFavor == 0 ? null : tmpFavor == 1;
        if (in.readByte() == 0) {
            usefor = null;
        } else {
            usefor = in.readInt();
        }
        if (in.readByte() == 0) {
            category = null;
        } else {
            category = in.readInt();
        }
        bonus = in.readParcelable(Bonus.class.getClassLoader());
        if (in.readByte() == 0) {
            prepay = null;
        } else {
            prepay = in.readDouble();
        }
        if (in.readByte() == 0) {
            minPrepay = null;
        } else {
            minPrepay = in.readDouble();
        }
        if (in.readByte() == 0) {
            balance = null;
        } else {
            balance = in.readDouble();
        }
        csMobile = in.readString();
        tradePages = in.createStringArrayList();
    }

    public static final Creator<DeviceCheckRespDTO> CREATOR = new Creator<DeviceCheckRespDTO>() {
        @Override
        public DeviceCheckRespDTO createFromParcel(Parcel in) {
            return new DeviceCheckRespDTO(in);
        }

        @Override
        public DeviceCheckRespDTO[] newArray(int size) {
            return new DeviceCheckRespDTO[size];
        }
    };

    /**
     * 如果favor为null，则默认返回false
     */
    public Boolean getFavor() {
        if (favor == null) {
            return false;
        }
        return favor;
    }

    private List<String> tradePages ; //  交易页面枚举 = ['BLE', 'GATEWAY_NETWORK', 'QR_CODE', 'NB'],

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(defaultMacAddress);
        if (defaultSupplierId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(defaultSupplierId);
        }
        dest.writeByte((byte) (existsUnsettledOrder == null ? 0 : existsUnsettledOrder ? 1 : 2));
        dest.writeString(extra);
        dest.writeString(unsettledMacAddress);
        if (unsettledSupplierId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(unsettledSupplierId);
        }
        dest.writeString(location);
        dest.writeString(remark);
        dest.writeByte((byte) (timeValid == null ? 0 : timeValid ? 1 : 2));
        dest.writeString(startTime);
        dest.writeString(title);
        if (residenceId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(residenceId);
        }
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(price);
        }
        dest.writeByte((byte) (favor == null ? 0 : favor ? 1 : 2));
        if (usefor == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(usefor);
        }
        if (category == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(category);
        }
        dest.writeParcelable(bonus, flags);
        if (prepay == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(prepay);
        }
        if (minPrepay == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(minPrepay);
        }
        if (balance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(balance);
        }
        dest.writeString(csMobile);
        dest.writeStringList(tradePages);
    }
}
