package com.xiaolian.amigo.data.network.model.login;

import lombok.Data;

@Data
public class CancelThirdBindReqDTO {
    String failReason;
    boolean result;
}
