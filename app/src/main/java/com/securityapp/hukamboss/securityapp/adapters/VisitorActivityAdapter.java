package com.securityapp.hukamboss.securityapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.securityapp.hukamboss.securityapp.R;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.interfaces.BaseActivityListener;
import com.securityapp.hukamboss.securityapp.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Samman on 27/02/18.
 */

public class VisitorActivityAdapter extends BaseActivityAdapter {
//    private final Context mContext;
//    private ArrayList<String> entries = new ArrayList<String>();
//    private Integer columns;
    // 1

    String resVisitorIn, socVisitorIn, resStaffVisitorIn, socStaffVisitorIn, resVisitorMatIn, socVisitorMatIn, resStaffVisitorMatIn, socStaffVisitorMatIn, resVisitorVehIn, socVisitorVehIn, resStaffVisitorVehIn, socStaffVisitorVehIn;
    String resVisitorOut, socVisitorOut, resStaffVisitorOut, socStaffVisitorOut, resVisitorMatOut, socVisitorMatOut, resStaffVisitorMatOut, socStaffVisitorMatOut, resVisitorVehOut, socVisitorVehOut, resStaffVisitorVehOut, socStaffVisitorVehOut;

    public VisitorActivityAdapter(Context context, BaseActivityListener listener, Integer nCol, JSONObject responseObj) {
        this.mContext = context;
        this.columns = nCol;
        this.baseActivityListener = listener;
        ArrayList<String> row0 = new ArrayList<>(Arrays.asList("Type",
                "Visitor",
                "Mat",
                "Veh",
                "Visitor",
                "Mat",
                "Veh",
                "Work Index"));

        if(responseObj == null) {

        } else  {
            try {
                resVisitorIn = responseObj.getString(Constants.RES_VISITOR_IN);
                socVisitorIn = responseObj.getString(Constants.SOC_VISITOR_IN);
                resStaffVisitorIn = responseObj.getString(Constants.RES_STAFF_VISITOR_IN);
                socStaffVisitorIn = responseObj.getString(Constants.SOC_STAFF_VISITOR_IN);
                resVisitorMatIn = responseObj.getString(Constants.RES_STAFF_VISITOR_MAT_IN);
                socVisitorMatIn = responseObj.getString(Constants.SOC_VISITOR_MAT_IN);
                resStaffVisitorMatIn = responseObj.getString(Constants.RES_STAFF_VISITOR_MAT_IN);
                socStaffVisitorMatIn = responseObj.getString(Constants.SOC_STAFF_VISITOR_MAT_IN);
                resVisitorVehIn = responseObj.getString(Constants.RES_VISITOR_VEH_IN);
                socVisitorVehIn = responseObj.getString(Constants.SOC_VISITOR_VEH_IN);
                resStaffVisitorVehIn = responseObj.getString(Constants.RES_STAFF_VISITOR_VEH_IN);
                socStaffVisitorVehIn = responseObj.getString(Constants.SOC_STAFF_VISITOR_VEH_IN);

                resVisitorOut= responseObj.getString(Constants.RES_VISITOR_OUT);
                socVisitorOut= responseObj.getString(Constants.SOC_VISITOR_OUT);
                resStaffVisitorOut= responseObj.getString(Constants.RES_STAFF_VISITOR_OUT);
                socStaffVisitorOut= responseObj.getString(Constants.SOC_STAFF_VISITOR_OUT);
                resVisitorMatOut= responseObj.getString(Constants.RES_STAFF_VISITOR_MAT_OUT);
                socVisitorMatOut= responseObj.getString(Constants.SOC_VISITOR_MAT_OUT);
                resStaffVisitorMatOut= responseObj.getString(Constants.RES_STAFF_VISITOR_MAT_OUT);
                socStaffVisitorMatOut= responseObj.getString(Constants.SOC_STAFF_VISITOR_MAT_OUT);
                resVisitorVehOut= responseObj.getString(Constants.RES_VISITOR_VEH_OUT);
                socVisitorVehOut= responseObj.getString(Constants.SOC_VISITOR_VEH_OUT);
                resStaffVisitorVehOut= responseObj.getString(Constants.RES_STAFF_VISITOR_VEH_OUT);
                socStaffVisitorVehOut= responseObj.getString(Constants.SOC_STAFF_VISITOR_VEH_OUT);

            } catch (JSONException e) { }
        }



        ArrayList<String> row1 = new ArrayList<>(Arrays.asList("Residence\nVisitor",
                resVisitorIn, resVisitorMatIn, resVisitorVehIn, resVisitorOut, resVisitorMatOut, resVisitorVehOut, "1"));

        ArrayList<String> row2 = new ArrayList<>(Arrays.asList("Society\nVisitor",
                socVisitorIn, socVisitorMatIn, socVisitorVehIn, socVisitorOut, socVisitorMatOut, socVisitorVehOut, "1"));

        ArrayList<String> row3 = new ArrayList<>(Arrays.asList("Residence\nStaff",
                resStaffVisitorIn, resStaffVisitorMatIn, resStaffVisitorVehIn, resStaffVisitorOut, resStaffVisitorMatOut, resStaffVisitorVehOut, "1"));

        ArrayList<String> row4 = new ArrayList<>(Arrays.asList("Society\nStaff",
                socStaffVisitorIn, socStaffVisitorMatIn, socStaffVisitorVehIn, socStaffVisitorOut, socStaffVisitorMatOut, socStaffVisitorVehOut, "1"));

        entries.addAll(row0);
        entries.addAll(row1);
        entries.addAll(row2);
        entries.addAll(row3);
        entries.addAll(row4);
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

    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.visitor_activity_grid, null);
        }
        if(position%2 ==0) {
            convertView.setBackgroundColor(Color.parseColor("#18A608"));
        }


        return convertView;
    }
*/
    // 5
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        int width = 120 ;
//        int height = 50;
//
//        float density = Utility.getDeviceScreenDensity(mContext);
//        Boolean shouldShowTitles = false;
//        if (position < columns) {
//            shouldShowTitles = true;
//        } else if (position % columns == 0) {
//            shouldShowTitles = true;
//        }
//
//        if (shouldShowTitles) {
//            int singleItemWidth = (int) (width * density);
//            int singleItemHeight = (int) (height * density);
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    singleItemWidth, singleItemHeight);
//
//            TextView dummyTextView = new TextView(mContext);
//            dummyTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            dummyTextView.setGravity(Gravity.CENTER_VERTICAL);
//            dummyTextView.setTypeface(null, Typeface.BOLD);
//            dummyTextView.setLayoutParams(params);
//            dummyTextView.setBackgroundColor(Color.GRAY);
//            dummyTextView.setTextColor(Color.WHITE);
//            dummyTextView.setTextSize(18);
//            dummyTextView.setText(entries.get(position));
//            return dummyTextView;
//        } else {
//            int singleItemWidth = (int) (width * density);
//            int singleItemHeight = (int) (height * density);
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    singleItemWidth, singleItemHeight);
//
//            Button dummyButton = new Button(mContext);
//            dummyButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            dummyButton.setGravity(Gravity.CENTER_VERTICAL);
//            dummyButton.setLayoutParams(params);
//            dummyButton.setBackgroundColor(Color.LTGRAY);
//            dummyButton.setText(entries.get(position));
//            return dummyButton;
//        }
//    }
}
