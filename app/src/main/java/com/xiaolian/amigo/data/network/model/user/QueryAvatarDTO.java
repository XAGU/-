package com.xiaolian.amigo.data.network.model.user;

import java.util.List;

import lombok.Data;

/**
 * 用户头像
 *
 * @author zcd
 * @date 17/9/27
 */
@Data
public class QueryAvatarDTO {
    List<String> avatars;
}
