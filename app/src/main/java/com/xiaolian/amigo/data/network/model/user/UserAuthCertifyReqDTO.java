package com.xiaolian.amigo.data.network.model.user;

import java.util.List;

public class UserAuthCertifyReqDTO {


    /**
     * className : string
     * faculty : string
     * grade : 0
     * idCardBehind : string
     * idCardFront : string
     * major : string
     * stuNum : 0
     * stuPictureUrls : ["string"]
     */

    private String className;
    private String faculty;
    private int grade;
    private String idCardBehind;
    private String idCardFront;
    private String major;
    private int stuNum;
    private List<String> stuPictureUrls;

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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getIdCardBehind() {
        return idCardBehind;
    }

    public void setIdCardBehind(String idCardBehind) {
        this.idCardBehind = idCardBehind;
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getStuNum() {
        return stuNum;
    }

    public void setStuNum(int stuNum) {
        this.stuNum = stuNum;
    }

    public List<String> getStuPictureUrls() {
        return stuPictureUrls;
    }

    public void setStuPictureUrls(List<String> stuPictureUrls) {
        this.stuPictureUrls = stuPictureUrls;
    }
}
