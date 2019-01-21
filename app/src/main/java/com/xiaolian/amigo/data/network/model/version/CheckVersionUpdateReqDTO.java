package com.xiaolian.amigo.data.network.model.version;

import lombok.Data;

/**
 * 检查更新DTO
 *
 * @author zcd
 * @date 17/10/31
 */
@Data
public class CheckVersionUpdateReqDTO {
    private Integer code;
    private String versionNo;

    /**
     * 平台  Android 为 2
     */
    private Integer platform  = 2 ;

    /**
     * 1 , 学生版  2 ,笑联云 3 , 笑联企业版
     */
    private Integer product =  1;


    /**
     * 手机号
     */
    private String mobile ;
}
