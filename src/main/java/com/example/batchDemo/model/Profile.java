package com.example.batchDemo.model;

public class Profile {
    private long empCode;
    private String empName;
    private String profileName;
    private double amount;

    public Profile(long empCode, String empName, String profileName) {
        this.empCode = empCode;
        this.empName = empName;
        this.profileName = profileName;
//        this.amount = amount;
    }

    public long getEmpCode() {
        return empCode;
    }

    public void setEmpCode(long empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

//    public double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
}