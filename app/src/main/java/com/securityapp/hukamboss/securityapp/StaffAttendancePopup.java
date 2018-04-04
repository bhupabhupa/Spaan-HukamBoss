package com.securityapp.hukamboss.securityapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.securityapp.hukamboss.securityapp.constants.Constants;

/**
 * Created by Bhupa on 12/03/18.
 */

public class StaffAttendancePopup  extends DialogFragment {
    private String accessToken;
    private String societyId;
    private String visitId;
    private String societyGateNo;

    private ProgressBar progressView;
    ImageView closeButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.staff_attendance_popup, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        //societyId = getArguments().getString(Constants.SOCIETY_ID);
        //visitId = getArguments().getString(Constants.VISIT_ID);
        //societyGateNo = getArguments().getString(Constants.SOCIETY_GATE_NO);


        Button scanQR = (Button) view.findViewById(R.id.scan_qr);
        scanQR.setOnClickListener(scanQRListener);

        Button fillDetails = (Button) view.findViewById(R.id.fill_details);
        fillDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        closeButton = view.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(closePopupListener);

        return view;
    }

    private View.OnClickListener scanQRListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.ACCESS_TOKEN, accessToken);
            bundle.putString(Constants.SOCIETY_ID, societyId);
            bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
            Intent intent = new Intent().putExtras(bundle);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            dismiss();

        }
    };

    private View.OnClickListener closePopupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };


}


