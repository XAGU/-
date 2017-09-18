package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.Data;

/**
 * UserDTO
 * @author zcd
 */
@Data
public class EntireUserDTO {

    private String residenceName;
    private int residenceId;
    private String floor;
    private int floorId;
    private int id;
    private String mobile;
    private String nickName;
    private String pictureUrl;
    private String room;
    private int roomId;
    private int schoolId;
    private String schoolName;
    private int sex;
    private int type;

}
