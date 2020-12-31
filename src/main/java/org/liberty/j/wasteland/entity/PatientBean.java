package org.liberty.j.wasteland.entity;

public class PatientBean {
    private String pID;
    private String pName;
    private char pGender;
    private String pNationality;
    private String pPhoneNumber;
    private Boolean pIsMarried;

    public PatientBean() {
    }

    public PatientBean(String pID, String pName, char pGender, String pNationality, String pPhoneNumber, Boolean pIsMarried) {
        this.pID = pID;
        this.pName = pName;
        this.pGender = pGender;
        this.pNationality = pNationality;
        this.pPhoneNumber = pPhoneNumber;
        this.pIsMarried = pIsMarried;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public char getpGender() {
        return pGender;
    }

    public void setpGender(char pGender) {
        this.pGender = pGender;
    }

    public String getpNationality() {
        return pNationality;
    }

    public void setpNationality(String pNationality) {
        this.pNationality = pNationality;
    }

    public String getpPhoneNumber() {
        return pPhoneNumber;
    }

    public void setpPhoneNumber(String pPhoneNumber) {
        this.pPhoneNumber = pPhoneNumber;
    }

    public Boolean getpIsMarried() {
        return pIsMarried;
    }

    public void setpIsMarried(Boolean pIsMarried) {
        this.pIsMarried = pIsMarried;
    }
}
