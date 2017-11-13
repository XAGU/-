package com.xiaolian.amigo.data.network.model.file;

import lombok.Data;

/**
 * oss模型
 * <p>
 * Created by zcd on 17/11/13.
 */
@Data
public class OssModel {
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
