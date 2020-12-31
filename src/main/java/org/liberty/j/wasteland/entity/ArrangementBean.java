package org.liberty.j.wasteland.entity;

public class ArrangementBean {
    private String eID;
    private String eName;
    private String eDept;
    private String eDesc;
    private Double eFee;
    private String ePosition;
    private String eStartTime;
    private String eEndTime;
    private String eCurrState;

    public ArrangementBean() {
    }

    public ArrangementBean(String eID, String eName, String eDept, String eDesc, Double eFee, String ePosition,
            String eStartTime, String eEndTime, String eCurrState) {
        this.eID = eID;
        this.eName = eName;
        this.eDept = eDept;
        this.eDesc = eDesc;
        this.eFee = eFee;
        this.ePosition = ePosition;
        this.eStartTime = eStartTime;
        this.eEndTime = eEndTime;
        this.eCurrState = eCurrState;
    }

    public String geteID() {
        return eID;
    }

    public void seteID(String eID) {
        this.eID = eID;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String geteDept() {
        return eDept;
    }

    public void seteDept(String eDept) {
        this.eDept = eDept;
    }

    public String geteDesc() {
        return eDesc;
    }

    public void seteDesc(String eDesc) {
        this.eDesc = eDesc;
    }

    public Double geteFee() {
        return eFee;
    }

    public void seteFee(Double eFee) {
        this.eFee = eFee;
    }

    public String getePosition() {
        return ePosition;
    }

    public void setePosition(String ePosition) {
        this.ePosition = ePosition;
    }

    public String geteStartTime() {
        return eStartTime;
    }

    public void seteStartTime(String eStartTime) {
        this.eStartTime = eStartTime;
    }

    public String geteEndTime() {
        return eEndTime;
    }

    public void seteEndTime(String eEndTime) {
        this.eEndTime = eEndTime;
    }

    public String geteCurrState() {
        return eCurrState;
    }

    public void seteCurrState(String eCurrState) {
        this.eCurrState = eCurrState;
    }


}
