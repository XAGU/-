package com.xiaolian.amigo.ui.main.update;

import java.io.Serializable;

public interface IVersionModel extends Serializable {
    int getVersionCode();

    String getVersionName();

    String getVersionDescription();

    String getDownloadUrl();

    boolean isMustUpdate();
}
