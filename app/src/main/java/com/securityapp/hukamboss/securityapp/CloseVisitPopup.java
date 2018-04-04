package com.securityapp.hukamboss.securityapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.securityapp.hukamboss.securityapp.constants.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bhupa on 21/02/18.
 */

public class CloseVisitPopup extends DialogFragment {
    private String accessToken;
    private String societyId;
    private String visitId;
    private String societyGateNo;
    private String hasVehicle;

    private ProgressBar progressView;
    ImageView closeButton;
    CheckBox vehicleOut;

    boolean isVehicleOut;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.close_visit_popup, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        visitId = getArguments().getString(Constants.VISIT_ID);
        societyGateNo = getArguments().getString(Constants.SOCIETY_GATE_NO);
        hasVehicle = getArguments().getString(Constants.HAS_VEHICLE);

        progressView = view.findViewById(R.id.progress);

        vehicleOut = view.findViewById(R.id.vehicle_out);
        if(hasVehicle.equalsIgnoreCase("TRUE")) {
            vehicleOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    if (checked) {
                        isVehicleOut = true;
                    } else {
                        isVehicleOut = false;
                    }
                }
            });
        } else {
            vehicleOut.setVisibility(View.GONE);
        }


        Button yesButton = (Button) view.findViewById(R.id.yes);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeVisitAtServer(visitId);
            }
        });

        Button noButton = (Button) view.findViewById(R.id.no);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        closeButton = view.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(closePopupListener);

        return view;
    }

    private void closeVisitAtServer(final String visitId) {
        //First check whether there is any incidence comment reported.....
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        String baseURL = "http://103.48.51.62:8018/Visits/";
        String URL = baseURL + visitId;
        StringRequest request = new StringRequest(Request.Method.PUT, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        VolleyLog.d("response", "Success: " + s);
                        popup.dismiss();
                        if((hasVehicle.equalsIgnoreCase("TRUE")) && isVehicleOut) {
                            closeVehicleAtServer(visitId);
                        } else {
                            final Bundle bundle = new Bundle();
                            bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                            bundle.putString(Constants.SOCIETY_ID, societyId);
                            bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
                            VisitorReportFragment reportFrag = new VisitorReportFragment();
                            reportFrag.setArguments(bundle);

                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.rootLayout,reportFrag);
                            ft.commit();

                            dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("responseError", "Error: " + volleyError);
                popup.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("IsVisitClose", "true");
                return params;
            }
        };
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    private void closeVehicleAtServer(final String visitId) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        //String baseURL = "http://103.48.51.62:8018/Vehicles";
        String URL = Constants.BASE_URL + "Vehicles/" + visitId;
        StringRequest request = new StringRequest(Request.Method.PUT, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        VolleyLog.d("response", "Success: " + s);
                        popup.dismiss();
                        final Bundle bundle = new Bundle();
                        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                        bundle.putString(Constants.SOCIETY_ID, societyId);
                        bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
                        VisitorReportFragment reportFrag = new VisitorReportFragment();
                        reportFrag.setArguments(bundle);

                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.rootLayout,reportFrag);
                        ft.commit();

                        dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("responseError", "Error: " + volleyError);
                popup.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("IsVisitClose", "true");
                params.put(Constants.VISIT_ID, visitId);
                return params;
            }
        };
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private View.OnClickListener closePopupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };


}

