package com.securityapp.hukamboss.securityapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.securityapp.hukamboss.securityapp.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bhupa on 13/02/18.
 */

public class VerifyOTPPopup extends DialogFragment {

    Button verifyOTP;
    EditText editOTP;
    ImageView closeButton;
    TextView otpError;

    boolean otpVerified;
    String accessToken, mobileNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_otp_popup, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        mobileNo = getArguments().getString(Constants.VISITOR_MOBILE_NO);

        editOTP = view.findViewById(R.id.edit_otp);
        otpError = view.findViewById(R.id.otp_error);
        otpError.setVisibility(View.GONE);

        verifyOTP = view.findViewById(R.id.verify_otp);
        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editOTP.getText().toString().trim().length() == 0){
                    //editOTP.setError("Enter OTP");
                    editOTP.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                    otpError.setText("Please enter OTP");
                    otpError.setVisibility(View.VISIBLE);
                } else {
                    /*Bundle bundle = new Bundle();
                    bundle.putString("ENTERED_OTP", editOTP.getText().toString());
                    Intent intent = new Intent().putExtras(bundle);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    dismiss();
                    */
                    checkOTP(mobileNo, editOTP.getText().toString());
                }


            }
        });
        /*verifyOTP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    editOTP.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    otpError.setVisibility(View.GONE);
                }
            }
        });*/

        closeButton = view.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(closePopupListener);

        return view;
    }

    private View.OnClickListener closePopupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private void checkOTP(final String mobileNo, final String OTP) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_URL + "/Visitorotp/?MobileNo=" + mobileNo + "&OtpNumber=" + OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //showProgress(false);
                        //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                        popup.dismiss();
                        try {
                            Log.d("SUCCESS OTP", s);

                            try {
                                JSONObject object = new JSONObject(s);
                                int totalRes = object.getInt("TotalResults");
                                if (totalRes > 0) {
                                    //vhfModel.setOTPVerified(true);
                                    otpVerified = true;
                                    dismiss();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constants.IS_OTP_VERIFIED, ""+otpVerified);
                                    Intent intent = new Intent().putExtras(bundle);
                                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                                    dismiss();
                                    //Toast.makeText(getActivity().getApplicationContext(), "Correct OTP", 5000).show();
                                } else {
                                    //vhfModel.setOTPVerified(false);
                                    otpVerified = false;
                                    otpError.setText("Wrong OTP");
                                    otpError.setVisibility(View.VISIBLE);
                                    //Toast.makeText(getActivity().getApplicationContext(), "Wrong OTP entry", 5000).show();
                                }
                            } catch (JSONException e) {

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //progress.setVisibility(View.GONE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // progressDialog.dismiss();
                //progress.setVisibility(View.GONE);
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                VolleyLog.d("responseError", "Error: " + volleyError);
                popup.dismiss();
                //Toast.makeText(getActivity().getApplicationContext(), "Wrong OTP", 5000).show();
                //showProgress(false);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };

        //showProgress(true);
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }



}
