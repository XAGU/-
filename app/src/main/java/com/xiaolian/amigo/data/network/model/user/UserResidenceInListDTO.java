package com.xiaolian.amigo.data.network.model.user;

import com.xiaolian.amigo.data.vo.Mapper;

import lombok.Data;

/**
 * 宿舍绑定
 * <p>
 * Created by zcd on 9/20/17.
 */
@Data
public class UserResidenceInListDTO implements Mapper<UserResidence> {
    private Long id;
    private Long residenceId;
    private String residenceName;
    private String macAddress;
    private Long supplierId;

    @Override
    public UserResidence transform() {
        UserResidence userResidence = new UserResidence();
        userResidence.setId(id);
        userResidence.setResidenceId(residenceId);
        userResidence.setResidenceName(residenceName);
        userResidence.setMacAddress(macAddress);
        userResidence.setSupplierId(supplierId);
        return userResidence;
    }
}
