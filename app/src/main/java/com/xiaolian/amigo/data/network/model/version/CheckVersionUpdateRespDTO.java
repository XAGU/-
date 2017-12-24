package com.xiaolian.amigo.data.network.model.version;

import com.xiaolian.amigo.data.network.model.version.VersionDTO;

import lombok.Data;

/**
 * 检查更新dto
 * <p>
 * Created by zcd on 17/10/31.
 */
@Data
public class CheckVersionUpdateRespDTO {
    private Boolean result;
    private VersionDTO version;
}
