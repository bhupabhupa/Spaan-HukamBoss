package com.securityapp.hukamboss.securityapp.adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.securityapp.hukamboss.securityapp.R;
import com.securityapp.hukamboss.securityapp.VehicleExitPopup;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.model.VehicleModel;

import java.util.ArrayList;

/**
 * Created by Bhupa on 14/03/18.
 */

public class VehicleListAdapter extends ArrayAdapter<VehicleModel> {

    String accessToken, societyId, societyGateNo;
    private ArrayList<VehicleModel> dataSet;
    Context mContext;
    FragmentManager fragment;
    private ArrayList<VehicleModel> originalDataSet;

    private int lastPosition = -1;
    Button outBtn;

    public VehicleListAdapter(ArrayList<VehicleModel> data, Context context, FragmentManager fragment, String accessToken, String societyId, String societyGateNo) {
        super(context, R.layout.vehicle_list, data);
        this.dataSet = data;
        this.mContext = context;
        this.fragment = fragment;
        this.accessToken = accessToken;
        this.societyId = societyId;
        this.societyGateNo = societyGateNo;
        originalDataSet = new ArrayList<VehicleModel>();
        originalDataSet.addAll(data);
    }

    private static class ViewHolder {
        TextView visitID, vehicleType, vehicleNo, noOfVehicles, inDate;
        String visitIDStr;
        Button outBtn;
        LinearLayout parentView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        VehicleModel dataModel = getItem(position);

        VehicleListAdapter.ViewHolder viewHolder = null;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.vehicle_list, parent, false);

            viewHolder.parentView = convertView.findViewById(R.id.parentView);

            //StaffModel visObj = dataSet.get(position);
            viewHolder.visitID = (TextView) convertView.findViewById(R.id.visit_id);
            viewHolder.vehicleNo = (TextView) convertView.findViewById(R.id.vehicle_no);
            viewHolder.vehicleType = (TextView) convertView.findViewById(R.id.vehicle_type);
            viewHolder.noOfVehicles = (TextView) convertView.findViewById(R.id.no_of_vehicles);
            viewHolder.inDate = (TextView) convertView.findViewById(R.id.in_date);

            outBtn = convertView.findViewById(R.id.out_button);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (VehicleListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        VehicleModel visObj = dataSet.get(position);

        viewHolder.visitID.setText(dataModel.getVisitID());
        viewHolder.vehicleType.setText(dataModel.getVehicleType());
        viewHolder.vehicleNo.setText(dataModel.getVehicleNo());
        viewHolder.noOfVehicles.setText(dataModel.getNoOfVehicles());
        viewHolder.inDate.setText(dataModel.getInDate());

        final String visitIDStr = dataModel.getVisitID();

        outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayVehicleExitPopup(visitIDStr);
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    private void displayVehicleExitPopup(String visitID) {
        VehicleExitPopup popup = new VehicleExitPopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.VISIT_ID, visitID);
        bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

        popup.setArguments(bundle);
        popup.setCancelable(false);
        popup.show(fragment, "Vehicle Exit");
    }
}
