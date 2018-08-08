package com.xiaolian.amigo.data.network.model.bathroom;


import lombok.Data;

/**
 * 主页显示是否有上一个订单
 */
@Data
public class CurrentBathOrderRespDTO {

    private boolean existOrder ;   // 是否存在上一订单

    private Integer status ;   // 转态  2 等待洗浴 5 预约超时 6正在洗浴
}
