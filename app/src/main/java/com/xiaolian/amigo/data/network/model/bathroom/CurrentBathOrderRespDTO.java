package com.xiaolian.amigo.data.network.model.bathroom;


import lombok.Data;

/**
 * 主页显示是否有上一个订单
 */
@Data
public class CurrentBathOrderRespDTO {


    private Integer status ;   //1 空闲 2 正在排队 3 等待洗浴 4 正在洗浴

}
