package com.xiaolian.amigo.data.network.model.user;

import java.util.List;

import okhttp3.RequestBody;

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
    private RequestBody idCardBehind;
    private RequestBody idCardFront;
    private String major;
    private int stuNum;
    private List<RequestBody> stuPictureUrls;

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

    public RequestBody getIdCardBehind() {
        return idCardBehind;
    }

    public void setIdCardBehind(RequestBody idCardBehind) {
        this.idCardBehind = idCardBehind;
    }

    public RequestBody getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(RequestBody idCardFront) {
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

    public List<RequestBody> getStuPictureUrls() {
        return stuPictureUrls;
    }

    public void setStuPictureUrls(List<RequestBody> stuPictureUrls) {
        this.stuPictureUrls = stuPictureUrls;
    }
}
