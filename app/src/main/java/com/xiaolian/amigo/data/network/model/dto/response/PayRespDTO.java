package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Created by caidong on 2017/10/9.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayRespDTO {

    // 订单编号
    @NonNull
    private Long orderId;

    // 开阀指令
    @NonNull
    private String openValveCommand;
}
