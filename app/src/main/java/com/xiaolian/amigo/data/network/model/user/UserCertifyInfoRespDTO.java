package com.xiaolian.amigo.data.network.model.user;

import java.util.List;

public class UserCertifyInfoRespDTO {

        /**
         * className : string
         * faculty : string
         * failReason : string
         * grade : 0
         * idCardBehind : string
         * idCardFront : string
         * major : string
         * status : 0
         * stuNum : 0
         * stuPictureU : ["string"]
         */

        private String className;
        private String faculty;
        private String failReason;
        private int grade;
        private String idCardBehindData;
        private String idCardFrontData;
        private String major;
        private int status;
        private int stuNum;
        private java.util.List<String> stuPicturesData;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getFaculty() {
            return faculty;
        }

        public void setFaculty(String faculty) {
            this.faculty = faculty;
        }

        public String getFailReason() {
            return failReason;
        }

        public void setFailReason(String failReason) {
            this.failReason = failReason;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getIdCardBehind() {
            return idCardBehindData;
        }


        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStuNum() {
            return stuNum;
        }

        public void setStuNum(int stuNum) {
            this.stuNum = stuNum;
        }

    public String getIdCardBehindData() {
        return idCardBehindData;
    }

    public void setIdCardBehindData(String idCardBehindData) {
        this.idCardBehindData = idCardBehindData;
    }

    public String getIdCardFrontData() {
        return idCardFrontData;
    }

    public void setIdCardFrontData(String idCardFrontData) {
        this.idCardFrontData = idCardFrontData;
    }

    public List<String> getStuPicturesData() {
        return stuPicturesData;
    }

    public void setStuPicturesData(List<String> stuPicturesData) {
        this.stuPicturesData = stuPicturesData;
    }
}
