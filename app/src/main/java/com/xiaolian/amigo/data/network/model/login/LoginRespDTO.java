package com.xiaolian.amigo.data.network.model.login;

import com.xiaolian.amigo.data.vo.User;

import lombok.Data;

/**
 * @author zcd
 */
@Data
public class LoginRespDTO {

    private String token;
    private EntireUserDTO user;
}
