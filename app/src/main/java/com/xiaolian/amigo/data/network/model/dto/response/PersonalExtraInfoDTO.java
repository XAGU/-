package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.notify.Notify;

import java.util.List;

import lombok.Data;

/**
 * 用户个人中心额外信息
 * <p>
 * Created by zcd on 9/22/17.
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
    private boolean isNeedShowDot = false;
}
