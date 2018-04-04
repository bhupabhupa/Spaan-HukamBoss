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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bhupa on 27/02/18.
 */

public class StaffScanPopup extends DialogFragment {
    private String accessToken;
    private String societyId;
    private String societyGateNo;
    private String scanData;
    String name;
    String telephone;
    String id;
    String title;

    private ProgressBar progressView;
    ImageView closeButton;
    TextView scanDataText;

    Button inButton, outButton, rescan;
    NetworkImageView photo;
    ImageLoader mimageLoader = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.staff_scan_popup, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        societyGateNo = getArguments().getString(Constants.SOCIETY_GATE_NO);
        //scanData = getArguments().getString(Constants.SCAN_DATA);
        name = getArguments().getString(Constants.NAME);
        title = getArguments().getString(Constants.DESIGNATION);
        telephone = getArguments().getString(Constants.MOBILE_NO);
        id = getArguments().getString(Constants.ID);


        progressView = view.findViewById(R.id.progress);
        mimageLoader = VolleyController.getInstance(getActivity().getApplicationContext()).getImageLoader();

        photo = view.findViewById(R.id.photo);
//            resultPic = view.findViewById(R.id.resultPic);
//            resultPic.setVisibility(View.GONE);
        //photo.setDefaultImageResId(R.drawable.ic_menu_camera);

        String photoPath = Constants.BASE_URL + "Staffimages/" + id + ".jpeg";
        photo.setImageUrl(photoPath, mimageLoader);

        scanDataText = view.findViewById(R.id.scan_data);
        scanDataText.setText("Staff ID : "+ id +"\n" +"Name : " + name + "\n" + "Designation : " + title + "\n" + "Mobile : " + telephone);

        closeButton = view.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(closePopupListener);

        inButton = view.findViewById(R.id.in_button);
        outButton = view.findViewById(R.id.out_button);
        rescan = view.findViewById(R.id.rescan);

        inButton.setOnClickListener(inBtnListener);
        outButton.setOnClickListener(outBtnListener);
        rescan.setOnClickListener(closePopupListener);

        /*
        rescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                bundle.putString(Constants.SOCIETY_ID, societyId);
                bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
                Intent intent = new Intent().putExtras(bundle);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });
        */

        return view;
    }

    private View.OnClickListener closePopupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //dismiss();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.ACCESS_TOKEN, accessToken);
            bundle.putString(Constants.SOCIETY_ID, societyId);
            bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
            Intent intent = new Intent().putExtras(bundle);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, intent);
            dismiss();

        }
    };

    private View.OnClickListener inBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //dismiss();
            Map<String, String> params = new HashMap<>();
            params.put(Constants.SOCIETY_ID, societyId);
            params.put(Constants.SOCIETY_GATE_NO, societyId);
            params.put(Constants.STAFF_ID, id);
            params.put(Constants.STAFF_IN, ""+true);
            sendInData(params);

        }
    };

    private View.OnClickListener outBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //dismiss();
            Map<String, String> params = new HashMap<>();
            //params.put(Constants.SOCIETY_ID, societyId);
            params.put(Constants.SOCIETY_GATE_NO, societyId);
            params.put(Constants.STAFF_ID, id);
            params.put(Constants.STAFF_OUT, ""+true);
            sendOutData(params);

        }
    };

    private void sendInData(final Map<String, String> myParams) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST,Constants.BASE_URL+"DomesticStaffSubscription",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            //Log.d("RESPONSE SUCCESS - ", "" + s);
                            String message = object.getString(Constants.MESSAGE_INFO);
                            popup.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(), message, 5000).show();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                            bundle.putString(Constants.SOCIETY_ID, societyId);
                            bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
                            Intent intent = new Intent().putExtras(bundle);
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                            dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
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

        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }

    private void sendOutData(final Map<String, String> myParams) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.PUT,Constants.BASE_URL+"DomesticStaffSubscription"+"/"+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String message = object.getString(Constants.MESSAGE_INFO);
                            popup.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(), message, 5000).show();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                            bundle.putString(Constants.SOCIETY_ID, societyId);
                            bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
                            Intent intent = new Intent().putExtras(bundle);
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                            dismiss();

                        } catch (JSONException e) {}
                        //Log.d("RESPONSE SUCCESS - ", "" + s);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
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

        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }
}
