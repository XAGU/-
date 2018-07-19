package com.xiaolian.amigo.data.network.model.system;

import java.util.ArrayList;

import lombok.Data;

/**
 * 基础信息
 *
 * @author zcd
 * @date 17/11/24
 */
@Data
public class BaseInfoDTO {
    private ArrayList<BannerDTO> banners;
    private ArrayList<String> bathPasswordDescription;   //公共浴室密码说明
}
