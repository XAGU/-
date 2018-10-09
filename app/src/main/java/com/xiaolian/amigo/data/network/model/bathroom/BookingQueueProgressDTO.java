package com.xiaolian.amigo.data.network.model.bathroom;

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

        public long getBathBookingId() {
                return bathBookingId;
        }

        public void setBathBookingId(long bathBookingId) {
                this.bathBookingId = bathBookingId;
        }

        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }

        public String getLocation() {
                return location;
        }

        public void setLocation(String location) {
                this.location = location;
        }

        public int getRemain() {
                return remain;
        }

        public void setRemain(int remain) {
                this.remain = remain;
        }
}
