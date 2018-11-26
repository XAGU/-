package com.xiaolian.amigo.data.network.model.login;

import lombok.Data;

/**
 * @author zcd
 * @date 17/12/14
 */
@Data
public class LoginRespDTO {

    private String accessToken;
    private String refreshToken ;
    private EntireUserDTO user;
}
