package com.xiaolian.amigo.data.vo;

import java.util.List;

public class UserCertificationStatus {


    public UserCertificationStatus(String departemtn , String profession ,String gradeStr ,
               String classStr , String studentIdStr , List<String> studentImageBase64 ,String frontImageBase64 ,
               String backImageBase64){

        setBackImageBase64(backImageBase64);
        setDepartemtn(departemtn);
        setProfession(profession);
        setGradeStr(gradeStr);
        setClassStr(classStr);
        setStudentIdStr(studentIdStr);
        setStudentImageBase64(studentImageBase64);
        setFrontImageBase64(frontImageBase64);
    }

    public UserCertificationStatus(String departemtn , String profession ,String gradeStr ,
                                   String classStr , String studentIdStr , List<String> studentImageBase64 ){

        setDepartemtn(departemtn);
        setProfession(profession);
        setGradeStr(gradeStr);
        setClassStr(classStr);
        setStudentIdStr(studentIdStr);
        setStudentImageBase64(studentImageBase64);
    }


    private String departemtn ;

    private String profession ;

    private String gradeStr ;

    private String classStr ;

    private String studentIdStr ;

    private List<String> studentImageBase64 ;

    private String frontImageBase64 ;

    private String backImageBase64 ;


    public String getDepartemtn() {
        return departemtn;
    }

    public void setDepartemtn(String departemtn) {
        this.departemtn = departemtn;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getGradeStr() {
        return gradeStr;
    }

    public void setGradeStr(String gradeStr) {
        this.gradeStr = gradeStr;
    }

    public String getClassStr() {
        return classStr;
    }

    public void setClassStr(String classStr) {
        this.classStr = classStr;
    }

    public String getStudentIdStr() {
        return studentIdStr;
    }

    public void setStudentIdStr(String studentIdStr) {
        this.studentIdStr = studentIdStr;
    }

    public List<String> getStudentImageBase64() {
        return studentImageBase64;
    }

    public void setStudentImageBase64(List<String> studentImageBase64) {
        this.studentImageBase64 = studentImageBase64;
    }

    public String getFrontImageBase64() {
        return frontImageBase64;
    }

    public void setFrontImageBase64(String frontImageBase64) {
        this.frontImageBase64 = frontImageBase64;
    }

    public String getBackImageBase64() {
        return backImageBase64;
    }

    public void setBackImageBase64(String backImageBase64) {
        this.backImageBase64 = backImageBase64;
    }
}
