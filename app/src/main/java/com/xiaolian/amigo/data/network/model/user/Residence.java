package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

/**
 * 建筑物模型
 *
 * @author zcd
 * @date 17/9/19
 */
@Data
public class Residence {
    private Long id;
    private String name;
    private String fullName;
    private String macAddress;
}
