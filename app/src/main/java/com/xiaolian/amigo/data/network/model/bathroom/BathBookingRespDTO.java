package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * 公共浴室预约
 *
 * @author zcd
 * @date 18/7/4
 */
@Data
public class BathBookingRespDTO {

        /**
         * bathOrderId : 0
         * createTime : 0
         * deviceNo : 0
         * expiredTime : 0
         * id : 0
         * location : string
         * status : 0
         */

        private long bathOrderId;
        private long createTime;
        private long deviceNo;
        private long expiredTime;
        private long id;
        private String location;
        private int status;
        private int type ;

    }
