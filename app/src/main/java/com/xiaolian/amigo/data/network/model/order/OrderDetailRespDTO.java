package com.xiaolian.amigo.data.network.model.order;

import lombok.Data;

/**
 * 网络返回-订单详情
 *
 * @author caidong
 * @date 17/10/11
 */
@Data
public class OrderDetailRespDTO {
    private Long id;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 设备地址
     */
    private String location;
    /**
     * 找零
     */
    private String odd;
    /**
     * 实际消费
     */
    private String consume;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 预付金额
     */
    private String prepay;
    /**
     * 设备类型
     */
    private Integer deviceType;
    /**
     * status为3表示异常账单
     */
    private Integer status;
    /**
     * 代金券
     */
    private String bonus;
    /**
     * 实际扣款
     */
    private String actualDebit;
    /**
     * 是否是最低消费
     */
    private Boolean lowest;
    /**
     * 使用时长
     */
    private String useTime;

    /**
     * 设备工作模式
     */
    private Integer mode;
    /**
     * 工作模式描述
     */
    private String modeDesc;
    /**
     * 二维码信息
     */
    private String qrCode;

    private Boolean favor;
    private String macAddress;
    private Long residenceId;
    private Integer usefor;
    /**
     * 零元消费额文案
     */
    private String zeroConsumeCopy;
}
