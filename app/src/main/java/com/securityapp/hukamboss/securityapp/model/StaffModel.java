package com.securityapp.hukamboss.securityapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bhupa on 14/03/18.
 */

public class StaffModel {
    private String staffID;
    private String staffName;
    private String inDate;
    private String inDateTime;
    private String Id;
    private String staffForm;

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getStaffForm() {
        return staffForm;
    }

    public void setStaffForm(String staffForm) {
        this.staffForm = staffForm;
    }
}
