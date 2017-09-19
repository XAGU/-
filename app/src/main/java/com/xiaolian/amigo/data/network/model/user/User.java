package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 9/19/17.
 */
@Data
public class User {
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
