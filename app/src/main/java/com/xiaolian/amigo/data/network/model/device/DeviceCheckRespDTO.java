package com.xiaolian.amigo.data.network.model.device;

import com.xiaolian.amigo.data.vo.Bonus;

import java.util.List;

import lombok.Data;

/**
 * 首页设备用水校验
 * <p>
 * Created by zcd on 10/12/17.
 */
@Data
public class DeviceCheckRespDTO {
    // 默认宿舍设备的macAddress
    private String defaultMacAddress;
    private Long defaultSupplierId;
    // 是否存在2小时内未找零的账单
    private Boolean existsUnsettledOrder;
    private String extra;
    // 未找零设备的macAddress
    private String unsettledMacAddress;
    private Long unsettledSupplierId;
    private String location;
    private String remark;
    private Boolean timeValid;
    private String startTime;
    private String title;
    private Long residenceId;
    // 最低费率 单位分
    private Integer price;
    // 是否收藏
    private Boolean favor;
    // 水温
    private Integer usefor;
    // 代金券
    private Bonus bonus;
    // 预付金额
    private Double prepay;
    // 最小预付金额
    private Double minPrepay;
    // 余额
    private Double balance;
    // 客服电话
    private String csMobile;
    // 供应商相关模型
    private List<DeviceCategoryBO> devices;
}
