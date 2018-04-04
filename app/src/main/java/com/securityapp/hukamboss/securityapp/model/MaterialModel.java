package com.securityapp.hukamboss.securityapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bhupa on 19/03/18.
 */

public class MaterialModel {
    private String visitID;
    private String nameOfStaff;
    private String materialStoragePlace;
    private String inDateTime;
    private String materialDetails;

    public String getVisitID() {
        return visitID;
    }

    public void setVisitID(String visitID) {
        this.visitID = visitID;
    }

    public String getNameOfStaff() {
        return nameOfStaff;
    }

    public void setNameOfStaff(String nameOfStaff) {
        this.nameOfStaff = nameOfStaff;
    }

    public String getMaterialStoragePlace() {
        return materialStoragePlace;
    }

    public void setMaterialStoragePlace(String materialStoragePlace) {
        this.materialStoragePlace = materialStoragePlace;
    }

    public String getInDateTime() {
        return inDateTime;
    }

    public void setInDateTime(String inDateTime) {
        try {
            String []tempVal = inDateTime.split("T");
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(tempVal[0]);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String inDate = dateFormat.format(date1);
            Date date2 = new SimpleDateFormat("HH:mm:ss").parse(tempVal[1]);
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aaa");
            String inTime = timeFormat.format(date2);
            this.inDateTime = inDate + " : " +inTime;
        } catch (Exception e) {}
    }

    public String getMaterialDetails() {
        return materialDetails;
    }

    public void setMaterialDetails(String materialDetails) {
        this.materialDetails = materialDetails;
    }
}
