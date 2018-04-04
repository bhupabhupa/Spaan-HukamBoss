package com.securityapp.hukamboss.securityapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.interfaces.BaseActivityListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Samman on 28/02/18.
 */

public class SecurityAttendanceAdapter extends BaseActivityAdapter {
    // 1
    String[] presentStr, absentStr, proxyStr;
    public SecurityAttendanceAdapter(Context context, BaseActivityListener listener, Integer nCol, JSONObject responseObj, String[] gateStr) {
        this.mContext = context;
        this.columns = nCol;
        this.baseActivityListener = listener;

        try {
            //Log.d("JSON 1", responseObj.getString(Constants.OTP_NOT_VERIFIED).toString());
            String temp = responseObj.getString(Constants.PRESENT).toString();
            presentStr = temp.split(",");

            temp = responseObj.getString(Constants.ABSENT).toString();
            absentStr = temp.split(",");

            temp = responseObj.getString(Constants.PROXY).toString();
            proxyStr = temp.split(",");

        } catch (JSONException e) {

        }

        ArrayList<String> row0 = new ArrayList<>();
        row0.add("");
        row0.add("All");
        for (int i=1; i<gateStr.length; i++){
            row0.add(gateStr[i]);
        }

        ArrayList<String> row1 = new ArrayList<>();
        row1.add("Present");

        for (String value: presentStr) {
            row1.add(value);
        }

        ArrayList<String> row2 = new ArrayList<>();
        row2.add("Absent");

        for (String value: absentStr) {
            row2.add(value);
        }

        ArrayList<String> row3 = new ArrayList<>();
        row3.add("Proxy");

        for (String value: proxyStr) {
            row3.add(value);
        }

        entries.addAll(row0);
        entries.addAll(row1);
        entries.addAll(row2);
        entries.addAll(row3);
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
