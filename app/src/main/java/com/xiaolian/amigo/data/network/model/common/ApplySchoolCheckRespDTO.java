package com.xiaolian.amigo.data.network.model.common;


import lombok.Data;

@Data
public class ApplySchoolCheckRespDTO {
    private String id;
    private String reason;
    private String schoolName;
    private boolean hasApply;
}
