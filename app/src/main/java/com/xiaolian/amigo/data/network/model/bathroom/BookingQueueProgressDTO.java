package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

@Data
public class BookingQueueProgressDTO {

        /**
         * bathBookingId : 0
         * id : 0
         * location : string
         * remain : 0
         */

        private long bathBookingId;
        private long id;
        private String location;
        private int remain;

    }
