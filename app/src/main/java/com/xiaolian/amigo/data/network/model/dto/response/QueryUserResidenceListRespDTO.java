package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.user.UserResidence;

import java.util.List;

import lombok.Data;

/**
 * 寝室列表
 * <p>
 * Created by zcd on 9/19/17.
 */
@Data
public class QueryUserResidenceListRespDTO {
    private int total;
    private List<UserResidence> userResidences;
}
