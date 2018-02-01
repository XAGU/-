package com.xiaolian.amigo.data.network.model.version;

import lombok.Data;

/**
 * 检查更新dto
 *
 * @author zcd
 * @date 17/10/31
 */
@Data
public class CheckVersionUpdateRespDTO {
    private Boolean result;
    private VersionDTO version;
}
