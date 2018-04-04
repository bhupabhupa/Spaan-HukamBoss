package com.securityapp.hukamboss.securityapp.adapters;

import android.content.Context;

import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.interfaces.BaseActivityListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Samman on 28/02/18.
 */

public class SecurityConcernsAdapter extends BaseActivityAdapter {
    String[] otpNotVerified, noIDCard, incidencesBypassed, incidenceRejected, visitor24Hrs, vehicle24Hrs, materiasl24Hrs, grievances;
    // 1
    public SecurityConcernsAdapter(Context context, BaseActivityListener listener, Integer nCol, JSONObject responseObj, String[] gateStr) {
        this.mContext = context;
        this.columns = nCol;
        this.baseActivityListener = listener;

        try {
            //Log.d("JSON 1", responseObj.getString(Constants.OTP_NOT_VERIFIED).toString());
            String temp = responseObj.getString(Constants.OTP_NOT_VERIFIED).toString();
            otpNotVerified = temp.split(",");

            temp = responseObj.getString(Constants.NO_ID_CARD).toString();
            noIDCard = temp.split(",");

            temp = responseObj.getString(Constants.INCIDENCE_BYPASSED).toString();
            incidencesBypassed = temp.split(",");

            temp = responseObj.getString(Constants.INCIDENCE_REJECTED).toString();
            incidenceRejected = temp.split(",");

            temp = responseObj.getString(Constants.VISITOR_24_HRS).toString();
            visitor24Hrs = temp.split(",");

            temp = responseObj.getString(Constants.MATERIALS_24_HRS).toString();
            materiasl24Hrs = temp.split(",");

            temp = responseObj.getString(Constants.VEHICLES_24_HRS).toString();
            vehicle24Hrs = temp.split(",");

            temp = responseObj.getString(Constants.GRIEVANCES).toString();
            grievances = temp.split(",");
        } catch (JSONException e) {

        }


        /*
        ArrayList<String> row0 = new ArrayList<>(Arrays.asList(" ",
                "All",
                "G1",
                "G2"));

        ArrayList<String> row1 = new ArrayList<>(Arrays.asList("Bypass OTP",
                "1", "2", "3"));

        ArrayList<String> row2 = new ArrayList<>(Arrays.asList("ID Card",
                "1", "2", "3"));

        ArrayList<String> row3 = new ArrayList<>(Arrays.asList("> Security Incidence",
                "", "", ""));

        ArrayList<String> row4 = new ArrayList<>(Arrays.asList("Past Ignored",
                "1", "2", "3"));

        ArrayList<String> row5 = new ArrayList<>(Arrays.asList("Reported",
                "1", "2", "3"));

        ArrayList<String> row6 = new ArrayList<>(Arrays.asList("> Not out in 24 hours",
                "", "", ""));

        ArrayList<String> row7 = new ArrayList<>(Arrays.asList("Visitor",
                "1", "2", "3"));

        ArrayList<String> row8 = new ArrayList<>(Arrays.asList("Material",
                "1", "2", "3"));

        ArrayList<String> row9 = new ArrayList<>(Arrays.asList("Vehicle",
                "1", "2", "3"));

        ArrayList<String> row10 = new ArrayList<>(Arrays.asList("Grievances",
                "1", "2", "3"));
        */

        //String[] gateNos = {"G1", "G2", "G3", "G4", "G5", "G6"};

        ArrayList<String> row0 = new ArrayList<>();
        row0.add("");
        row0.add("All");
        for (int i=1; i<gateStr.length; i++){
            row0.add(gateStr[i]);
        }
        /*for (String gate : gateStr) {
            row0.add(gate);
        }*/

        String[] values = {"1", "2", "3", "4", "5", "6", "7"};

        ArrayList<String> row1 = new ArrayList<>();
        row1.add("OTP Not Verified");

        for (String value: otpNotVerified) {
            row1.add(value);
        }

        ArrayList<String> row2 = new ArrayList<>();
        row2.add("NO ID Card");
        for (String value: noIDCard) {
            row2.add(value);
        }

        ArrayList<String> row3 = new ArrayList<>();
        row3.add("> Security Incidence");
        for (String value: otpNotVerified) {
            row3.add("");
        }

        ArrayList<String> row4 = new ArrayList<>();
        row4.add("Bypass Incidence");
        for (String value: incidencesBypassed) {
            row4.add(value);
        }

        ArrayList<String> row5 = new ArrayList<>();
        row5.add("Rejected Incidence");
        for (String value: incidenceRejected) {
            row5.add(value);
        }

        ArrayList<String> row6 = new ArrayList<>();
        row6.add("> Not out in 24 hours");
        for (String value: otpNotVerified) {
            row6.add("");
        }

        ArrayList<String> row7 = new ArrayList<>();
        row7.add("Visitor");
        for (String value: visitor24Hrs) {
            row7.add(value);
        }

        ArrayList<String> row8 = new ArrayList<>();
        row8.add("Material");
        for (String value: materiasl24Hrs) {
            row8.add(value);
        }

        ArrayList<String> row9 = new ArrayList<>();
        row9.add("Vehicle");
        for (String value: vehicle24Hrs) {
            row9.add(value);
        }

        ArrayList<String> row10 = new ArrayList<>();
        row10.add("Grievances");
        for (String value: grievances) {
            row10.add(value);
        }

        /*for (String value : values) {
            row1.add(value);
            row2.add(value);
            row3.add("");
            row4.add(value);
            row5.add(value);
            row6.add("");
            row7.add(value);
            row8.add(value);
            row9.add(value);
            row10.add(value);
        }*/

        entries.addAll(row0);
        entries.addAll(row1);
        entries.addAll(row2);
        entries.addAll(row3);
        entries.addAll(row4);
        entries.addAll(row5);
        entries.addAll(row6);
        entries.addAll(row7);
        entries.addAll(row8);
        entries.addAll(row9);
        entries.addAll(row10);
    }

    // 2
    @Override
    public int getCount() {
        return entries.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }
}
