package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * 公共浴室预约
 *
 * @author zcd
 * @date 18/7/17
 */
@Data
public class BathPreBookingRespDTO {

        /**
         * location : string
         * maxMissAbleTimes : 0
         * missedTimes : 0
         * prepayInfo : {"balance":0,"minPrepay":0,"prepay":0}
         * reservedMinute : 0
         */

        private String location;
        private int maxMissAbleTimes;
        private int missedTimes;
        private PrepayInfo prepayInfo;
        private String reservedMinute;

}
