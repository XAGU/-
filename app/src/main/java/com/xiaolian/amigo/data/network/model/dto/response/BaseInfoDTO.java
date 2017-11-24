package com.xiaolian.amigo.data.network.model.dto.response;

import java.util.ArrayList;

import lombok.Data;

/**
 * 基础信息
 * <p>
 * Created by zcd on 17/11/24.
 */
@Data
public class BaseInfoDTO {
    private ArrayList<BannerDTO> banners;
}
