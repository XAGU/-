package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

/**
 * 绑定洗澡地址DTO
 *
 * @author zcd
 * @date 17/9/19
 */
@Data
public class BindResidenceReq {
    private Long id;
    private Long residenceId;  //  楼栋id
//    private int type  ;   //   1、宿舍 2、公共浴室
}
