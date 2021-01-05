package org.liberty.j.wasteland.entity;

public class ClinicRecordBean {
    public ClinicRecordBean() {
    }

    private String CID;
    private String pID;
    private String Curr_State;

    public ClinicRecordBean(String CID, String pID, String curr_State) {
        this.CID = CID;
        this.pID = pID;
        Curr_State = curr_State;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getCurr_State() {
        return Curr_State;
    }

    public void setCurr_State(String curr_State) {
        Curr_State = curr_State;
    }
}
