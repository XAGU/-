package com.xiaolian.amigo.data.network.model.user;

import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.xiaolian.amigo.data.vo.Notify;

import java.util.List;

import lombok.Data;

/**
 * 用户个人中心额外信息
 *
 * @author zcd
 * @date 17/9/22
 */
@Data
public class PersonalExtraInfoDTO {
    private Double allBalance;
    /**
     * 充值的余额 可提现
     */
    private Double chargeBalance;
    /**
     * 赠送的余额
     */
    private Double givingBalance;
    /**
     * 增送规则
     */
    private String givingRule;
    /**
     * 使用期限
     */
    private String useLimit;
    /**
     * 是否存在赠送金额
     */
    private Boolean existGiving;
    private List<BannerDTO> banners;
    private Integer bonusAmount;
    private Integer notifyAmount;
    private Double prepay;
    private Long repair;
    private Long lastRepairTime;
    private Notify urgentNotify;
    private String preFileUrl;
    /**
     * 是否需要账户迁移
     */
    private Boolean showTransfer;
    /**
     * 是否需要显示红点
     */
    private boolean isNeedShowDot = false;
    /**
     * 积分
     */
    private Integer credits;
    /**
     * 是否显示提现入口
     */
    private Boolean showWithdrawNew;

    private Integer studentAuth ;

}
