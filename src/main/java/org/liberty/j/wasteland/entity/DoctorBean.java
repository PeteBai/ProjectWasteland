package org.liberty.j.wasteland.entity;

public class DoctorBean {
    private String sID;
    private String sName;
    private String sDept;
    private String sDesc;
    private Double sFee;
    private String sPassword;
    private String sPosition;

    public DoctorBean() {
    }

    public DoctorBean(String sID, String sName, String sDept, String sDesc, Double sFee, String sPassword, String sPosition) {
        this.sID = sID;
        this.sName = sName;
        this.sDept = sDept;
        this.sDesc = sDesc;
        this.sFee = sFee;
        this.sPassword = sPassword;
        this.sPosition = sPosition;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsDept() {
        return sDept;
    }

    public void setsDept(String sDept) {
        this.sDept = sDept;
    }

    public String getsDesc() {
        return sDesc;
    }

    public void setsDesc(String sDesc) {
        this.sDesc = sDesc;
    }

    public Double getsFee() {
        return sFee;
    }

    public void setsFee(Double sFee) {
        this.sFee = sFee;
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }

    public String getsPosition() {
        return sPosition;
    }

    public void setsPosition(String sPosition) {
        this.sPosition = sPosition;
    }
}
