package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.Data;

/**
 * UserDTO
 * @author zcd
 */
@Data
public class EntireUserDTO {

    /**
     * building : string
     * buildingId : 0
     * floor : string
     * floorId : 0
     * id : 0
     * mobile : 0
     * nickName : string
     * pictureUrl : string
     * room : string
     * roomId : 0
     * schoolId : 0
     * schoolName : string
     * sex : 0
     * type : 0
     */

    private String building;
    private int buildingId;
    private String floor;
    private int floorId;
    private int id;
    private int mobile;
    private String nickName;
    private String pictureUrl;
    private String room;
    private int roomId;
    private int schoolId;
    private String schoolName;
    private int sex;
    private int type;

}
