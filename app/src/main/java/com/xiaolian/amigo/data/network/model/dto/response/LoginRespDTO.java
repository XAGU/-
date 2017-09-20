package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.user.User;

import lombok.Data;

/**
 * @author zcd
 */
@Data
public class LoginRespDTO {

    private String token;
    private User user;
}
