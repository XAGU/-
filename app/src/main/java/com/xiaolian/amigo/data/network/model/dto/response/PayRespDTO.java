package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Created by caidong on 2017/10/9.
 */
@Data
public class PayRespDTO {

    // 订单编号
    private Long orderId;

    // 开阀指令
    private String openValveCommand;
}
