package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 2/2/2020.
 */

public class ResignedStaffDatum {
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
    private Object currentAddress2;
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
    private Object permanentAddress2;
    @SerializedName("Permanent_Locality")
    @Expose
    private Object permanentLocality;
    @SerializedName("Permanent_Pincode")
    @Expose
    private Object permanentPincode;
    @SerializedName("Mobile_No_1")
    @Expose
    private String mobileNo1;
    @SerializedName("Mobile_No_2")
    @Expose
    private String mobileNo2;
    @SerializedName("Alternative_Mobile_No")
    @Expose
    private Object alternativeMobileNo;
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
    private Object nomineeMobileNo;
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
    private Object aadharNo;
    @SerializedName("Pancard_No")
    @Expose
    private Object pancardNo;
    @SerializedName("Batch_No")
    @Expose
    private Object batchNo;
    @SerializedName("PF_No")
    @Expose
    private Object pFNo;
    @SerializedName("ESIC_No")
    @Expose
    private Object eSICNo;
    @SerializedName("Bank_Name")
    @Expose
    private Object bankName;
    @SerializedName("Bank_Account_No")
    @Expose
    private Object bankAccountNo;
    @SerializedName("Bank_Branch")
    @Expose
    private Object bankBranch;
    @SerializedName("Bank_IFSC_Code")
    @Expose
    private Object bankIFSCCode;
    @SerializedName("Bank_City")
    @Expose
    private Object bankCity;
    @SerializedName("Shift")
    @Expose
    private String shift;
    @SerializedName("From_Hours")
    @Expose
    private Object fromHours;
    @SerializedName("To_Hours")
    @Expose
    private Object toHours;
    @SerializedName("Department")
    @Expose
    private String department;
    @SerializedName("PublicTravelNo")
    @Expose
    private String publicTravelNo;
    @SerializedName("Salary")
    @Expose
    private Object salary;
    @SerializedName("SalaryType")
    @Expose
    private Object salaryType;
    @SerializedName("ResignationDate")
    @Expose
    private String resignationDate;
    @SerializedName("ResignationReason")
    @Expose
    private String resignationReason;

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

    public Object getCurrentAddress2() {
        return currentAddress2;
    }

    public void setCurrentAddress2(Object currentAddress2) {
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

    public Object getPermanentAddress2() {
        return permanentAddress2;
    }

    public void setPermanentAddress2(Object permanentAddress2) {
        this.permanentAddress2 = permanentAddress2;
    }

    public Object getPermanentLocality() {
        return permanentLocality;
    }

    public void setPermanentLocality(Object permanentLocality) {
        this.permanentLocality = permanentLocality;
    }

    public Object getPermanentPincode() {
        return permanentPincode;
    }

    public void setPermanentPincode(Object permanentPincode) {
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

    public Object getAlternativeMobileNo() {
        return alternativeMobileNo;
    }

    public void setAlternativeMobileNo(Object alternativeMobileNo) {
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

    public Object getNomineeMobileNo() {
        return nomineeMobileNo;
    }

    public void setNomineeMobileNo(Object nomineeMobileNo) {
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

    public Object getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(Object aadharNo) {
        this.aadharNo = aadharNo;
    }

    public Object getPancardNo() {
        return pancardNo;
    }

    public void setPancardNo(Object pancardNo) {
        this.pancardNo = pancardNo;
    }

    public Object getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Object batchNo) {
        this.batchNo = batchNo;
    }

    public Object getPFNo() {
        return pFNo;
    }

    public void setPFNo(Object pFNo) {
        this.pFNo = pFNo;
    }

    public Object getESICNo() {
        return eSICNo;
    }

    public void setESICNo(Object eSICNo) {
        this.eSICNo = eSICNo;
    }

    public Object getBankName() {
        return bankName;
    }

    public void setBankName(Object bankName) {
        this.bankName = bankName;
    }

    public Object getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(Object bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public Object getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(Object bankBranch) {
        this.bankBranch = bankBranch;
    }

    public Object getBankIFSCCode() {
        return bankIFSCCode;
    }

    public void setBankIFSCCode(Object bankIFSCCode) {
        this.bankIFSCCode = bankIFSCCode;
    }

    public Object getBankCity() {
        return bankCity;
    }

    public void setBankCity(Object bankCity) {
        this.bankCity = bankCity;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public Object getFromHours() {
        return fromHours;
    }

    public void setFromHours(Object fromHours) {
        this.fromHours = fromHours;
    }

    public Object getToHours() {
        return toHours;
    }

    public void setToHours(Object toHours) {
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

    public Object getSalary() {
        return salary;
    }

    public void setSalary(Object salary) {
        this.salary = salary;
    }

    public Object getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(Object salaryType) {
        this.salaryType = salaryType;
    }

    public String getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(String resignationDate) {
        this.resignationDate = resignationDate;
    }

    public String getResignationReason() {
        return resignationReason;
    }

    public void setResignationReason(String resignationReason) {
        this.resignationReason = resignationReason;
    }

}
