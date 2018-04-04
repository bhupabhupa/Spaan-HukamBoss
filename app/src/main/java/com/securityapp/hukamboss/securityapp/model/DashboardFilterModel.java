package com.securityapp.hukamboss.securityapp.model;

/**
 * Created by Bhupa on 23/03/18.
 */

public class DashboardFilterModel {
    private String type;
    private String gateNo;
    private String securityPerson;
    private String filterDays;
    private boolean societyPurpose;
    private boolean visitorIn;
    private boolean isOTPVerified;
    private boolean isVisitRejected;
    private String visitor24Hrs;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGateNo() {
        return gateNo;
    }

    public void setGateNo(String gateNo) {
        this.gateNo = gateNo;
    }

    public String getSecurityPerson() {
        return securityPerson;
    }

    public void setSecurityPerson(String securityPerson) {
        this.securityPerson = securityPerson;
    }

    public String getFilterDays() {
        return filterDays;
    }

    public void setFilterDays(String filterDays) {
        this.filterDays = filterDays;
    }

    public boolean isSocietyPurpose() {
        return societyPurpose;
    }

    public void setSocietyPurpose(boolean societyPurpose) {
        this.societyPurpose = societyPurpose;
    }

    public boolean isVisitorIn() {
        return visitorIn;
    }

    public void setVisitorIn(boolean visitorIn) {
        this.visitorIn = visitorIn;
    }

    public boolean isOTPVerified() {
        return isOTPVerified;
    }

    public void setOTPVerified(boolean OTPVerified) {
        isOTPVerified = OTPVerified;
    }

    public boolean isVisitRejected() {
        return isVisitRejected;
    }

    public void setVisitRejected(boolean visitRejected) {
        isVisitRejected = visitRejected;
    }

    public String getVisitor24Hrs() {
        return visitor24Hrs;
    }

    public void setVisitor24Hrs(String visitor24Hrs) {
        this.visitor24Hrs = visitor24Hrs;
    }
}
