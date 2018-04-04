package com.securityapp.hukamboss.securityapp;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.securityapp.hukamboss.securityapp.adapters.EmergencyDirectoryAdapter;
import com.securityapp.hukamboss.securityapp.adapters.PastIncidencesAdapter;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.model.EmergencyDirectoryModel;
import com.securityapp.hukamboss.securityapp.model.PastIncidenceModel;
import com.securityapp.hukamboss.securityapp.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bhupa on 23/02/18.
 */

public class EmergencyDirectoryPopup extends DialogFragment {

    private ListView listView = null;
    private ArrayList<EmergencyDirectoryModel> dataModels;
    private static EmergencyDirectoryAdapter adapter;

    private String accessToken;
    private String societyId;
    private String directoryType;

    private ProgressBar progressView;
    ImageView closeButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.emergency_directory_popup, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        directoryType = getArguments().getString(Constants.DIRECTORY_TYPE);

        progressView = view.findViewById(R.id.progress);


        listView = (ListView) view.findViewById(R.id.pi_list);

        getEmergencyDirectory(societyId, directoryType);

        closeButton = view.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(closePopupListener);

        /*Button close = (Button)view.findViewById(R.id.pi_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });*/

        return view;
    }

    public void populateEmergencyDirectory(JSONArray resourceList) {
        dataModels = new ArrayList<>();
        try {
            for (int i =0; i < resourceList.length(); i++) {
                JSONObject resource = resourceList.getJSONObject(i);
                String phoneNo = resource.getString(Constants.PHONE_NO);
                String name = resource.getString(Constants.NAME);//"ABCD";//
                String address = resource.getString(Constants.ADDRESS);//"PQRS";//

                dataModels.add(new EmergencyDirectoryModel(name, address, phoneNo));
            }
        } catch (Exception e) {}

        adapter = new EmergencyDirectoryAdapter(dataModels, getActivity().getApplicationContext());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EmergencyDirectoryModel dataModel= dataModels.get(position);
                displayDialerPhone(dataModel.getPhoneNo());

                Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getAddress(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }


    private void getEmergencyDirectory(String societyId, String directoryName) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);

        String URL = Constants.BASE_URL+"Sos/GetDirectory/?SocietyId="+societyId+"&DirectoryType="+directoryName;
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        popup.dismiss();
                        //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                        try {
                            JSONArray resourceList = new JSONArray(s);

                            if(resourceList.length() == 0) {
                                //incidenceText = "No incidence associated with the member";
                            } else  {
                                populateEmergencyDirectory(resourceList);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //progress.setVisibility(View.GONE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                VolleyLog.d("responseError", "Error: " + volleyError);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };

        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
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

    public void displayDialerPhone(String mobileNo) {
        FragmentManager fm = getFragmentManager();
        DialerPopup popup = new DialerPopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.MOBILE_NO, mobileNo);

        popup.setArguments(bundle);
        popup.show(fm, "Call");
    }
}
