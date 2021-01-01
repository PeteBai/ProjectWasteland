package org.liberty.j.wasteland.entity;

public class ReservationBean {
    private String R_ID;
    private String R_Reserve_number;
    private String sID;
    private String docStartTime;
    private Integer WeekNum;
    private String R_Status;

    public ReservationBean() {
    }

    public ReservationBean(String r_ID, String r_Reserve_number, String sID, String docStartTime, Integer weekNum, String r_Status) {
        R_ID = r_ID;
        R_Reserve_number = r_Reserve_number;
        this.sID = sID;
        this.docStartTime = docStartTime;
        WeekNum = weekNum;
        R_Status = r_Status;
    }

    public String getR_ID() {
        return R_ID;
    }

    public void setR_ID(String r_ID) {
        R_ID = r_ID;
    }

    public String getR_Reserve_number() {
        return R_Reserve_number;
    }

    public void setR_Reserve_number(String r_Reserve_number) {
        R_Reserve_number = r_Reserve_number;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getDocStartTime() {
        return docStartTime;
    }

    public void setDocStartTime(String docStartTime) {
        this.docStartTime = docStartTime;
    }

    public Integer getWeekNum() {
        return WeekNum;
    }

    public void setWeekNum(Integer weekNum) {
        WeekNum = weekNum;
    }

    public String getR_Status() {
        return R_Status;
    }

    public void setR_Status(String r_Status) {
        R_Status = r_Status;
    }
}
