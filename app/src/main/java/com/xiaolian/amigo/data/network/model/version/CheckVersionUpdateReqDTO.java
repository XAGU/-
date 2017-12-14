package com.xiaolian.amigo.data.network.model.version;

import lombok.Data;

/**
 * 检查更新DTO
 * <p>
 * Created by zcd on 17/10/31.
 */
@Data
public class CheckVersionUpdateReqDTO {
    private Integer code;
    private String versionNo;
    private Integer systemType = 2;
}
