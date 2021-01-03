package org.liberty.j.wasteland.entity;

public class HistoryBean {
    private String pID;
    private String sID;
    private String hID;
    private String hDate;
    private String hComment;

    public HistoryBean() {
    }

    public HistoryBean(String pID, String sID, String hID, String hDate, String hComment) {
        this.pID = pID;
        this.sID = sID;
        this.hID = hID;
        this.hDate = hDate;
        this.hComment = hComment;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String gethID() {
        return hID;
    }

    public void sethID(String hID) {
        this.hID = hID;
    }

    public String gethDate() {
        return hDate;
    }

    public void sethDate(String hDate) {
        this.hDate = hDate;
    }

    public String gethComment() {
        return hComment;
    }

    public void sethComment(String hComment) {
        this.hComment = hComment;
    }
}
