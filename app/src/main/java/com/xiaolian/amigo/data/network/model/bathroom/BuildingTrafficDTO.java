package com.xiaolian.amigo.data.network.model.bathroom;

import java.util.List;

/**
 * 楼层流量
 */
public class BuildingTrafficDTO {

        /**
         * buildingId : 0
         * floors : [{"availableCount":0,"id":0,"name":"string","waitCount":0}]
         * schoolId : 0
         */

        private long buildingId;
        private long schoolId;
        private java.util.List<FloorsBean> floors;

        public long getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(long buildingId) {
            this.buildingId = buildingId;
        }

        public long getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(long schoolId) {
            this.schoolId = schoolId;
        }

        public List<FloorsBean> getFloors() {
            return floors;
        }

        public void setFloors(List<FloorsBean> floors) {
            this.floors = floors;
        }

        public static class FloorsBean {
            /**
             * availableCount : 0
             * id : 0
             * name : string
             * waitCount : 0
             */

            private int availableCount;
            private long id;
            private String name;
            private int waitCount;
            private String deviceNo ;

            public String getDeviceNo() {
                return deviceNo;
            }

            public void setDeviceNo(String deviceNo) {
                this.deviceNo = deviceNo;
            }

            public int getAvailableCount() {
                return availableCount;
            }

            public void setAvailableCount(int availableCount) {
                this.availableCount = availableCount;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getWaitCount() {
                return waitCount;
            }

            public void setWaitCount(int waitCount) {
                this.waitCount = waitCount;
            }
        }
    }
