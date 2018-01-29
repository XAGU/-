package com.xiaolian.amigo.data.network.model.trade;

import java.util.List;

import lombok.Data;

/**
 * 请求洗衣机模式
 * <p>
 * Created by zcd on 18/1/17.
 */
@Data
public class WashingModeRespDTO {
    private List<Mode> modes;
}
