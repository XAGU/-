package com.xiaolian.amigo.data.base;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.xiaolian.amigo.data.network.model.file.OssModel;

/**
 * <p>
 * Created by zcd on 17/11/13.
 */

public class OssClientHolder {
    private volatile static OssClientHolder holder;
    private OssModel ossModel;
    private OSSClient client;

    public interface ExpirationCallback {
        OSSFederationToken callback();
    }

    private OssClientHolder(OssModel ossModel, OSSClient client) {
        this.ossModel = ossModel;
        this.client = client;
    }

    public static OssClientHolder get() {
        return holder;
    }

    public static OssClientHolder get(Context context, OssModel model, ExpirationCallback callback) {
        if (null == holder) {
            synchronized (OssClientHolder.class) {
                if (null == holder) {
                    holder = createOssClient(context, model, callback);
                }
            }
        } else {
            if (!TextUtils.equals(holder.getEndpoint(), model.getEndpoint())) {
                holder = null;
                synchronized (OssClientHolder.class) {
                    holder = createOssClient(context, model, callback);
                }
            }
        }
        return holder;
    }

    private static OssClientHolder createOssClient(Context context, OssModel model,
                                                   ExpirationCallback callback) {
        ClientConfiguration conf;
        conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // connction time out default 15s
        conf.setSocketTimeout(15 * 1000); // socket timeout，default 15s
        conf.setMaxConcurrentRequest(5); // synchronous request number，default 5
        conf.setMaxErrorRetry(2); // retry，default 2
        OSSLog.enableLog(); //write local log file ,path is SDCard_path\OSSLog\logs.csv
        OSSCredentialProvider credentialProvider = new OSSFederationCredentialProvider() {
            @Override
            public OSSFederationToken getFederationToken() {
                return callback.callback();
            }
        };
        return new OssClientHolder(model, new OSSClient(context.getApplicationContext(), model.getEndpoint(), credentialProvider, conf));
    }


    public static OssClientHolder getHolder() {
        return holder;
    }

    public static void setHolder(OssClientHolder holder) {
        OssClientHolder.holder = holder;
    }

    private String getEndpoint() {
        return ossModel.getEndpoint();
    }

    public OSSClient getClient() {
        return client;
    }

    public OssModel getOssModel() {
        return ossModel;
    }

    public void setOssModel(OssModel ossModel) {
        this.ossModel = ossModel;
    }
}
