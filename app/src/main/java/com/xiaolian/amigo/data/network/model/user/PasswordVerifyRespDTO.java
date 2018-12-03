package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

@Data
public class PasswordVerifyRespDTO {
     String failReason;
     Integer protectInMinutes;
     Integer remaining;
     boolean result;
}
