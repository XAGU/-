package com.xiaolian.amigo.data.base;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.xiaolian.amigo.data.network.model.file.OssModel;

/**
 * @author zcd
 * @date 17/11/13
 */

public class OssClientHolder {

    private OssClientHolder() {
    }

    public static OSSClient getClient(Context context, OssModel model) {
        ClientConfiguration conf;
        conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // connction time out default 15s
        conf.setSocketTimeout(15 * 1000); // socket timeout，default 15s
        conf.setMaxConcurrentRequest(5); // synchronous request number，default 5
        conf.setMaxErrorRetry(2); // retry，default 2
        OSSLog.enableLog(); //write local log file ,path is SDCard_path\OSSLog\logs.csv
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(model.getAccessKeyId(),
                model.getAccessKeySecret(), model.getSecurityToken());
        return new OSSClient(context.getApplicationContext(), model.getEndpoint(), credentialProvider, conf);
    }
}
