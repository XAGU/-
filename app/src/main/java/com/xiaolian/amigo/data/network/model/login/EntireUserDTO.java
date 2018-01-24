package com.xiaolian.amigo.data.network.model.login;

import com.xiaolian.amigo.data.vo.Mapper;
import com.xiaolian.amigo.data.vo.User;

import lombok.Data;

/**
 * UserDTO
 * @author zcd
 */
@Data
public class EntireUserDTO implements Mapper<User> {

    private String residenceName;
    private Long residenceId;
    private String macAddress;
    private String floor;
    private Long floorId;
    private Long id;
    private String mobile;
    private String nickName;
    private String pictureUrl;
    private String room;
    private Long roomId;
    private Long schoolId;
    private String schoolName;
    private Integer sex;
    private Integer type;

    @Override
    public User transform() {
        User user = new User(this);
        return user;
    }
}