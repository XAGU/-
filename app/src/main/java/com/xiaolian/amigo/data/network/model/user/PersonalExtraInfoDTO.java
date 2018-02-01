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
    private Double balance;
    private List<BannerDTO> banners;
    private Integer bonusAmount;
    private Integer notifyAmount;
    private Double prepay;
    private Long repair;
    private Long lastRepairTime;
    private Notify urgentNotify;
    private String preFileUrl;
    private Boolean showTransfer;
    private boolean isNeedShowDot = false;
}
