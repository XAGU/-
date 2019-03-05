package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

@Data
public class ResidenceUpdateRespDTO {

    //  返回的结果
    private Boolean result;

    // 是否是当前供水时段
    private Boolean timeValid;

    // 标题
    private String title;

    // 提示内容
    private String remark;

    private String failReason ;   // 失败原因


}
