package com.xiaolian.amigo.data.network.model.dto.response;

import java.util.List;

import lombok.Data;

/**
 * 用户头像
 * <p>
 * Created by zcd on 9/27/17.
 */
@Data
public class QueryAvatarDTO {
    List<String> avatars;
}
