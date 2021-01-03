package org.liberty.j.wasteland.entity;

public class AnalysisBean {
    private String TestItemID;
    private String TestName;
    private String Dept;
    private float TestPrice;

    public AnalysisBean() {
    }

    public AnalysisBean(String testItemID, String testName, String dept, float testPrice) {
        TestItemID = testItemID;
        TestName = testName;
        Dept = dept;
        TestPrice = testPrice;
    }

    public String getTestItemID() {
        return TestItemID;
    }

    public void setTestItemID(String testItemID) {
        TestItemID = testItemID;
    }

    public String getTestName() {
        return TestName;
    }

    public void setTestName(String testName) {
        TestName = testName;
    }

    public String getDept() {
        return Dept;
    }

    public void setDept(String dept) {
        Dept = dept;
    }

    public float getTestPrice() {
        return TestPrice;
    }

    public void setTestPrice(float testPrice) {
        TestPrice = testPrice;
    }
}
