package org.liberty.j.wasteland.entity;

public class MedicineTableBean {
    String sid;
    String mtID;
    String CID;
    String mtDate;
    double mtFee;
    String mtStatus;

    public MedicineTableBean() {
    }

    public MedicineTableBean(String sid, String mtID, String CID, String mtDate, double mtFee, String mtStatus) {
        this.sid = sid;
        this.mtID = mtID;
        this.CID = CID;
        this.mtDate = mtDate;
        this.mtFee = mtFee;
        this.mtStatus = mtStatus;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getMtID() {
        return mtID;
    }

    public void setMtID(String mtID) {
        this.mtID = mtID;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getMtDate() {
        return mtDate;
    }

    public void setMtDate(String mtDate) {
        this.mtDate = mtDate;
    }

    public double getMtFee() {
        return mtFee;
    }

    public void setMtFee(double mtFee) {
        this.mtFee = mtFee;
    }

    public String getMtStatus() {
        return mtStatus;
    }

    public void setMtStatus(String mtStatus) {
        this.mtStatus = mtStatus;
    }
}
