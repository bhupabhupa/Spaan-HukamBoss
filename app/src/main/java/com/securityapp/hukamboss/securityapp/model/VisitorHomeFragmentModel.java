package com.securityapp.hukamboss.securityapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bhupa on 15/02/18.
 */

public class VisitorHomeFragmentModel implements Serializable {
    private String gateNo;
    private String visitorMobile;
    private String visitorName;
    private String noOfVisitors;
    private String visitorAddress;
    private String visitorType;
    private String orgName;
    private String picPath;
    private String flatNo;

    private String residentName;
    private String societyBearer;
    private String vehicleNo;
    private String vehicleType;
    private String vehicleCount;
    private String narration;
    private String proofOfID;
    private String idNo;

    private boolean societyPurpose;
    private String staffName;
    private String materialDetails;
    private String materialDetailsPicPath;
    private String materialStoragePlace;
    private String materialStoragePicPath;
    private ArrayList<String> selectedFlatNo;
    private HashMap<String, String> flatNoResName;
    private HashMap<String, String> flatNoMemId;
    private ArrayList<String> selectedSocietyBearer;
    private HashMap<String, String> bearerMemId;
    private boolean isVisitorPicCaptured;
    private String societyBearerStr;
    private String societyBearerMemberIdStr;
    private boolean isOTPVerified;
    private String bypassName;
    private String bypassRemark;
    private boolean hasPastIncidence;
    private boolean rejected;
    private boolean isCameraCaptured;
    private boolean previousPhoto;
    private boolean isBypassFilled;
    private boolean correctPhotoURL;

    private String startDateTime;
    private String endDateTime;

    public VisitorHomeFragmentModel() {
        this.selectedFlatNo = new ArrayList<>();
        this.flatNoResName = new HashMap<>();
        this.flatNoMemId = new HashMap<>();
        this.selectedSocietyBearer = new ArrayList<>();
        this.bearerMemId = new HashMap<>();
    }


    public boolean isSocietyPurpose() {
        return societyPurpose;
    }

    public void setSocietyPurpose(boolean societyPurpose) {

        this.societyPurpose = societyPurpose;
    }



    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getSocietyBearer() {
        return societyBearer;
    }

    public void setSocietyBearer(String societyBearer) {
        this.societyBearer = societyBearer;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(String vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getProofOfID() {
        return proofOfID;
    }

    public void setProofOfID(String proofOfID) {
        this.proofOfID = proofOfID;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }



    public String getGateNo() {
        return gateNo;
    }

    public void setGateNo(String gateNo) {
        this.gateNo = gateNo;
    }

    public String getVisitorMobile() {
        return visitorMobile;
    }

    public void setVisitorMobile(String visitorMobile) {
        this.visitorMobile = visitorMobile;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getNoOfVisitors() {
        return noOfVisitors;
    }

    public void setNoOfVisitors(String noOfVisitors) {
        this.noOfVisitors = noOfVisitors;
    }

    public String getVisitorAddress() {
        return visitorAddress;
    }

    public void setVisitorAddress(String visitorAddress) {
        this.visitorAddress = visitorAddress;
    }

    public String getVisitorType() {
        return visitorType;
    }

    public void setVisitorType(String visitorType) {
        this.visitorType = visitorType;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getMaterialDetails() {
        return materialDetails;
    }

    public void setMaterialDetails(String materialDetails) {
        this.materialDetails = materialDetails;
    }

    public String getMaterialDetailsPicPath() {
        return materialDetailsPicPath;
    }

    public void setMaterialDetailsPicPath(String materialDetailsPicPath) {
        this.materialDetailsPicPath = materialDetailsPicPath;
    }

    public String getMaterialStoragePlace() {
        return materialStoragePlace;
    }

    public void setMaterialStoragePlace(String materialStoragePlace) {
        this.materialStoragePlace = materialStoragePlace;
    }

    public String getMaterialStoragePicPath() {
        return materialStoragePicPath;
    }

    public void setMaterialStoragePicPath(String materialStoragePicPath) {
        this.materialStoragePicPath = materialStoragePicPath;
    }


    public ArrayList<String> getSelectedFlatNo() {
        return selectedFlatNo;
    }

    public void setSelectedFlatNo(ArrayList<String> selectedFlatNo) {
        this.selectedFlatNo = selectedFlatNo;
    }

    public HashMap<String, String> getFlatNoResName() {
        return flatNoResName;
    }

    public void setFlatNoResName(HashMap<String, String> flatNoResName) {
        this.flatNoResName = flatNoResName;
    }

    public HashMap<String, String> getFlatNoMemId() {
        return flatNoMemId;
    }

    public void setFlatNoMemId(HashMap<String, String> flatNoMemId) {
        this.flatNoMemId = flatNoMemId;
    }



    public ArrayList<String> getSelectedSocietyBearer() {
        return selectedSocietyBearer;
    }

    public void setSelectedSocietyBearer(ArrayList<String> selectedSocietyBearer) {
        this.selectedSocietyBearer = selectedSocietyBearer;
    }

    public HashMap<String, String> getBearerMemId() {
        return bearerMemId;
    }

    public void setBearerMemId(HashMap<String, String> bearerMemId) {
        this.bearerMemId = bearerMemId;
    }


    public boolean isVisitorPicCaptured() {
        return isVisitorPicCaptured;
    }

    public void setVisitorPicCaptured(boolean visitorPicCaptured) {
        isVisitorPicCaptured = visitorPicCaptured;
    }

    public String getSocietyBearerStr() {
        return societyBearerStr;
    }

    public void setSocietyBearerStr(String societyBearerStr) {
        this.societyBearerStr = societyBearerStr;
    }

    public String getSocietyBearerMemberIdStr() {
        return societyBearerMemberIdStr;
    }

    public void setSocietyBearerMemberIdStr(String societyBearerMemberIdStr) {
        this.societyBearerMemberIdStr = societyBearerMemberIdStr;
    }

    public boolean isOTPVerified() {
        return isOTPVerified;
    }

    public void setOTPVerified(boolean OTPVerified) {
        isOTPVerified = OTPVerified;
    }

    public String getBypassName() {
        return bypassName;
    }

    public void setBypassName(String bypassName) {
        this.bypassName = bypassName;
    }

    public String getBypassRemark() {
        return bypassRemark;
    }

    public void setBypassRemark(String bypassRemark) {
        this.bypassRemark = bypassRemark;
    }



    /*public boolean isCheckedPastIncidence() {
        return checkedPastIncidence;
    }*/

    /*
    public void setCheckedPastIncidence(boolean checkedPastIncidence) {
        this.checkedPastIncidence = checkedPastIncidence;
    }*/

    public boolean isHasPastIncidence() {
        return hasPastIncidence;
    }

    public void setHasPastIncidence(boolean hasPastIncidence) {
        this.hasPastIncidence = hasPastIncidence;
    }

    //private boolean checkedPastIncidence;


    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }



    public boolean isCameraCaptured() {
        return isCameraCaptured;
    }

    public void setCameraCaptured(boolean cameraCaptured) {
        isCameraCaptured = cameraCaptured;
    }



    public boolean hasPreviousPhoto() {
        return previousPhoto;
    }

    public void setPreviousPhoto(boolean previousPhoto) {
        this.previousPhoto = previousPhoto;
    }



    public boolean isBypassFilled() {
        return isBypassFilled;
    }

    public void setBypassFilled(boolean bypassFilled) {
        isBypassFilled = bypassFilled;
    }


    public boolean isCorrectPhotoURL() {
        return correctPhotoURL;
    }

    public void setCorrectPhotoURL(boolean correctPhotoURL) {
        this.correctPhotoURL = correctPhotoURL;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }
}
