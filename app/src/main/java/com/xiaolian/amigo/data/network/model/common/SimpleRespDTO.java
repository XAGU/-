package com.xiaolian.amigo.data.network.model.common;

import lombok.Data;

/**
 * SimpleRespDTO
 *
 * @author zcd
 * @date 17/9/15
 */
@Data
public class SimpleRespDTO {
    private Long id;

    //密码保护
    private Integer protectInMinutes;
    private Integer remaining;
    private Boolean result;
    //密码保护end
}
