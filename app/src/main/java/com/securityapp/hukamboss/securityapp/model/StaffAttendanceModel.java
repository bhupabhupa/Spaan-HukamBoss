package com.securityapp.hukamboss.securityapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bhupa on 26/03/18.
 */

public class StaffAttendanceModel {
    private String userID;
    private String staffName;
    private String staffRole;
    private String loginDateTime;
    private String logOutDateTime;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }

    public String getLoginDateTime() {
        return loginDateTime;
    }

    public void setLoginDateTime(String loginDateTime) {
        try {
            String []tempVal = loginDateTime.split("T");
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(tempVal[0]);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String inDate = dateFormat.format(date1);
            Date date2 = new SimpleDateFormat("HH:mm:ss").parse(tempVal[1]);
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aaa");
            String inTime = timeFormat.format(date2);
            //this.inDateTime = inDate + " : " +inTime;
            this.loginDateTime = inDate + " : " +inTime;;
        } catch (Exception e) {}

    }

    public String getLogOutDateTime() {
        return logOutDateTime;
    }

    public void setLogOutDateTime(String logOutDateTime) {
        try {
            String []tempVal = logOutDateTime.split("T");
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(tempVal[0]);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String inDate = dateFormat.format(date1);
            Date date2 = new SimpleDateFormat("HH:mm:ss").parse(tempVal[1]);
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aaa");
            String inTime = timeFormat.format(date2);
            this.logOutDateTime = inDate + " : " +inTime;;
        } catch (Exception e) {}
    }
}
