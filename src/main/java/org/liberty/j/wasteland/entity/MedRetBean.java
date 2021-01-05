package org.liberty.j.wasteland.entity;

public class MedRetBean {
    private String sID;
    private String mrID;
    private String pID;
    private String mrDate;
    private double mrFee;
    private String mrStatus;

    public MedRetBean() {
    }

    public MedRetBean(String sID, String mrID, String pID, String mrDate, double mrFee, String mrStatus) {
        this.sID = sID;
        this.mrID = mrID;
        this.pID = pID;
        this.mrDate = mrDate;
        this.mrFee = mrFee;
        this.mrStatus = mrStatus;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getMrID() {
        return mrID;
    }

    public void setMrID(String mrID) {
        this.mrID = mrID;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getMrDate() {
        return mrDate;
    }

    public void setMrDate(String mrDate) {
        this.mrDate = mrDate;
    }

    public double getMrFee() {
        return mrFee;
    }

    public void setMrFee(double mrFee) {
        this.mrFee = mrFee;
    }

    public String getMrStatus() {
        return mrStatus;
    }

    public void setMrStatus(String mrStatus) {
        this.mrStatus = mrStatus;
    }
}
