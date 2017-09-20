package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 网络请求-收藏设备
 * <p>
 * Created by caidong on 2017/9/15.
 */
@Data
public class FavoriteReqDTO {

    // 页码
    private Integer page;
    // 页大小
    private Integer size;

}
