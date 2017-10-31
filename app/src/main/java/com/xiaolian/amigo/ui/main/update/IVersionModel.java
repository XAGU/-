package com.xiaolian.amigo.ui.main.update;

import java.io.Serializable;

/**
 * Created by adamzfc on 2017/3/29.
 */

public interface IVersionModel extends Serializable {
    int getVersionCode();

    String getVersionName();

    String getVersionDescription();

    String getDownloadUrl();

    boolean isMustUpdate();
}
