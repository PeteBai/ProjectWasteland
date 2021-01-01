package org.liberty.j.wasteland.entity;

public class ArrangementBean {
    private String sID;
    private String sName;
    private String sDept;
    private String sDesc;
    private Double sFee;
    private String sPosition;
    private String docStartTime;
    private String docEndTime;
    private String docCurrState;

    public ArrangementBean(String sID, String sName, String sDept, String sDesc, Double sFee, String sPosition, String docStartTime, String docEndTime, String docCurrState) {
        this.sID = sID;
        this.sName = sName;
        this.sDept = sDept;
        this.sDesc = sDesc;
        this.sFee = sFee;
        this.sPosition = sPosition;
        this.docStartTime = docStartTime;
        this.docEndTime = docEndTime;
        this.docCurrState = docCurrState;
    }

    public ArrangementBean() {
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

    public String getsPosition() {
        return sPosition;
    }

    public void setsPosition(String sPosition) {
        this.sPosition = sPosition;
    }

    public String getDocStartTime() {
        return docStartTime;
    }

    public void setDocStartTime(String docStartTime) {
        this.docStartTime = docStartTime;
    }

    public String getDocEndTime() {
        return docEndTime;
    }

    public void setDocEndTime(String docEndTime) {
        this.docEndTime = docEndTime;
    }

    public String getDocCurrState() {
        return docCurrState;
    }

    public void setDocCurrState(String docCurrState) {
        this.docCurrState = docCurrState;
    }
}
