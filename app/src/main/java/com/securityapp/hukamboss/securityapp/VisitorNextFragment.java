package com.securityapp.hukamboss.securityapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.model.VisitorHomeFragmentModel;
import com.securityapp.hukamboss.securityapp.utils.DataPart;
import com.securityapp.hukamboss.securityapp.utils.Utility;
import com.securityapp.hukamboss.securityapp.utils.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VisitorNextFragment extends Fragment {
    VisitorHomeFragmentModel vhfModel;
    //String flatNo, residentName, vehicleNo, vehicleType, vehicleCount, narration, proofOfID, idNo;
    //boolean isValidResidenceNo = true;
    boolean reqMaterial = false;
    //boolean notifyByPhone = false;
    EditText edit1_1,edit1_2,edit1_3,edit1_4,edit1_8,edit1_9,edit1_10, vehicleNo,edit3_1,edit3_3, narration, govIdNo;
    Spinner vehicleType, govtId, vehicle_count;
    //Spinner pastSecurityIncidences;
    //AutoCompleteTextView auto3_2;
    MultiAutoCompleteTextView unitNo, societyOffice;
    LinearLayout panel4;

    TextInputLayout societyLayout, unitNoLayout, residentNameLayout;

    CheckBox notify, material;

    HashMap<String, String> flatmember = new HashMap<String, String>();
    HashMap<String, String> flatmemberNameId = new HashMap<String, String>();

    ArrayList<String> societyBearerList = new ArrayList<String>();
    ArrayList<String> societyMemberIdList = new ArrayList<String>();
    String societyBearerStr = "";
    String societyMemberIdStr = "";
    HashMap<String, String> societyBearer = new HashMap<String, String>();
    HashMap<String, String> societyBearerId = new HashMap<String, String>();

    ArrayList<String> flatNoList = new ArrayList<String>();
    ArrayList<String> tempFlatNo = new ArrayList<String>();

    Button submitButton, backButton, materialButton;
    Bitmap visitorPic;

    ProgressBar progressView;
    ScrollView parentScroll;

    //boolean forSocietyVisit = false;

    String accessToken;
    String societyId;
    String societyGateNo;
    //String gateNo, visitorMobile, visitorName, noOfVisitors, visitorEmail, visitorPincode, visitorAddress, visitorPurpose, visitorType, orgName, picPath;

    private static final int RESIDENT_REQUEST_CODE = 0;
    private String resultResName = "";
    String residentId = "";
    //HashMap<String, String> flatNo_ResName = new HashMap<String, String>();
    //HashMap<String, String> flatNo_MemId = new HashMap<String, String>();
    //ArrayList<String> selectedFlatNo = new ArrayList<String>();
    int uploadCount = 0;

    Map<String, String> params = new HashMap<String, String>();

    String[] vehicleTypeList = new String[]{"-- Select Vehicle Type --", "2 wheeler", "4 wheeler"};
    String[] vehicleCountList = new String[]{"0","1","2","3","4","5"};
    String[] proofTypeList = new String[]{"-- Select Proof Of ID --", "PAN", "AADHAR", "Voter ID", "Driving License"};

    TextView unitNoError;
    String unitNoErrorTxt = "Unit No cannot be blank";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_visitor_next, container, false);

        //String textV = getArguments().getString("Name");

        //TextView tv = view.findViewById(R.id.text);
        //tv.setText(textV);
        /*
        String picturePath = getArguments().getString("image");
        Bitmap bmp = null;
        if (picturePath != null) {
            bmp = Utility.getImageFromPath(picturePath);
        }
        */

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        societyGateNo = getArguments().getString(Constants.SOCIETY_GATE_NO);

        vhfModel = (VisitorHomeFragmentModel) getArguments().getSerializable(Constants.VHFMODEL);

        uploadCount = 0;


        /*
        gateNo = getArguments().getString(Constants.GATE_NO, gateNo);
        visitorMobile  = getArguments().getString(Constants.VISITOR_MOBILE_NO);
        visitorName = getArguments().getString(Constants.VISITOR_NAME);
        noOfVisitors  = getArguments().getString(Constants.NO_OF_VISTORS);
        //visitorEmail = getArguments().getString(Constants.VISITOR_EMAIL);
        //visitorPincode = getArguments().getString(Constants.VISITOR_PINCODE);
        visitorAddress = getArguments().getString(Constants.VISITOR_ADDRESS);
        //visitorPurpose = getArguments().getString(Constants.PURPOSE_OF_VISIT);
        visitorType = getArguments().getString(Constants.VISITOR_TYPE);
        orgName = getArguments().getString(Constants.ORGANIZATION_NAME);
        picPath = getArguments().getString(Constants.VISITOR_PHOTO_PATH);
        forSocietyVisit = getArguments().getBoolean(Constants.SOCIETY_PURPOSE);

        visitorPic = null;
        if (picPath != null) {
            visitorPic = Utility.getImageFromPath(picPath);
        }
        */
        progressView = view.findViewById(R.id.progress);
        parentScroll = view.findViewById(R.id.parent_scroll);

        panel4 = view.findViewById(R.id.panel4);


        /*
        if (getArguments().getBoolean(Constants.VISITOR_HAS_MOBILE))
        {
            sendVisitorOTP(accessToken, visitorMobile);
        }
        */



        societyLayout = view.findViewById(R.id.society_layout);
        unitNoLayout = view.findViewById(R.id.unit_no_layout);
        residentNameLayout = view.findViewById(R.id.resident_name_layout);

        edit3_1 = view.findViewById(R.id.edit3_1);
        edit3_1.setEnabled(false);
        unitNo = view.findViewById(R.id.auto3_2);
        unitNoError = view.findViewById(R.id.unit_no_error);
        unitNoError.setVisibility(View.GONE);
        societyOffice = view.findViewById(R.id.society_office);
        //societyOffice.setVisibility(View.GONE);
        societyOffice.setEnabled(false);

        materialButton = view.findViewById(R.id.material_button);
        materialButton.setOnClickListener(materialButtonListener);

        //auto3_2 = view.findViewById(R.id.auto3_2);
        if(vhfModel.isSocietyPurpose()) {
            getSocietyBearerList(accessToken, societyId);
            unitNoLayout.setVisibility(View.GONE);
            residentNameLayout.setVisibility(View.GONE);
            edit3_1.setVisibility(View.GONE);
            unitNo.setVisibility(View.GONE);

            societyOffice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String []tempSocietyBearer = societyOffice.getEditableText().toString().split(",");
                    String societyMem = tempSocietyBearer[tempSocietyBearer.length - 2].trim();

                    String bearerMemId = societyBearer.get(societyMem);

                    vhfModel.getSelectedSocietyBearer().add(societyMem);
                    vhfModel.getBearerMemId().put(societyMem, bearerMemId);

                    //residentId = flatmemberNameId.get(tempResNo[0]);
                }
            });
        } else {
            getFlatList(accessToken, societyId);
            societyOffice.setVisibility(View.GONE);
            societyLayout.setVisibility(View.GONE);
            panel4.setWeightSum(2);
            materialButton.setVisibility(View.GONE);
            /*auto3_2.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,flatNoList);
            auto3_2.setThreshold(1);
            auto3_2.setAdapter(adapter);*/
            unitNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                        long id) {
                    Log.d("Spinner",unitNo.getEditableText().toString());

                    unitNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    unitNoError.setVisibility(View.GONE);

                    String []tempResNo = unitNo.getEditableText().toString().split(",");
                    String flatMem = flatmember.get(tempResNo[tempResNo.length - 2].trim());

                    String flatMemId = flatmemberNameId.get(tempResNo[tempResNo.length - 2].trim());

                    //selectedFlatNo.add(tempResNo[tempResNo.length - 2].trim());
                    vhfModel.getSelectedFlatNo().add(tempResNo[tempResNo.length - 2].trim());

                    //flatNo_ResName.put(tempResNo[tempResNo.length - 2].trim(), flatMem);
                    //flatNo_MemId.put(tempResNo[tempResNo.length - 2].trim(), flatMemId);
                    vhfModel.getFlatNoResName().put(tempResNo[tempResNo.length - 2].trim(), flatMem);
                    vhfModel.getFlatNoMemId().put(tempResNo[tempResNo.length - 2].trim(), flatMemId);

                    Log.d("Spinner 2",tempResNo[tempResNo.length - 2]);
                    Log.d("Spinner 3",flatMem);

                    residentId = flatmemberNameId.get(tempResNo[0]);

                    popupResidentNames(flatMem);
                    //int a =
                /*Log.d("Spinner",""+pos);
                int tempPos = 0;
                for (int p=0; p < tempFlatNo.size();p++){
                    Log.d("Temp Spinner",""+tempFlatNo.get(p) + " - " + auto3_2.getEditableText().toString());
                    if((tempFlatNo.get(p)+", ").equals(auto3_2.getEditableText().toString())) {
                        tempPos = p;
                        Log.d("Spinner",""+p);
                    }
                }
                flatNo.remove(tempPos);
                ArrayAdapter adapter1 = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,flatNo);
                auto3_2.setAdapter(adapter1);
                */
                    //edit3_1.setText(flatmember.get(auto3_2.getEditableText().toString()));
                    //Toast.makeText(getApplicationContext()," selected", Toast.LENGTH_LONG).show();
                    //PopUp for resident names list

                }
            });

            unitNo.setOnFocusChangeListener(unitFocusListener);
        }

        /*
        material = view.findViewById(R.id.checkbox_material);
        material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if(checked) {
                    reqMaterial = true;
                } else {
                    reqMaterial = false;
                }
            }
        });
        */


        /*
        material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) view).isChecked();
                isValidResidenceNo = true;

                if(checked) {
                    notifyByPhone = true;
                    edit3_3.setVisibility(View.VISIBLE);
                } else {
                    notifyByPhone = false;
                    edit3_3.setVisibility(View.GONE);
                }
            }
        });
        */

        vehicleNo = view.findViewById(R.id.edit1_11);
        vehicleNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        vehicleType = view.findViewById(R.id.vehicle_type);
        populateVehicleNo(vehicleType);
        vehicle_count = view.findViewById(R.id.vehicle_count);
        populateVehicleCount(vehicle_count);

        narration = view.findViewById(R.id.edit3_4);
        narration.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});

        govtId = view.findViewById(R.id.govtId);
        populateProofType(govtId);
        govIdNo = view.findViewById(R.id.edit5_2);
        govIdNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});

        //edit3_3 = view.findViewById(R.id.edit3_3);

        /*
        pastSecurityIncidences = view.findViewById(R.id.vn_past_security_incidence);
        pastSecurityIncidences.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    // Display popup
                    displayBypassPopup();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        populatePastSecuritySpinner(pastSecurityIncidences);
        */


        /*
        Button viewHistory = (Button)view.findViewById(R.id.vn_view_history);
        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPastIncidences();
            }
        });
        */

        vehicleNo.setOnKeyListener(gotoNextListener);
        narration.setOnKeyListener(gotoNextListener);

        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(submitButtonListener);

        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(backButtonListener);

        setVisitorNextFragmentValues(vhfModel);


        /*
        //societyOffice.setText("ABCD");
        vehicleNo.setText("MH46W1234");
        vehicleType.setSelection(1);
        vehicle_count.setSelection(3);
        narration.setText("Do work");
        govtId.setSelection(2);
        govIdNo.setText("ABDT");
        */

        return view;
    }

    public void populatePastSecuritySpinner(Spinner dropdown) {
        String[] items = new String[]{"Options", "ByPass", "Reject"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    public void displayBypassPopup() {
        FragmentManager fm = getFragmentManager();
        SecurityPopup popup = new SecurityPopup();
        popup.show(fm, "Bypass");
    }

    /*
    public void displayPastIncidences() {
        FragmentManager fm = getFragmentManager();
        PastIncidencesPopup popup = new PastIncidencesPopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.VISITOR_MOBILE_NO, visitorMobile);

        popup.setArguments(bundle);
        popup.show(fm, "PastIncidences");
    }
    */

    /*public void showHideNotifyPhone(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        isValidResidenceNo = true;

        if(checked) {
            notifyByPhone = true;
            edit3_3.setVisibility(View.VISIBLE);
        } else {
            notifyByPhone = false;
            edit3_3.setVisibility(View.GONE);
        }
    }*/

    /*
    private void sendVisitorOTP(final String iaccessToken, String visitorMobile)
    {
        final Map<String, String> myParams = new HashMap<String, String>();
        myParams.put(Constants.OTP_MOBILE_NO, visitorMobile);
        String URL = Constants.BASE_URL+"Visitorotp";
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //showProgress(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // progressDialog.dismiss();
                //progress.setVisibility(View.GONE);
                VolleyLog.d("responseError", "Error: " + volleyError);
                //showProgress(false);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + iaccessToken);
                //params.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return myParams;
            }

        };

        //showProgress(true);
        //VolleyController commInstanceObj = VolleyController.getInstance(this);
        //commInstanceObj.getRequestQueue().add(jsonObjReq);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
    */

    private void getFlatList(final String accessToken, String societyId) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);

        //String URL = "http://103.48.51.62:8018/Property/?SocityId="+societyId;
        String URL = Constants.BASE_URL+"Property/?SocietyId="+societyId;
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //showProgress(false);
                        try {
                            JSONObject object = new JSONObject(s);

                            JSONArray resourceList = object.getJSONArray("ResourceList");

                            Log.d("FLATLIST", ""+resourceList.length());

                            for (int i=0; i<resourceList.length();i++) {
                                JSONObject resource = resourceList.getJSONObject(i);
                                //Log.d("FLATLIST", ""+resource.toString());
                                flatNoList.add(resource.getString("MpwNoOfFlats"));
                                tempFlatNo.add(resource.getString("MpwNoOfFlats"));
                                flatmember.put(resource.getString("MpwNoOfFlats"), resource.getString("MemberName"));
                                flatmemberNameId.put(resource.getString("MpwNoOfFlats"), resource.getString("MemberID"));
                            }

                            popup.dismiss();
                            //Utility.showProgress(false, getActivity().getApplicationContext(), progressView, parentScroll);

                            unitNo.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                            ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.my_spinner_dropdown_item,flatNoList);
                            unitNo.setThreshold(1);
                            unitNo.setAdapter(adapter);

                            //String socityID = resource.getString("Societyd");
                            //societyId = resource.getString("Societyd");

                            //Log.d("Flat", flatNo.toString());
                            //Log.d("Flat", flatmember.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //progress.setVisibility(View.GONE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView, parentScroll);
                VolleyLog.d("responseError", "Error: " + volleyError);
                //showProgress(false);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                //params.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

        };


        //VolleyController commInstanceObj = VolleyController.getInstance(this);
        //commInstanceObj.getRequestQueue().add(jsonObjReq);
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView, parentScroll);
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void populateVehicleNo(Spinner dropdown) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_dropdown_item, vehicleTypeList);
        dropdown.setAdapter(adapter);
    }

    public void populateVehicleCount(Spinner dropdown) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_dropdown_item, vehicleCountList);
        dropdown.setAdapter(adapter);
    }

    public void populateProofType(Spinner dropdown) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_dropdown_item, proofTypeList);
        dropdown.setAdapter(adapter);
    }


    private void saveToServer(final Map<String, String> myParams, final int uploadingIndex) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);

        StringRequest request = new StringRequest(Request.Method.POST,Constants.BASE_URL+"Visitors",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            Log.d("RESPONSE SUCCESS - ", "" + s.toString());

                            String visitorId = object.getString("Id");
                            String visitId = object.getString(Constants.VISIT_ID);//VisitId

                            //String visitId = object.getString("VisitId");
                            popup.dismiss();
                            if(!vhfModel.hasPreviousPhoto()){
                                saveVisitorPic(visitorId, visitId, uploadingIndex);
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Visitor added successfully!!", 5000).show();

                                final Bundle bundle = new Bundle();
                                bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                                bundle.putString(Constants.SOCIETY_ID, societyId);
                                bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();

                                VisitorHomeFragment homeFragment = new VisitorHomeFragment();
                                homeFragment.setArguments(bundle);
                                ft.replace(R.id.rootLayout,homeFragment);
                                ft.commit();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView, parentScroll);
                Toast.makeText(getActivity().getApplicationContext(), "Error while adding visitor to server", 5000).show();
                Log.d("RESPONSE ERROR", volleyError.toString());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //String accesstoken = "Oo5_Wrq-Xl0YgPlQPvDImcPab9m_o5own8Vtfa9jBJH0DSmH71BCs62spp9uyyUfHqKx-gmGc89DHIUCcQAKEyYbhoziFUbMUPg5_CrTc5fIc7Ji9o0QNIsHiWpe4YIW9hJb5Ht3A8j5qtgp_pow3B7tZF1_06JBT-zDNGbai3uJrjoQVCEiwiDiBbvhG94k44mIId2QTfa_koM1u0dzzF-QB65h5_0kzIpELeQHKhzjGEFxSvh5RQYFyyIJtZq_Hvy3EpUfc7dDrA5if3Xpy9LAu2u5PkwmoHLEppUNrXyfrhQhtb3al8HUxaaH0mOqn3T4GqcZZ-DlaSZ1ljpbLuvZnIRzj3_CsGD8TEq5CkwnU3KHEC6zdgrOrxdgL8u2";
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                //params.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(vhfModel.isSocietyPurpose()) {
                    //String societyMem = vhfModel.getSelectedSocietyBearer().get(uploadingIndex);
                    //myParams.put(Constants.SOCIETY_OFFICE_BEARER, societyMem);
                    //myParams.put(Constants.MEMBER_ID, vhfModel.getBearerMemId().get(societyMem));
                    myParams.put(Constants.FLAT_NUMBER, "");
                    myParams.put(Constants.RESIDENT_NAME, "");
                    myParams.put(Constants.SOCIETY_OFFICE_BEARER, vhfModel.getSocietyBearerStr());
                    myParams.put(Constants.MEMBER_ID, vhfModel.getSocietyBearerMemberIdStr());

                } else {
                    String flatNumber = vhfModel.getSelectedFlatNo().get(uploadingIndex);
                    myParams.put(Constants.FLAT_NUMBER, flatNumber);
                    myParams.put(Constants.MEMBER_ID, vhfModel.getFlatNoMemId().get(flatNumber));
                    myParams.put(Constants.RESIDENT_NAME, vhfModel.getFlatNoResName().get(flatNumber));
                }

                return myParams;
            }
        };



        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView, parentScroll);

        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }


    private void saveVisitorPic(final String visitorId, final String visitId, final int uploadingIndex) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);

        //final String URL = Constants.BASE_URL+"api/DocumentUpload/MediaUpload";
        //http://103.48.51.62:8018/Visitors/VisitorImage
        final String URL = Constants.BASE_URL+"Visitors/VisitorImage";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            Log.d("RESPONSE SUCCESS PIC", response.toString());

                            if((vhfModel.getSelectedFlatNo().size() - 1) > uploadingIndex) {
                                saveToServer(params, uploadingIndex+1);
                            } else {
                                popup.dismiss();
                                Toast.makeText(getActivity().getApplicationContext(), "Visitor added successfully!!", 5000).show();

                                final Bundle bundle = new Bundle();
                                bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                                bundle.putString(Constants.SOCIETY_ID, societyId);
                                bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();

                                if(reqMaterial) {
                                    MaterialEntryFragment materialFragment = new MaterialEntryFragment();
                                    materialFragment.setArguments(bundle);
                                    ft.replace(R.id.rootLayout,materialFragment);
                                } else {
                                    VisitorHomeFragment homeFragment = new VisitorHomeFragment();
                                    homeFragment.setArguments(bundle);
                                    ft.replace(R.id.rootLayout,homeFragment);
                                }
                                ft.commit();

                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        popup.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Error while saving visitor pic", Toast.LENGTH_SHORT).show();
                        Log.d("RESPONSE SUCCESS ERROR", error.toString());
                    }
                }) {

            /*
            * If you want to add more parameters with the image
            * you can do it here
            * here we have only one parameter with the image
            * which is tags
            * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("VisitorId", visitorId);
                params.put(Constants.VISIT_ID, visitId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                //params.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            /*
            * Here we are passing image by renaming it with a unique name
            * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                //Log.d("SUCCESS", visitorPic.toString());
                Log.d("FILE visitorPic", visitorPic.toString());
                params.put("ClientImage", new DataPart(imagename + ".png", getFileDataFromDrawable(visitorPic)));
                return params;
            }
        };

        //adding the request to volley
        popup.show(fm, "Progress");
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);



    }



    public void popupResidentNames(String residentName) {
        FragmentManager fm = getFragmentManager();
        ResidentNames popup = new ResidentNames();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESIDENT_NAME, residentName);
        popup.setArguments(bundle);
        popup.setCancelable(false);
        popup.setTargetFragment(this, RESIDENT_REQUEST_CODE);
        popup.show(fm, "Resident Names");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESIDENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if(data.getExtras().containsKey("RESIDENT_NAME")) {
                Log.d("RETVAL", data.getExtras().getString("RESIDENT_NAME"));
                resultResName += data.getExtras().getString("RESIDENT_NAME");
                edit3_1.setText(resultResName);
                resultResName += ", ";
            }
        }
    }

    private void addVisitorsToServer(Map<String, String> myParams) {
        Map<String, String> params = myParams;

        /*if(selectedFlatNo.size() > 0) {
            saveToServer(params, uploadCount);
        }*/
        saveToServer(params, uploadCount);

    }


    public View.OnClickListener backButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VisitorHomeFragment homeFragment = new VisitorHomeFragment();
            final Bundle bundle = new Bundle();
            bundle.putString(Constants.ACCESS_TOKEN, accessToken);
            bundle.putString(Constants.SOCIETY_ID, societyId);
            bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
            bundle.putBoolean(Constants.FROM_BACK_FRAGMENT, true);

            /*
            bundle.putBoolean(Constants.FROM_BACK_FRAGMENT, true);
            bundle.putString(Constants.GATE_NO, gateNo);
            bundle.putString(Constants.VISITOR_MOBILE_NO, visitorMobile);
            bundle.putString(Constants.VISITOR_NAME, visitorName);
            bundle.putString(Constants.NO_OF_VISTORS, noOfVisitors);
            bundle.putString(Constants.VISITOR_ADDRESS, visitorAddress);
            bundle.putString(Constants.VISITOR_TYPE, visitorType);
            bundle.putString(Constants.ORGANIZATION_NAME, orgName);
            bundle.putString(Constants.VISITOR_PHOTO_PATH, picPath);
            bundle.putBoolean(Constants.SOCIETY_PURPOSE, forSocietyVisit);
            */

            //NextFragment
            /*
            flatNo = auto3_2.getEditableText().toString();
            residentName = edit3_1.getText().toString();
            vehicleNo = vehicleNo.getText().toString();
            vehicleType = vehicleType.getSelectedItem().toString();
            vehicleCount = vehicle_count.getSelectedItem().toString();
            narration = narration.getText().toString();
            proofOfID = govtId.getSelectedItem().toString();
            idNo = govIdNo.getText().toString();
            */

            vhfModel.setFlatNo(unitNo.getEditableText().toString());
            vhfModel.setSocietyBearer(societyOffice.getEditableText().toString());
            vhfModel.setResidentName(edit3_1.getText().toString());
            vhfModel.setVehicleNo(vehicleNo.getText().toString());
            vhfModel.setVehicleType(vehicleType.getSelectedItem().toString());
            vhfModel.setVehicleCount(vehicle_count.getSelectedItem().toString());
            vhfModel.setNarration(narration.getText().toString());
            vhfModel.setProofOfID(govtId.getSelectedItem().toString());
            vhfModel.setIdNo(govIdNo.getText().toString());

            /*
            bundle.putString(Constants.FLAT_NUMBER, flatNo);
            bundle.putString(Constants.RESIDENT_NAME, residentName);
            bundle.putString(Constants.VISITOR_VEHICLE_NO, vehicleNo);
            bundle.putString(Constants.VISITOR_VEHICLE_TYPE, vehicleType);
            bundle.putString(Constants.NO_OF_VEHICLES, vehicleCount);
            bundle.putString(Constants.NARRATION, narration);
            bundle.putString(Constants.VISITOR_GOVT_ID, proofOfID);
            bundle.putString(Constants.VISITOR_ID_NO, idNo);
            */

            bundle.putSerializable(Constants.VHFMODEL, vhfModel);



            homeFragment.setArguments(bundle);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.rootLayout,homeFragment);
            ft.commit();
        }
    };

    public View.OnClickListener submitButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String errorType = "";
            boolean isValidData = true;
            if(vhfModel.isSocietyPurpose()) {
                if(societyOffice.getEditableText().toString().trim().length() == 0) {
                    //societyOffice.setError("Mobile no must be 10 digit");
                    isValidData = false;
                    errorType = "SocietyPurpose";
                }
            } else {
                if(unitNo.getEditableText().toString().trim().length() == 0) {
                    //auto3_2.setError("Mobile no must be 10 digit");
                    unitNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                    unitNoError.setText(unitNoErrorTxt);
                    unitNoError.setVisibility(View.VISIBLE);
                    isValidData = false;
                    errorType = "No SocietyPurpose";
                }
            }

            if(!isValidData) {
                Log.d("ERROR", "ERROR  --> " + errorType);
            }


            if(isValidData) {
                String flatNo = unitNo.getEditableText().toString();
                String residentName = edit3_1.getText().toString();
                /*
                String vehicleNo = vehicleNo.getText().toString();
                String vehicleType = vehicleType.getSelectedItem().toString();
                String vehicleCount = vehicle_count.getSelectedItem().toString();
                String narration = narration.getText().toString();
                String proofOfID = govtId.getSelectedItem().toString();
                String idNo = govIdNo.getText().toString();
                */

                vhfModel.setSocietyBearer(societyOffice.getEditableText().toString());
                vhfModel.setVehicleNo(vehicleNo.getText().toString());
                vhfModel.setVehicleType(vehicleType.getSelectedItem().toString());
                vhfModel.setVehicleCount(vehicle_count.getSelectedItem().toString());
                vhfModel.setNarration(narration.getText().toString());
                vhfModel.setProofOfID(govtId.getSelectedItem().toString());
                vhfModel.setIdNo(govIdNo.getText().toString());

                //String residentId = flatmemberNameId.get(flatNo);


                params.put(Constants.SOCIETY_ID, ""+societyId);
                params.put(Constants.GATE_NO, vhfModel.getGateNo());
                params.put(Constants.VISITOR_MOBILE_NO, vhfModel.getVisitorMobile());
                params.put(Constants.IS_OTP_VERIFIED, ""+vhfModel.isOTPVerified());
                params.put(Constants.VISITOR_PREFIX, "Mr.");
                params.put(Constants.VISITOR_NAME, vhfModel.getVisitorName());
                params.put(Constants.NO_OF_VISTORS, vhfModel.getNoOfVisitors());
                params.put(Constants.VISITOR_ADDRESS, vhfModel.getVisitorAddress());
                params.put(Constants.VISITOR_TYPE, vhfModel.getVisitorType());
                params.put(Constants.ORGANIZATION_NAME, vhfModel.getOrgName());
                params.put(Constants.SOCIETY_PURPOSE, ""+vhfModel.isSocietyPurpose());
                params.put(Constants.VISITOR_PHOTO_PATH, vhfModel.getPicPath());
                if (vhfModel.getPicPath() != null && (!vhfModel.hasPreviousPhoto())) {
                    visitorPic = Utility.getImageFromPath(vhfModel.getPicPath());
                }

                //Second Fragment
                params.put(Constants.VISITOR_VEHICLE_NO, vhfModel.getVehicleNo());
                params.put(Constants.VISITOR_VEHICLE_TYPE, vhfModel.getVehicleType());
                params.put(Constants.NO_OF_VEHICLES, vhfModel.getVehicleCount());
                params.put(Constants.NARRATION, vhfModel.getNarration());
                params.put(Constants.VISITOR_GOVT_ID, vhfModel.getProofOfID());
                params.put(Constants.VISITOR_ID_NO,vhfModel.getIdNo());
                params.put(Constants.SOCIETY_OFFICE_BEARER,vhfModel.getSocietyBearer() == null ? "" : vhfModel.getSocietyBearer());

                params.put(Constants.BYPASS_NAME,vhfModel.getBypassName() == null ? "" : vhfModel.getBypassName());
                params.put(Constants.BYPASS_REMARK,vhfModel.getBypassRemark() == null ? "" : vhfModel.getBypassRemark());
                //params.put(Constants.IS_VISITOR_REJECTED, ""+vhfModel.isRejected());


                params.put(Constants.NOTIFICATION_TYPE, "Visitor Alert");

                params.put(Constants.START_DATE_TIME, vhfModel.getStartDateTime());
                params.put(Constants.END_DATE_TIME, ""+System.currentTimeMillis());

                //saveToServer(params);
                addVisitorsToServer(params);
            }

        }
    };

    public View.OnClickListener materialButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MaterialEntryFragment materialFragment = new MaterialEntryFragment();
            final Bundle bundle = new Bundle();
            bundle.putString(Constants.ACCESS_TOKEN, accessToken);
            bundle.putString(Constants.SOCIETY_ID, societyId);
            bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
            bundle.putBoolean(Constants.FROM_VISITOR, true);

            vhfModel.setFlatNo(unitNo.getEditableText().toString());
            vhfModel.setSocietyBearer(societyOffice.getEditableText().toString());
            vhfModel.setResidentName(edit3_1.getText().toString());
            vhfModel.setVehicleNo(vehicleNo.getText().toString());
            vhfModel.setVehicleType(vehicleType.getSelectedItem().toString());
            vhfModel.setVehicleCount(vehicle_count.getSelectedItem().toString());
            vhfModel.setNarration(narration.getText().toString());
            vhfModel.setProofOfID(govtId.getSelectedItem().toString());
            vhfModel.setIdNo(govIdNo.getText().toString());

            bundle.putSerializable(Constants.VHFMODEL, vhfModel);

            materialFragment.setArguments(bundle);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.rootLayout,materialFragment);
            ft.commit();
        }
    };


    //params.put(Constants.FLAT_NUMBER, key);
    //params.put(Constants.MEMBER_ID, flatNo_MemId.get(key));
    //params.put(Constants.RESIDENT_NAME, flatNo_ResName.get(key));

    private void setVisitorNextFragmentValues(VisitorHomeFragmentModel vhfModel) {

        unitNo.setText(vhfModel.getFlatNo());
        societyOffice.setText(vhfModel.getSocietyBearer());
        edit3_1.setText(vhfModel.getResidentName());
        vehicleNo.setText(vhfModel.getVehicleNo());

        for (int i = 0; i<vehicleTypeList.length; i++) {
            if(vehicleTypeList[i].equals(vhfModel.getVehicleType())) {
                vehicleType.setSelection(i);
            }
        }

        for (int i = 0; i<vehicleCountList.length; i++) {
            if(vehicleCountList[i].equals(vhfModel.getVehicleCount())) {
                vehicle_count.setSelection(i);
            }
        }

        narration.setText(vhfModel.getNarration());

        for (int i = 0; i<proofTypeList.length; i++) {
            if(proofTypeList[i].equals(vhfModel.getProofOfID())) {
                govtId.setSelection(i);
            }
        }

        govIdNo.setText(vhfModel.getIdNo());

    }

    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //bitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/4, bitmap.getHeight()/4, false);
        bitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth()*3, bitmap.getHeight()*3, false);

        int width1 = bitmap.getWidth();
        int height1 = bitmap.getHeight();
        Log.d("ORG RE-SIZE :- ", "" + width1);
        Log.d("ORG RE-SIZE :- ", "" + height1);

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        Log.d("LENGTH", ""+byteArrayOutputStream.size());
        return byteArrayOutputStream.toByteArray();
    }

    private void getSocietyBearerList(final String accessToken, String societyId) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        String URL = Constants.BASE_URL+"SocietyOfficeBearers/?SocietyId="+societyId+"&CommType=1";
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            JSONArray resourceList = object.getJSONArray("ResourceList");
                            Log.d("SocietyOfficeBearers", ""+resourceList.length());

                            for (int i=0; i<resourceList.length();i++) {
                                if(i != 0) {
                                    societyBearerStr += ",";
                                    societyMemberIdStr += ",";
                                }
                                JSONObject resource = resourceList.getJSONObject(i);
                                societyBearerList.add(resource.getString("MemberName"));
                                societyMemberIdList.add(resource.getString("MemberId"));
                                societyBearer.put(resource.getString("MemberName"), resource.getString("MemberId"));

                                societyBearerStr += resource.getString("MemberName");
                                societyMemberIdStr += resource.getString("MemberId");
                                societyOffice.setText(societyBearerStr);

                                vhfModel.setSocietyBearerStr(societyBearerStr);
                                vhfModel.setSocietyBearerMemberIdStr(societyMemberIdStr);
                                //societyBearerId.put(resource.getString("MpwNoOfFlats"), resource.getString("MemberID"));
                            }

                            //Utility.showProgress(false, getActivity().getApplicationContext(), progressView, parentScroll);

                            popup.dismiss();
                            societyOffice.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                            ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.my_spinner_dropdown_item,societyBearerList);
                            societyOffice.setThreshold(1);
                            societyOffice.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView, parentScroll);
                VolleyLog.d("responseError", "Error: " + volleyError);
                //showProgress(false);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                //params.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

        };


        //VolleyController commInstanceObj = VolleyController.getInstance(this);
        //commInstanceObj.getRequestQueue().add(jsonObjReq);
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView, parentScroll);
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    private View.OnFocusChangeListener unitFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                boolean matchFound = false;
                String flatNo = "";
                String flatMem = "";
                String flatMemId = "";
                for (int i = 0; i < flatNoList.size(); i++) {
                    if(flatNoList.get(i).equalsIgnoreCase(unitNo.getEditableText().toString())) {
                        //Show residence popup
                        matchFound = true;
                        flatNo = flatNoList.get(i);
                        vhfModel.getSelectedFlatNo().add(flatNo);
                        flatMem = flatmember.get(flatNo);
                        flatMemId = flatmemberNameId.get(flatNo);
                        vhfModel.getFlatNoResName().put(flatNo, flatMem);
                        vhfModel.getFlatNoMemId().put(flatNo, flatMemId);
                        break;
                    }
                }
                //If no match found show error
                if(matchFound) {
                    popupResidentNames(flatMem);
                } else {
                    //Wrong Unit NO
                    if(vhfModel.getSelectedFlatNo().size() == 0) {
                        //unitNo.setError("Wrong Unit No");
                        unitNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        unitNoError.setText("Wrong Unit No");
                        unitNoError.setVisibility(View.VISIBLE);
                    }
                }

            } else {
                unitNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                unitNoError.setVisibility(View.GONE);
            }

        }
    };


    public boolean onBackPressed() {
        return true;
    }

    private View.OnKeyListener gotoNextListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            Log.d("Key Pressed", "--> " + keyCode);
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                //type.requestFocus();
                switch (view.getId()) {
                    case R.id.edit1_11:
                        vehicleType.requestFocus();
                        vehicleType.performClick();
                        Utility.hideKeyboard(getActivity(), getView());
                        break;
                    case R.id.edit3_4:
                        govtId.requestFocus();
                        govtId.performClick();
                        break;
                    /*case R.id.vh_visitor_name:
                        vh_visitor_count.requestFocus();
                        break;
                    case R.id.vh_visitor_count:
                        address.requestFocus();
                        break;
                    case R.id.edit1_3:
                        Utility.hideKeyboard(getActivity(), getView());
                        type.requestFocus();
                        type.performClick();
                        break;
                    default:
                        break;
                        */

                }
            }
            return false;
        }
    };



}
