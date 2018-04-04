package com.securityapp.hukamboss.securityapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class SOSFragment extends Fragment {
    String emergencyType = "";
    String action;

    RadioButton btn1, btn2, btn3, btn4;
    EditText otherText;
    TextInputLayout otherHint;
    CheckBox evacuate, noLift, stayIndoor;
    boolean isEvacuated, isNoLift, isStayIndoor;
    String accessToken;
    String societyId;
    ProgressBar progressView;
    ScrollView parentScroll;
    Map<String, String> params = new HashMap<String, String>();
    Button submitButton;
    ImageView hospital, fireBrigade, bloodBank, police;
    TextView errorMsg1, errorMsg2;
    ArrayList<String> actionTaken = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sos, container, false);

        progressView = view.findViewById(R.id.progress);
        parentScroll = view.findViewById(R.id.parent_scroll);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);


        btn1 = view.findViewById(R.id.radio1_1);
        btn2 = view.findViewById(R.id.radio1_2);
        btn3 = view.findViewById(R.id.radio1_3);
        btn4 = view.findViewById(R.id.radio1_4);
        otherText = view.findViewById(R.id.other_text);
        otherText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        otherText.setVisibility(View.GONE);
        otherHint = view.findViewById(R.id.other_hint);
        otherHint.setVisibility(View.GONE);
        btn1.setOnClickListener(radioButtonListener);
        btn2.setOnClickListener(radioButtonListener);
        btn3.setOnClickListener(radioButtonListener);
        btn4.setOnClickListener(radioButtonListener);

        evacuate = view.findViewById(R.id.radio2_1);
        noLift = view.findViewById(R.id.radio2_2);
        stayIndoor = view.findViewById(R.id.radio2_3);

        evacuate.setOnClickListener(evacuateListener);
        noLift.setOnClickListener(noLiftListener);
        stayIndoor.setOnClickListener(stayIndoorListener);

        errorMsg1 = view.findViewById(R.id.error_msg1);
        errorMsg1.setVisibility(View.GONE);

        errorMsg2 = view.findViewById(R.id.error_msg2);
        errorMsg2.setVisibility(View.GONE);

        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(submitButtonListener);


        hospital = view.findViewById(R.id.hospital);
        fireBrigade = view.findViewById(R.id.fire_brigade);
        bloodBank = view.findViewById(R.id.blood_bank);
        police = view.findViewById(R.id.police);

        emergencyDirectoryClicked(hospital, Constants.HOSPITAL);
        emergencyDirectoryClicked(fireBrigade, Constants.FIRE_BRIGADE);
        emergencyDirectoryClicked(bloodBank, Constants.BLOOD_BANK);
        emergencyDirectoryClicked(police, Constants.POLICE);

        return view;
    }


    public View.OnClickListener radioButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            errorMsg1.setVisibility(View.GONE);
            boolean checked = ((RadioButton) view).isChecked();
            /*RadioButton btn1 = view.findViewById(R.id.radio1_1);
            RadioButton btn2 = view.findViewById(R.id.radio1_2);
            RadioButton btn3 = view.findViewById(R.id.radio1_3);
            RadioButton btn4 = view.findViewById(R.id.radio1_4);*/

            // Check which radio button was clicked
            switch (view.getId()) {
                case R.id.radio1_1:
                    if (checked) {
                        emergencyType = "Fire";
                        btn2.setChecked(false);
                        btn3.setChecked(false);
                        btn4.setChecked(false);
                        otherText.setVisibility(View.GONE);
                        otherHint.setVisibility(View.GONE);
                    }
                    break;
                case R.id.radio1_2:
                    if (checked) {
                        emergencyType = "Natural Disaster";
                        btn1.setChecked(false);
                        btn3.setChecked(false);
                        btn4.setChecked(false);
                        otherText.setVisibility(View.GONE);
                        otherHint.setVisibility(View.GONE);
                    }
                    break;
                case R.id.radio1_3:
                    if (checked) {
                        emergencyType = "Thief";
                        btn1.setChecked(false);
                        btn2.setChecked(false);
                        btn4.setChecked(false);
                        otherText.setVisibility(View.GONE);
                        otherHint.setVisibility(View.GONE);
                    }
                    break;
                case R.id.radio1_4:
                    if (checked) {
                        emergencyType = otherText.getEditableText().toString();
                        btn1.setChecked(false);
                        btn2.setChecked(false);
                        btn3.setChecked(false);
                        otherText.setVisibility(View.VISIBLE);
                        otherHint.setVisibility(View.VISIBLE);
                    }
                    break;
            }

        }
    };

    public View.OnClickListener evacuateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean checked = ((CheckBox) v).isChecked();
            if (checked) {
                actionTaken.add("Evacuate");
                errorMsg2.setVisibility(View.GONE);
                isEvacuated = true;
            } else {
                removeElementIfPresent("Evacuate");
                isEvacuated = false;
            }
        }
    };

    public View.OnClickListener noLiftListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean checked = ((CheckBox) v).isChecked();
            if (checked) {
                actionTaken.add("Do not use lift");
                errorMsg2.setVisibility(View.GONE);
                isNoLift = true;
            } else {
                removeElementIfPresent("Do not use lift");
                //checkAndShowError();
                isNoLift = false;
            }
        }
    };

    public View.OnClickListener stayIndoorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean checked = ((CheckBox) v).isChecked();
            if (checked) {
                actionTaken.add("Stay Indoor");
                errorMsg2.setVisibility(View.GONE);
                isStayIndoor = true;
            } else {
                removeElementIfPresent("Stay Indoor");
                isStayIndoor = false;
            }
        }
    };

    /*
    private void setOnClick(final CheckBox btn, final String str){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CHECKED", str);
            }
        });
    }
    */

    private void validateSOS() {
        /*ArrayList<String> actionTaken = new ArrayList<>();
        if(isEvacuated) {
            actionTaken.add("Evacuate");
        }
        if(isNoLift) {
            actionTaken.add("Do not use lift");
        }
        if(isStayIndoor) {
            actionTaken.add("Stay Indoor");
        }
        String actionStr = "";
        for (int i = 0; i < actionTaken.size(); i++) {
            if(i>0) {
                actionStr += ",";
            }
            actionStr += actionTaken.get(i);
        }
        */
        //Log.d("SUGGESTED_ACTION", actionStr);
        //params.put(Constants.SUGGESTED_ACTION, actionStr);
        params.put(Constants.SOCIETY_ID, societyId);
        params.put(Constants.TYPE_OF_EMERGENCY, emergencyType);
        boolean isValidData = true;

        String errorStr = "";//"Please select emergency and suggest action";
        if(!btn1.isChecked()
                && !btn2.isChecked()
                && !btn3.isChecked()
                && !btn4.isChecked()) {
            isValidData = false;
            errorStr = "Please select emergency";
            errorMsg1.setText(errorStr);
            errorMsg1.setVisibility(View.VISIBLE);
        }
        if(btn4.isChecked() && otherText.getEditableText().toString().trim().length() == 0) {
            isValidData = false;
            errorStr = "Other field cannot be blank";
            errorMsg1.setText(errorStr);
            errorMsg1.setVisibility(View.VISIBLE);
        }
        if(actionTaken.size() == 0) {
            isValidData = false;
            errorStr = "Please suggest action";
            errorMsg2.setText(errorStr);
            errorMsg2.setVisibility(View.VISIBLE);
        }

        /*if(emergencyType.trim().length() == 0 && actionTaken.size() == 0 && !btn4.isChecked()) {
            errorStr = "Please select emergency and suggest action";
        }*/

        if(isValidData) {
            displayAnnouncePopup();
        }
        /*else {
            errorMsg.setText(errorStr);
            errorMsg.setVisibility(View.VISIBLE);
        }*/

        //saveSOS();

        //AnnounceEmergencyPopup
    }

    private void saveSOS() {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        final String URL = Constants.BASE_URL + "SosEmergency";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("RESPONSE SUCCESS", s.toString());
                        popup.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Emergency Announced!!", 5000).show();
                        final Bundle bundle = new Bundle();
                        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                        bundle.putString(Constants.SOCIETY_ID, societyId);


                        //VisitorHomeActivity homeActivity = new VisitorHomeActivity();
                        //homeActivity.setupNavigationView(0);

                        Intent myIntent = new Intent(getActivity(), VisitorHomeActivity.class);
                        myIntent.putExtras(bundle);
                        getActivity().startActivity(myIntent);

                        /*
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

                        VisitorHomeFragment homeFragment = new VisitorHomeFragment();
                        homeFragment.setArguments(bundle);
                        ft.replace(R.id.rootLayout, homeFragment);
                        ft.commit();
                        */
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView, parentScroll);
                popup.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), "Error while announcing emergency", 5000).show();
                VolleyLog.d("responseError", "Error: " + volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> myParams = params;
                return myParams;
            }

        };
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView, parentScroll);
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    public View.OnClickListener submitButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            validateSOS();
        }
    };


    private void emergencyDirectoryClicked(final ImageView imageView, final String directoryName){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEmergencyDirectory(directoryName);
            }
        });
    }



    public void displayEmergencyDirectory(String directoryName) {
        FragmentManager fm = getFragmentManager();
        EmergencyDirectoryPopup popup = new EmergencyDirectoryPopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.DIRECTORY_TYPE, directoryName);

        popup.setArguments(bundle);
        popup.show(fm, "EMERGENCY");
    }


    public void displayAnnouncePopup() {
        final Bundle bundle = new Bundle();
        //bundle.putString(Constants.ACCESS_TOKEN, accessToken);

        FragmentManager fm = getFragmentManager();
        AnnounceEmergencyPopup popup = new AnnounceEmergencyPopup();
        popup.setArguments(bundle);
        popup.setTargetFragment(this, Constants.ANNOUNCE_POPUP);
        popup.setCancelable(false);
        popup.show(fm, "Emergency Announcement");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ANNOUNCE_POPUP && resultCode == Activity.RESULT_OK) {
            saveSOS();
        }
    }

    private void removeElementIfPresent(String value) {
        for(int i =0; i < actionTaken.size(); i++) {
            if(actionTaken.get(i).equalsIgnoreCase(value)) {
                actionTaken.remove(i);
            }
        }
        checkAndShowError();
    }

    private void checkAndShowError() {
        if(actionTaken.size() == 0) {
            errorMsg2.setVisibility(View.VISIBLE);
        }
    }


}
