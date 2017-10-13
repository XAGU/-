package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.Data;

/**
 * 首页设备用水校验
 * <p>
 * Created by zcd on 10/12/17.
 */
@Data
public class DeviceCheckRespDTO {
    // 是否存在2小时内未找零的账单
    private Boolean existsUnsettledOrder;
    private String macAddress;
    private String remark;
    private Boolean timeValid;
    private String title;
}
