package com.xiaolian.amigo.data.network.model.device;

import java.util.List;

import lombok.Data;

/**
 * 设备详情
 */
@Data
public class BriefDeviceDTO {

    /**
     * deviceType : 0
     * id : 0
     * location : string
     * schoolId : 0
     * schoolName : string
     * supplierId : 0
     */

    private int deviceType;
    private long id;
    private String location;
    private long schoolId;
    private String schoolName;
    private long supplierId; // 设备供应商
    private long residenceId;
    private boolean favor;
    private String tradePage; //  交易页面枚举 = ['BLE', 'GATEWAY_NETWORK', 'QR_CODE', 'NB'],

    // 是否是当前供水时段
    private Boolean timeValid;

    // 标题
    private String title;

    // 提示内容
    private String remark;

    /**
     * 饮水机类型 普通 三合一
     */
    private Integer category;

    /**
     * 水温
     */
    private int usefor;


    /**
     * 下单前文案
     */
    private List<String> afterOrderCopy ;

    /**
     * 下单后文案
     */
    private List<String> preOrderCopy ;

}
