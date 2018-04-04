package com.securityapp.hukamboss.securityapp.model;

/**
 * Created by Samman on 05/02/18.
 */

public class PastIncidenceModel {
    String date, time, flat, description;

    public PastIncidenceModel(String date, String time, String flat, String description) {
        this.date = date;
        this.time = time;
        this.flat = flat;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getFlat() {
        return flat;
    }

    public String getDescription() {
        return description;
    }
}
