package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

@Data
public class QueueInfo {

    private long bathBookingId;
    private long id;
    private String location;
    private int remain;  // 排队人数
}
