package org.liberty.j.wasteland.entity;

public class MedicineBean {
    private String medicineID;
    private String medicineName;
    private Integer count;
    private String Company;
    private double MedicinePrice;

    public MedicineBean(String medicineID, String medicineName, Integer count, String company, double medicinePrice) {
        this.medicineID = medicineID;
        this.medicineName = medicineName;
        this.count = count;
        Company = company;
        MedicinePrice = medicinePrice;
    }

    public MedicineBean() {
    }

    public String getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public double getMedicinePrice() {
        return MedicinePrice;
    }

    public void setMedicinePrice(double medicinePrice) {
        MedicinePrice = medicinePrice;
    }
}
