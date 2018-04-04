package com.securityapp.hukamboss.securityapp;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Bhupa on 14/03/18.
 */

public class VehicleExitPopup extends DialogFragment {
    private String accessToken;
    private String societyId;
    private String visitID;
    private String societyGateNo;

    ImageView closeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.vehicle_exit_popup, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        visitID = getArguments().getString(Constants.VISIT_ID);
        societyGateNo = getArguments().getString(Constants.SOCIETY_GATE_NO);


        Button yesButton = (Button) view.findViewById(R.id.yes);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.SOCIETY_GATE_NO, societyId);
                params.put(Constants.VISIT_ID, visitID);
                params.put(Constants.SOCIETY_GATE_NO, societyGateNo);
                //vehicleExitFromServer(params, visitID);
                closeVehicleAtServer(visitID);
                dismiss();
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



    private void closeVehicleAtServer(final String visitId) {
        final FragmentManager fm = getFragmentManager();
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
                        //Toast.makeText(getActivity().getApplicationContext(), "Vehicle exit successful", 5000).show();
                        dismiss();
                        final Bundle bundle = new Bundle();
                        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                        bundle.putString(Constants.SOCIETY_ID, societyId);
                        bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
                        VisitorReportFragment reportFrag = new VisitorReportFragment();
                        reportFrag.setArguments(bundle);

                        //FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.rootLayout,reportFrag);
                        ft.commit();

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

    private void vehicleExitFromServer(final Map<String, String>myParams,String visitID) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.PUT,Constants.BASE_URL+"DomesticStaffSubscription"+"/"+visitID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String message = object.getString(Constants.MESSAGE_INFO);
                            Toast.makeText(getActivity().getApplicationContext(), message, 5000).show();
                            popup.dismiss();
                            dismiss();

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


                        } catch (JSONException e) {}
                        //Log.d("RESPONSE SUCCESS - ", "" + s);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Toast.makeText(getApplicationContext(), myParams.toString(), 5000).show();
                Log.d("RESPONSE ERROR", volleyError.toString());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return myParams;
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



