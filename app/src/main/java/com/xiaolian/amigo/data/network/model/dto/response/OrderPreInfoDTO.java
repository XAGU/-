package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.bonus.Bonus;

import java.util.List;

import lombok.Data;

/**
 * 订单预备信息
 * <p>
 * Created by zcd on 10/13/17.
 */
@Data
public class OrderPreInfoDTO {
    /**
     * 红包
     */
    Bonus bonus;
    /**
     * 预付金额
     */
    Double prepay;
    /**
     * 最小预付金额
     */
    Double minPrepay;
    /**
     * 余额
     */
    Double balance;
    /**
     * 客服电话
     */
    String csMobile;

}
