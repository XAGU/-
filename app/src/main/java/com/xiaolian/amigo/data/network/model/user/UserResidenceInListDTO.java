package com.xiaolian.amigo.data.network.model.user;

import com.xiaolian.amigo.data.network.model.device.Supplier;

import lombok.Data;

/**
 * 宿舍绑定
 *
 * @author zcd
 * @date 17/9/20
 */

@Data
public class UserResidenceInListDTO {

    /**
     * buildingId : 0
     * id : 0
     * isPubBath : false
     * macAddress : string
     * residenceId : 0
     * residenceName : string
     * residenceType : 0
     * supplierId : 0
     */

    private long buildingId;
    private long id;
    private boolean isPubBath;
    private String macAddress;
    private long residenceId;
    private String residenceName;
    private long residenceType;
    private long supplierId;

    private Supplier supplier ;

    // 是否是当前供水时段
    private Boolean timeValid;

    // 标题
    private String title;

    // 提示内容
    private String remark;
}
