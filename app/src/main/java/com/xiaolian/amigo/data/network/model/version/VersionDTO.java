package com.xiaolian.amigo.data.network.model.version;

import com.xiaolian.amigo.ui.main.update.IVersionModel;

import lombok.Data;

/**
 * 版本更新DTO
 *
 * @author zcd
 * @date 17/10/31
 */
@Data
public class VersionDTO implements IVersionModel {
    private String content;
    private Long id;
    private Integer type;
    private Long updateTime;
    private String url;
    private String versionNo;

    @Override
    public int getVersionCode() {
        return 0;
    }

    @Override
    public String getVersionName() {
        return versionNo;
    }

    @Override
    public String getVersionDescription() {
        return content;
    }

    @Override
    public String getDownloadUrl() {
        return url;
    }

    @Override
    public boolean isMustUpdate() {
        return type == 1;
    }
}
