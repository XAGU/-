package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * 绑定洗澡地址
 */
@Data
public class RecordResidenceReqDTO {


    /**
     * bathType : 0
     * id : 0
     * residenceId : 0
     */

    private int bathType;    // 1 、宿舍  2 、公共浴室
    private Long id;     // 非必传 ， 不传为新增， 传为修改
    private long residenceId;   // 位置id

}
