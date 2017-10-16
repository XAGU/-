package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Created by caidong on 2017/10/15.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LatestOrderReqDTO {

    // 设备mac地址
    @NonNull
    private String macAddress;


}
