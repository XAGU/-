package com.xiaolian.amigo.data.network.model.order;

/**
 * @author zcd
 * @date 18/6/5
 */
public class QueryPersonalOrderListReqDTO {
    /**
     * 1-使用中
     * 2-已结束
     * 3-异常订单
     * 4-手动退单的订单
     * 5-自动退单的订单
     * 6-结账退单， 与自动退单的区别在于结账退单未使用，用于洗衣机二维码扫描场景
     * 订单状态
     * 值为2时，返回2，4，5，6状态的订单
     * 值非2时，返回对应状态的数据
     */
    private Integer orderStatus;
    /**
     * 设备类型
     */
    private Integer deviceType;

    private Integer year;
    private Integer month;
    private Integer page;
    private Integer size;
}
