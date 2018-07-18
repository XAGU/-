package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * 公共浴室支付请求
 *
 * @author zcd
 * @date 18/7/18
 */
@Data
public class BathOrderReqDTO {
  /**
   * 用户实际扣除金额
   */
  private Double prepayAmount;
  /**
   * 1 预约 2 购买劵
   *
   * @see com.xiaolian.amigo.data.enumeration.BathTradeType
   */
  private Integer type;
  /**
   * 优惠劵
   */
  private Long userBonusId;

}
