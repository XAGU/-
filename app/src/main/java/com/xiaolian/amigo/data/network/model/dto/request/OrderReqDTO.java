package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 网络请求-订单
 * <p>
 * Created by caidong on 2017/9/15.
 */
@Data
public class OrderReqDTO {

    // 订单状态 1 - 使用中， 2 - 已结束
    private int orderStatus;
    // 页码
    private int page;
    // 页大小
    private int size;

}
