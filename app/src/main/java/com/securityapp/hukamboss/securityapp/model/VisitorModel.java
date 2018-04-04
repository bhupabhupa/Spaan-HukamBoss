package com.securityapp.hukamboss.securityapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Samman on 04/02/18.
 */

public class VisitorModel {
    String image;
    String name;
    String mobile;
    String in_time;
    String out_time;
    String type;
    String purpose;
    String in_gate;
    String out_gate;
    String hasVehicle;

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    private String visitorId;

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    private String visitId;

    public VisitorModel() {}

    public VisitorModel(String image, String name, String mobile, String in_time, String out_time, String type, String purpose, String in_gate, String out_gate, String visitId, String hasVehicle) {
        this.image = image;
        this.name = name;
        this.mobile = mobile;
        this.in_time = in_time;
        this.out_time = out_time;
        this.type = type;
        this.purpose = purpose;
        this.in_gate = in_gate;
        this.out_gate = out_gate;
        visitId = visitId;
        this.hasVehicle = hasVehicle;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
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
            this.in_time = inDate + " : " +inTime;
        } catch (Exception e) {}
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setIn_gate(String in_gate) {
        this.in_gate = in_gate;
    }

    public void setOut_gate(String out_gate) {
        this.out_gate = out_gate;
    }
    /*public void setImage(String image) {
        this.image = image;
    }*/

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getIn_time() {
        return in_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public String getType() {
        return type;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getIn_gate() {
        return in_gate;
    }

    public String getOut_gate() {
        return out_gate;
    }

    public String getHasVehicle() {
        return hasVehicle;
    }

    public void setHasVehicle(String hasVehicle) {
        this.hasVehicle = hasVehicle;
    }
}
