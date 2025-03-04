package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 2/1/2020.
 */

public class StaffDatum {
    @SerializedName("Staff_ID")
    @Expose
    private Integer staffID;
    @SerializedName("Staff_Code")
    @Expose
    private String staffCode;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("MiddleName")
    @Expose
    private String middleName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("Current_Address_1")
    @Expose
    private String currentAddress1;
    @SerializedName("Current_Address_2")
    @Expose
    private String currentAddress2;
    @SerializedName("Current_Locality")
    @Expose
    private String currentLocality;
    @SerializedName("Current_Pincode")
    @Expose
    private String currentPincode;
    @SerializedName("Permanent_Address_1")
    @Expose
    private String permanentAddress1;
    @SerializedName("Permanent_Address_2")
    @Expose
    private String permanentAddress2;
    @SerializedName("Permanent_Locality")
    @Expose
    private String permanentLocality;
    @SerializedName("Permanent_Pincode")
    @Expose
    private String permanentPincode;
    @SerializedName("Mobile_No_1")
    @Expose
    private String mobileNo1;
    @SerializedName("Mobile_No_2")
    @Expose
    private String mobileNo2;
    @SerializedName("Alternative_Mobile_No")
    @Expose
    private String alternativeMobileNo;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("BloodGroup")
    @Expose
    private String bloodGroup;
    @SerializedName("BirthDate")
    @Expose
    private String birthDate;
    @SerializedName("LicenceNo")
    @Expose
    private String licenceNo;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("JoiningDate")
    @Expose
    private String joiningDate;
    @SerializedName("PrefferedDepo")
    @Expose
    private Object prefferedDepo;
    @SerializedName("Age")
    @Expose
    private Integer age;
    @SerializedName("BirthMark")
    @Expose
    private String birthMark;
    @SerializedName("MaritalStatus")
    @Expose
    private String maritalStatus;
    @SerializedName("Children")
    @Expose
    private String children;
    @SerializedName("FathersName")
    @Expose
    private String fathersName;
    @SerializedName("FathersOccupation")
    @Expose
    private String fathersOccupation;
    @SerializedName("NomineeMobileNo")
    @Expose
    private String nomineeMobileNo;
    @SerializedName("FathersIncome")
    @Expose
    private String fathersIncome;
    @SerializedName("LicenceExpiryDate")
    @Expose
    private String licenceExpiryDate;
    @SerializedName("AnyMedicalProblem")
    @Expose
    private String anyMedicalProblem;
    @SerializedName("Qualification")
    @Expose
    private String qualification;
    @SerializedName("ReferedBy")
    @Expose
    private String referedBy;
    @SerializedName("Aadhar_No")
    @Expose
    private String aadharNo;
    @SerializedName("Pancard_No")
    @Expose
    private String pancardNo;
    @SerializedName("Batch_No")
    @Expose
    private String batchNo;
    @SerializedName("PF_No")
    @Expose
    private String pFNo;
    @SerializedName("ESIC_No")
    @Expose
    private String eSICNo;
    @SerializedName("Bank_Name")
    @Expose
    private String bankName;
    @SerializedName("Bank_Account_No")
    @Expose
    private String bankAccountNo;
    @SerializedName("Bank_Branch")
    @Expose
    private String bankBranch;
    @SerializedName("Bank_IFSC_Code")
    @Expose
    private String bankIFSCCode;
    @SerializedName("Bank_City")
    @Expose
    private String bankCity;
    @SerializedName("Shift")
    @Expose
    private String shift;
    @SerializedName("From_Hours")
    @Expose
    private String fromHours;
    @SerializedName("To_Hours")
    @Expose
    private String toHours;
    @SerializedName("Department")
    @Expose
    private String department;
    @SerializedName("PublicTravelNo")
    @Expose
    private String publicTravelNo;
    @SerializedName("Salary")
    @Expose
    private Float salary;
    @SerializedName("SalaryType")
    @Expose
    private String salaryType;

    public StaffDatum(Integer staffID, String staffCode, String firstName, String middleName, String lastName) {
        this.staffID = staffID;
        this.staffCode = staffCode;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public StaffDatum(Integer staffID, String staffCode, String firstName, String middleName, String lastName, String currentAddress1, String currentAddress2, String currentLocality, String permanentAddress1, String permanentAddress2, String designation, String currentPincode) {
        this.staffID = staffID;
        this.staffCode = staffCode;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.currentAddress1 = currentAddress1;
        this.currentAddress2 = currentAddress2;
        this.currentLocality = currentLocality;
        this.permanentAddress1 = permanentAddress1;
        this.permanentAddress2 = permanentAddress2;
        this.designation = designation;
        this.currentPincode = currentPincode;
    }

    public Integer getStaffID() {
        return staffID;
    }

    public void setStaffID(Integer staffID) {
        this.staffID = staffID;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCurrentAddress1() {
        return currentAddress1;
    }

    public void setCurrentAddress1(String currentAddress1) {
        this.currentAddress1 = currentAddress1;
    }

    public String getCurrentAddress2() {
        return currentAddress2;
    }

    public void setCurrentAddress2(String currentAddress2) {
        this.currentAddress2 = currentAddress2;
    }

    public String getCurrentLocality() {
        return currentLocality;
    }

    public void setCurrentLocality(String currentLocality) {
        this.currentLocality = currentLocality;
    }

    public String getCurrentPincode() {
        return currentPincode;
    }

    public void setCurrentPincode(String currentPincode) {
        this.currentPincode = currentPincode;
    }

    public String getPermanentAddress1() {
        return permanentAddress1;
    }

    public void setPermanentAddress1(String permanentAddress1) {
        this.permanentAddress1 = permanentAddress1;
    }

    public String getPermanentAddress2() {
        return permanentAddress2;
    }

    public void setPermanentAddress2(String permanentAddress2) {
        this.permanentAddress2 = permanentAddress2;
    }

    public String getPermanentLocality() {
        return permanentLocality;
    }

    public void setPermanentLocality(String permanentLocality) {
        this.permanentLocality = permanentLocality;
    }

    public String getPermanentPincode() {
        return permanentPincode;
    }

    public void setPermanentPincode(String permanentPincode) {
        this.permanentPincode = permanentPincode;
    }

    public String getMobileNo1() {
        return mobileNo1;
    }

    public void setMobileNo1(String mobileNo1) {
        this.mobileNo1 = mobileNo1;
    }

    public String getMobileNo2() {
        return mobileNo2;
    }

    public void setMobileNo2(String mobileNo2) {
        this.mobileNo2 = mobileNo2;
    }

    public String getAlternativeMobileNo() {
        return alternativeMobileNo;
    }

    public void setAlternativeMobileNo(String alternativeMobileNo) {
        this.alternativeMobileNo = alternativeMobileNo;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Object getPrefferedDepo() {
        return prefferedDepo;
    }

    public void setPrefferedDepo(Object prefferedDepo) {
        this.prefferedDepo = prefferedDepo;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBirthMark() {
        return birthMark;
    }

    public void setBirthMark(String birthMark) {
        this.birthMark = birthMark;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getFathersOccupation() {
        return fathersOccupation;
    }

    public void setFathersOccupation(String fathersOccupation) {
        this.fathersOccupation = fathersOccupation;
    }

    public String getNomineeMobileNo() {
        return nomineeMobileNo;
    }

    public void setNomineeMobileNo(String nomineeMobileNo) {
        this.nomineeMobileNo = nomineeMobileNo;
    }

    public String getFathersIncome() {
        return fathersIncome;
    }

    public void setFathersIncome(String fathersIncome) {
        this.fathersIncome = fathersIncome;
    }

    public String getLicenceExpiryDate() {
        return licenceExpiryDate;
    }

    public void setLicenceExpiryDate(String licenceExpiryDate) {
        this.licenceExpiryDate = licenceExpiryDate;
    }

    public String getAnyMedicalProblem() {
        return anyMedicalProblem;
    }

    public void setAnyMedicalProblem(String anyMedicalProblem) {
        this.anyMedicalProblem = anyMedicalProblem;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getReferedBy() {
        return referedBy;
    }

    public void setReferedBy(String referedBy) {
        this.referedBy = referedBy;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getPancardNo() {
        return pancardNo;
    }

    public void setPancardNo(String pancardNo) {
        this.pancardNo = pancardNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getPFNo() {
        return pFNo;
    }

    public void setPFNo(String pFNo) {
        this.pFNo = pFNo;
    }

    public String getESICNo() {
        return eSICNo;
    }

    public void setESICNo(String eSICNo) {
        this.eSICNo = eSICNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankIFSCCode() {
        return bankIFSCCode;
    }

    public void setBankIFSCCode(String bankIFSCCode) {
        this.bankIFSCCode = bankIFSCCode;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getFromHours() {
        return fromHours;
    }

    public void setFromHours(String fromHours) {
        this.fromHours = fromHours;
    }

    public String getToHours() {
        return toHours;
    }

    public void setToHours(String toHours) {
        this.toHours = toHours;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPublicTravelNo() {
        return publicTravelNo;
    }

    public void setPublicTravelNo(String publicTravelNo) {
        this.publicTravelNo = publicTravelNo;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    @Override
    public String toString() {
        return staffCode + " - " + firstName + " " + middleName + " " + lastName;
    }
}
