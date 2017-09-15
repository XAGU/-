package com.xiaolian.amigo.data.network.model;

import lombok.Data;

/**
 * @author zcd
 */
@Data
public class LoginRespDTO {

    private String token;
    private UserBean user;

    public static class UserBean {
        /**
         * building : string
         * buildingId : 0
         * floor : string
         * floorId : 0
         * id : 0
         * mobile : 0
         * nickName : string
         * pictureUrl : string
         * room : string
         * roomId : 0
         * schoolId : 0
         * schoolName : string
         * sex : 0
         * type : 0
         */

        private String building;
        private int buildingId;
        private String floor;
        private int floorId;
        private int id;
        private int mobile;
        private String nickName;
        private String pictureUrl;
        private String room;
        private int roomId;
        private int schoolId;
        private String schoolName;
        private int sex;
        private int type;

        public String getBuilding() {
            return building;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public int getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(int buildingId) {
            this.buildingId = buildingId;
        }

        public String getFloor() {
            return floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public int getFloorId() {
            return floorId;
        }

        public void setFloorId(int floorId) {
            this.floorId = floorId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMobile() {
            return mobile;
        }

        public void setMobile(int mobile) {
            this.mobile = mobile;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public int getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
