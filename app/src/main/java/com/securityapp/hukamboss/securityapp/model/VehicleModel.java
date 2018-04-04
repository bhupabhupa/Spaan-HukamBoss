package com.securityapp.hukamboss.securityapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bhupa on 14/03/18.
 */

public class VehicleModel {
    private String vehicleNo;
    private String noOfVehicles;
    private String visitID;
    private String vehicleType;
    private  String inDate;


    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getNoOfVehicles() {
        return noOfVehicles;
    }

    public void setNoOfVehicles(String noOfVehicles) {
        this.noOfVehicles = noOfVehicles;
    }

    public String getVisitID() {
        return visitID;
    }

    public void setVisitID(String visitID) {
        this.visitID = visitID;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public void setInTime(String in_time) {
        try {
            String []tempVal = in_time.split("T");
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(tempVal[0]);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String inDate = dateFormat.format(date1);
            Date date2 = new SimpleDateFormat("HH:mm:ss").parse(tempVal[1]);
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aaa");
            String inTime = timeFormat.format(date2);
            this.inDate = inDate + " : " +inTime;
        } catch (Exception e) {}
    }
}
