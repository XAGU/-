package com.xiaolian.amigo.data.network.model.version;

import java.util.Map;

/**
 * @author  wcm
 * 版本更新弹窗时间
 */
public class VersionDialogTime {

    /**
     * key  mobile
     * value  dialog show   time
     */
    private Map<String ,Long> versionUpdateTime ;

    public Map<String, Long> getVersionDialogTime() {
        return versionUpdateTime;
    }

    public void setVersionDialogTime(Map<String, Long> versionDialogTime) {
        this.versionUpdateTime = versionDialogTime;
    }
}
