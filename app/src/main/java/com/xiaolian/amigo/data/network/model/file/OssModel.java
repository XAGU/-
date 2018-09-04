package com.xiaolian.amigo.data.network.model.file;

import lombok.Data;

/**
 * oss模型
 *
 * @author zcd
 * @date 17/11/13
 */

public class OssModel {

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    /**
     * 访问域名
     */
    private String endpoint;
    /**
     * 表示Android/iOS应用初始化OSSClient获取的 AccessKeyId
     */
    private String accessKeyId;
    /**
     * 表示Android/iOS应用初始化OSSClient获取AccessKeySecret。
     */
    private String accessKeySecret;
    /**
     * 表示Android/iOS应用初始化的Token。
     */
    private String securityToken;
    /**
     * 表示该Token失效的时间。主要在Android SDK会自动判断是否失效，自动获取Token。
     */
    private Long expiration;
    private String bucket;
}
