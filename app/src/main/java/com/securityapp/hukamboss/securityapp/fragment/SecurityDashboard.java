package com.securityapp.hukamboss.securityapp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
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
import com.securityapp.hukamboss.securityapp.CustomProgressBar;
import com.securityapp.hukamboss.securityapp.R;
import com.securityapp.hukamboss.securityapp.adapters.SecurityAttendanceAdapter;
import com.securityapp.hukamboss.securityapp.adapters.SecurityConcernsAdapter;
import com.securityapp.hukamboss.securityapp.adapters.VisitorActivityAdapter;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.interfaces.BaseActivityListener;
import com.securityapp.hukamboss.securityapp.model.DashboardFilterModel;
import com.securityapp.hukamboss.securityapp.model.MaterialModel;
import com.securityapp.hukamboss.securityapp.model.StaffAttendanceModel;
import com.securityapp.hukamboss.securityapp.model.StaffModel;
import com.securityapp.hukamboss.securityapp.model.VehicleModel;
import com.securityapp.hukamboss.securityapp.model.VisitorModel;
import com.securityapp.hukamboss.securityapp.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SecurityDashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecurityDashboard extends Fragment implements BaseActivityListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String[] gateItems = new String[]{"--Gate No--", "1", "2", "3"};

    Spinner gateNo,securityPerson, filterDays;
    String[] securityPersonItems = new String[]{"--Select Security--", "Mr. Umesh", "Mr. Rajesh", "Mr. Suresh"};

    String[] filterDaysItems = new String[]{"--Select Days--", "Today", "Last 7 days", "Last 30 days", "Last weekend"};

    Button searchBtn;

    String resVisitorIn, socVisitorIn, resStaffVisitorIn, socStaffVisitorIn, resVisitorMatIn, socVisitorMatIn, resStaffVisitorMatIn, socStaffVisitorMatIn, resVisitorVehIn, socVisitorVehIn, resStaffVisitorVehIn, socStaffVisitorVehIn;

    String accessToken, societyId, societyGateNo;

    JSONObject resultJSON;
    JSONObject securityConcernJSON;
    JSONObject securityAttendanceJSON;

    ProgressBar progressView;
    GridView gridView1 = null;
    GridView gridView2 = null;
    GridView gridView3 = null;
    final SecurityDashboard listener = this;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TabHost tabHost;
    TabHost mainTabHost;
    boolean searchPressed;
    int currentTab = 1;

    private OnFragmentInteractionListener mListener;

    public SecurityDashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecurityDashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static SecurityDashboard newInstance(String param1, String param2) {
        SecurityDashboard fragment = new SecurityDashboard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_security_dashboard, container, false);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        societyGateNo = getArguments().getString(Constants.SOCIETY_GATE_NO);

        progressView = view.findViewById(R.id.progress);


        //getDashboard(accessToken, societyId, "0", "0", "0");
        gridView1 = view.findViewById(R.id.gridview1);
        gridView2 = view.findViewById(R.id.gridview2);
        gridView3 = view.findViewById(R.id.gridview3);
        getVisitorActivity(accessToken, societyId, "0", "0", "0");

        mainTabHost = view.findViewById(R.id.tabHost);

        final TabHost host = (TabHost)view.findViewById(R.id.tabHost);
        host.setup();

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            //gridView = null;
            @Override
            public void onTabChanged(String s) {
                int i = host.getCurrentTab();

                switch (i) {
                    case 0:
                        gridView1 = view.findViewById(R.id.gridview1);
                        currentTab = 1;
                        gridViewSetting(gridView1, 8);
                        gridView1.setAdapter(new VisitorActivityAdapter(getActivity(), listener, 8, resultJSON));

                        //gridView1.get

                        /*for(int j =0; j < gridView1.getNumColumns()/2; j++) {
                            gridView1.setBackgroundColor(Color.parseColor("#18A608"));
                        }*/
                        break;
                    case 1:
                        gridView2 = view.findViewById(R.id.gridview2);
                        gridViewSetting(gridView2, gateItems.length+1);
                        currentTab = 2;
                        gridView2.setAdapter(new SecurityConcernsAdapter(getActivity(), listener, gateItems.length+1, securityConcernJSON, gateItems));
                        break;
                    case 2:
                        gridView3 = view.findViewById(R.id.gridview3);
                        gridViewSetting(gridView3, gateItems.length+1);
                        currentTab = 3;
                        gridView3.setAdapter(new SecurityAttendanceAdapter(getActivity(), listener, gateItems.length+1, securityAttendanceJSON, gateItems));
                        break;
                }
            }
        });

        gateNo = view.findViewById(R.id.gate_no);
        populateGateNos(gateNo);

        securityPerson = view.findViewById(R.id.security_person);
        populateSecurityPerson(securityPerson);

        filterDays = view.findViewById(R.id.filter_days);
        populateFilterDays(filterDays);

        searchBtn = view.findViewById(R.id.search_button);
        searchBtn.setOnClickListener(searchBtnListener);


        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec(getString(R.string.sd_visitoractivity));
        spec.setContent(R.id.tab1);
        spec.setIndicator(getString(R.string.sd_visitoractivity));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec(getString(R.string.sd_securityconcerns));
        spec.setContent(R.id.tab2);
        spec.setIndicator(getString(R.string.sd_securityconcerns));
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec(getString(R.string.sd_attendance));
        spec.setContent(R.id.tab3);
        spec.setIndicator(getString(R.string.sd_attendance));
        host.addTab(spec);

        return view;
    }

    private void gridViewSetting(GridView gridview, Integer nCol) {

        // Calculated single Item Layout Width for each grid element ....
        int width = 120 ;

//        DisplayMetrics dm = new DisplayMetrics();
//        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        float density = dm.density;
        float density = Utility.getDeviceScreenDensity(getActivity());

        int totalWidth = (int) (width * nCol * density);
        int singleItemWidth = (int) (width * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        gridview.setLayoutParams(params);
        gridview.setColumnWidth(singleItemWidth);
        gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridview.setNumColumns(nCol);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void populateGateNos(Spinner dropdown) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_dropdown_item, gateItems);
        dropdown.setAdapter(adapter);
    }

    public void populateSecurityPerson(Spinner dropdown) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_dropdown_item, securityPersonItems);
        dropdown.setAdapter(adapter);
    }

    public void populateFilterDays(Spinner dropdown) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_dropdown_item, filterDaysItems);
        dropdown.setAdapter(adapter);
    }



    @Override
    public void didClickButton(ArrayList<String> entries, Integer position) {
        Log.d("POPUP", position.toString() + " - " + entries.toString());
        //showDialog();
        if(entries.get(0).equalsIgnoreCase("TYPE")) {
            visitorActivityPopup(position);
        }
        if(entries.get(5).equalsIgnoreCase("OTP NOT VERIFIED")) {
            securityConcernPopup(position);
        }

        if(entries.get(5).equalsIgnoreCase("Present")) {
            staffAttendancePopup(position);
        }
    }

    public void showDialog(String title, String[] result) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.dashboard_visitor_popup, result);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setItems(result, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private View.OnClickListener searchBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Log.d(" ---> ", gateNo.getSelectedItemPosition() + " - " + securityPerson.getSelectedItemPosition() + " - " + filterDays.getSelectedItemPosition());
            String gateNoVal = ""+gateNo.getSelectedItemPosition();
            String securityPersonVal = ""+securityPerson.getSelectedItemPosition();
            String filterDaysVal = ""+filterDays.getSelectedItemPosition();
            //getDashboard(accessToken, societyId, gateNoVal, securityPersonVal, filterDaysVal);
            searchPressed = true;
            //getVisitorActivity(accessToken, societyId, gateNoVal, securityPersonVal, filterDaysVal);
            /*if(currentTab < 3) {
                getVisitorActivity(accessToken, societyId, gateNoVal, securityPersonVal, filterDaysVal);
            } else {
                getStaffAttendance(accessToken, societyId, gateNoVal, securityPersonVal, filterDaysVal);
            }*/

            getVisitorActivity(accessToken, societyId, gateNoVal, securityPersonVal, filterDaysVal);
            getStaffAttendance(accessToken, societyId, gateNoVal, securityPersonVal, filterDaysVal);


        }
    };

    private void getVisitorActivity(final String accessToken, final String societyId, final String gateNoStr, final String securityPersonStr, final String filterDaysStr) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        String URL = Constants.BASE_URL+"SecuritySupervisorDashboard/?FilterDays="+filterDaysStr+"&GateNumber="+gateNoStr;
        Log.d("URL", "--> " +URL);
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                            popup.dismiss();
                            JSONArray responseArray = new JSONArray(s);

                            //JSONObject object = new JSONObject(s);
                            JSONObject object = responseArray.getJSONObject(0);


                            resultJSON = object;
                            gridView1.setAdapter(new VisitorActivityAdapter(getActivity(), listener, 8, resultJSON));

                            /*if(!searchPressed) {
                                getSecurityConcerns(accessToken, societyId, gateNoStr, securityPersonStr, filterDaysStr);
                            }*/

                            getSecurityConcerns(accessToken, societyId, gateNoStr, securityPersonStr, filterDaysStr);


                            /*
                            resVisitorIn = object.getString(Constants.RES_VISITOR_IN);
                            socVisitorIn = object.getString(Constants.SOC_VISITOR_IN);
                            resStaffVisitorIn = object.getString(Constants.RES_STAFF_VISITOR_IN);
                            socStaffVisitorIn = object.getString(Constants.SOC_STAFF_VISITOR_IN);
                            resVisitorMatIn = object.getString(Constants.RES_STAFF_VISITOR_MAT_IN);
                            socVisitorMatIn = object.getString(Constants.SOC_VISITOR_MAT_IN);
                            resStaffVisitorMatIn = object.getString(Constants.RES_STAFF_VISITOR_MAT_IN);
                            socStaffVisitorMatIn = object.getString(Constants.SOC_STAFF_VISITOR_MAT_IN);
                            resVisitorVehIn = object.getString(Constants.RES_VISITOR_VEH_IN);
                            socVisitorVehIn = object.getString(Constants.SOC_VISITOR_VEH_IN);
                            resStaffVisitorVehIn = object.getString(Constants.RES_STAFF_VISITOR_VEH_IN);
                            socStaffVisitorVehIn = object.getString(Constants.SOC_STAFF_VISITOR_VEH_IN);*/

                            //Utility.showProgress(false, getActivity().getApplicationContext(), progressView, parentScroll);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                VolleyLog.d("responseError", "Error: " + volleyError);
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


        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void getSecurityConcerns(final String accessToken, final String societyId, final String gateNoStr, final String securityPersonStr, final String filterDaysStr) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        String URL = Constants.BASE_URL+"SecurityConcernsDashboard/?FilterDays="+filterDaysStr;
        Log.d("URL", "--> " +URL);
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                            popup.dismiss();
                            JSONArray responseArray = new JSONArray(s);

                            //JSONObject object = new JSONObject(s);
                            JSONObject object = responseArray.getJSONObject(0);


                            securityConcernJSON = object;
                            //gridViewSetting(gridView2, gateItems.length+1);
                            //gridView2.setAdapter(new SecurityConcernsAdapter(getActivity(), listener, gateItems.length, securityConcernJSON, gateItems));
                            getStaffAttendance(accessToken, societyId, gateNoStr, securityPersonStr, filterDaysStr);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                VolleyLog.d("responseError", "Error: " + volleyError);
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


        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void getStaffAttendance(final String accessToken, String societyId, String gateNoStr, String securityPersonStr, String filterDaysStr) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        //http://103.48.51.62:8018/SecurityStaffAttendanceDashboard/?FilterDays=0&SocietyId=1
        String URL = Constants.BASE_URL+"SecurityStaffAttendanceDashboard/?FilterDays="+filterDaysStr+"&SocietyId="+societyId;
        //String URL = Constants.BASE_URL+"SecurityStaffAttendanceDashboard/?FilterDays=3&SocietyId="+societyId;
        Log.d("URL", "--> " +URL);
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                            popup.dismiss();
                            JSONArray responseArray = new JSONArray(s);

                            //JSONObject object = new JSONObject(s);
                            JSONObject object = responseArray.getJSONObject(0);

                            securityAttendanceJSON = object;
                            //gridViewSetting(gridView1, 8);
                            //gridView1.setAdapter(new VisitorActivityAdapter(getActivity(), listener, 8, resultJSON));
                            mainTabHost.setCurrentTab(0);
                            //gridViewSetting(gridView3, gateItems.length+1);
                            //gridView3.setAdapter(new SecurityAttendanceAdapter(getActivity(), listener, gateItems.length, securityAttendanceJSON, gateItems));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                VolleyLog.d("responseError", "Error: " + volleyError);
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


        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void showVisitors(String type, String gateNo, String securityPerson, String filterDays, boolean societyPurpose, boolean visitorIn, boolean isOTPVerified, boolean isVisitRejected) {
        callVisitorList(type, gateNo, securityPerson, filterDays, societyPurpose, visitorIn, isOTPVerified, isVisitRejected);
    }

    private void showVisitors(DashboardFilterModel dashboardFilterModel) {
        callVisitorList(dashboardFilterModel);
    }

    private void showMaterials(String gateNo, String securityPerson, String filterDays, boolean societyPurpose, boolean visitorIn) {
        callMaterialList(gateNo, securityPerson, filterDays, societyPurpose, visitorIn);
    }

    private void showMaterials(DashboardFilterModel dashboardFilterModel) {
        callMaterialList(dashboardFilterModel);
    }

    private void showVehicles(String gateNo, String securityPerson, String filterDays, boolean societyPurpose, boolean visitorIn) {
        callVehicleList(gateNo, securityPerson, filterDays, societyPurpose, visitorIn);
    }

    private void showVehicles(DashboardFilterModel dashboardFilterModel) {
        callVehicleList(dashboardFilterModel);
    }

    private void showStaff(DashboardFilterModel dashboardFilterModel) {
        callStaffList(dashboardFilterModel);
    }

    private void showStaffAttendance(DashboardFilterModel dashboardFilterModel) {
        callStaffAttendanceList(dashboardFilterModel);
    }





    private void callVisitorList(String type, String gateNo, String securityPerson, String filterDays, boolean societyPurpose, boolean visitorIn, boolean isOTPVerified, boolean isVisitRejected) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        //IsOTPVerified=false&IsVisitRejected=false
        String URL = Constants.BASE_URL+"Visits/?FilterDays="+filterDays+"&GateNo="+gateNo;
        if(type.equalsIgnoreCase(Constants.VISITOR_ACTIVITY)) {
            URL += "&ActiveVisitor=Security";

            if(!visitorIn) {
                URL += "&IsVisit=true";
            }
            if(societyPurpose) {
                URL += "&SocietyPurpose=true";
            } else {
                URL += "&SocietyPurpose=false";
            }
        } else if(type.equalsIgnoreCase(Constants.SECURITY_CONCERN)) {
            URL += "&IsVisitRejected="+isVisitRejected;
            if(!isOTPVerified) {
                URL += "&IsOTPVerified="+isOTPVerified;
            }
            /*
            else {
                URL += "&IsOTPVerified=true";
            }*/


        }



        Log.d("URL", URL);

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            popup.dismiss();
                            String valStr = " ";
                            JSONArray resourceList = object.getJSONArray("ResourceList");
                            if (resourceList.length() > 0) {
                                parseVisitorModel(resourceList);
                            } else {
                                //empty_list.setVisibility(View.VISIBLE);
                                //visitorView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Visitor list is empty", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //popup.dismiss();
                            //progress.setVisibility(View.GONE);
                            //showProgress(false);
                            //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //VolleyLog.d("responseError", "Error: " + volleyError);
                //popup.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };

        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void callVisitorList(DashboardFilterModel dashboardFilterModel) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);

        String URL = Constants.BASE_URL+"Visits/?FilterDays="+dashboardFilterModel.getFilterDays()+"&GateNo="+dashboardFilterModel.getGateNo();
        if(dashboardFilterModel.getType().equalsIgnoreCase(Constants.VISITOR_ACTIVITY)) {
            //IsVisit=false&IsVisitRejected=false
            URL += "&ActiveVisitor=Security";

            if(!dashboardFilterModel.isVisitorIn()) {
                URL += "&IsVisit=true";
            } else {
                URL += "&IsVisit=false";
            }
            if(dashboardFilterModel.isSocietyPurpose()) {
                URL += "&SocietyPurpose=true";
            } else {
                URL += "&SocietyPurpose=false";
            }
            if(dashboardFilterModel.isVisitRejected()) {
                URL += "&IsVisitRejected=true";
            } else {
                URL += "&IsVisitRejected=false";
            }
        } else if(dashboardFilterModel.getType().equalsIgnoreCase(Constants.SECURITY_CONCERN)) {
            URL += "&IsVisitRejected="+dashboardFilterModel.isVisitRejected();
            if(!dashboardFilterModel.isOTPVerified()) {
                URL += "&IsOTPVerified="+dashboardFilterModel.isOTPVerified();
            }
        } else if(dashboardFilterModel.getType().equalsIgnoreCase(Constants.VISITOR_24_HRS)) {
            URL += "&IsVisit=false&Visitor24Hrs="+dashboardFilterModel.getVisitor24Hrs()+"&IsVisitRejected="+dashboardFilterModel.isVisitRejected();
        } else if(dashboardFilterModel.getType().equalsIgnoreCase(Constants.INCIDENCE_BYPASSED)) {
            URL += "&IsVisitRejected=false&BypassName=true";
        }

        Log.d("URL", URL);

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            popup.dismiss();
                            String valStr = " ";
                            JSONArray resourceList = object.getJSONArray("ResourceList");
                            if (resourceList.length() > 0) {
                                parseVisitorModel(resourceList);
                            } else {
                                //empty_list.setVisibility(View.VISIBLE);
                                //visitorView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Visitor list is empty", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //popup.dismiss();
                            //progress.setVisibility(View.GONE);
                            //showProgress(false);
                            //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //VolleyLog.d("responseError", "Error: " + volleyError);
                //popup.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };

        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void parseVisitorModel(JSONArray resourceList) {
        //ArrayList<VisitorModel> dataModel = new ArrayList<>();
        String[] dataModel = new String[resourceList.length()];

        try {
            for (int i = 0; i < resourceList.length(); i++) {
                VisitorModel visitorModel = new VisitorModel();
                Log.d("VISITOR", resourceList.get(i).toString());
                JSONObject visitorObj = resourceList.getJSONObject(i);
                String retVal = visitorObj.getString(Constants.CREATED_DATE);
                visitorModel.setInTime(retVal);

                //dataModel.add(visitorModel);
                dataModel[i] = "Name: " + visitorObj.getString(Constants.VISITOR_NAME) + "\n"
                        + "Mobile: " + visitorObj.getString(Constants.VISITOR_MOBILE_NO) + "\n"
                        + "Visitor Type: " + visitorObj.getString(Constants.VISITOR_TYPE) + "\n"
                        + "In Time: " + visitorModel.getIn_time() + "\n ----------";
            }

            Log.d("PARSE", dataModel.toString());
            //String[] abcd = {""};
            //showDialog("", abcd);

            showDialog("Visitors List", dataModel);
        } catch (Exception e) {
            Log.d("PARSE", e.getMessage());
        }

    }

    private void callVehicleList(String gateNo, String securityPerson, String filterDays, boolean societyPurpose, boolean visitorIn) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        String URL = Constants.BASE_URL+"Vehicles/?";
        if(!visitorIn) {
            URL += "IsVehicleClose=true";
        } else {
            URL += "IsVehicleClose=false";
        }
        if(societyPurpose) {
            URL += "&SocietyPurpose=true";
        } else {
            URL += "&SocietyPurpose=false";
        }

        Log.d("URL", URL);

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            popup.dismiss();
                            JSONArray resourceList = object.getJSONArray("ResourceList");
                            if (resourceList.length() > 0) {
                                parseVehicleModel(resourceList);
                            } else {
                                //empty_list.setVisibility(View.VISIBLE);
                                //visitorView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Vehicle list is empty", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            popup.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //VolleyLog.d("responseError", "Error: " + volleyError);
                popup.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };

        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    private void callVehicleList(DashboardFilterModel dashboardFilterModel) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        String URL = Constants.BASE_URL+"Vehicles/?FilterDays="+dashboardFilterModel.getFilterDays()+"&GateNo="+dashboardFilterModel.getGateNo();
        if(!dashboardFilterModel.isVisitorIn()) {
            URL += "&IsVehicleClose=true";
        } else {
            URL += "&IsVehicleClose=false";
        }
        if(dashboardFilterModel.isSocietyPurpose()) {
            URL += "&SocietyPurpose=true";
        } else {
            URL += "&SocietyPurpose=false";
        }

        Log.d("URL", URL);

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            popup.dismiss();
                            JSONArray resourceList = object.getJSONArray("ResourceList");
                            if (resourceList.length() > 0) {
                                parseVehicleModel(resourceList);
                            } else {
                                //empty_list.setVisibility(View.VISIBLE);
                                //visitorView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Vehicle list is empty", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            popup.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //VolleyLog.d("responseError", "Error: " + volleyError);
                popup.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };

        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void parseVehicleModel(JSONArray resourceList) {
        String[] dataModel = new String[resourceList.length()];

        try {
            for (int i = 0; i < resourceList.length(); i++) {
                VehicleModel vehicleModel = new VehicleModel();
                Log.d("VISITOR", resourceList.get(i).toString());
                JSONObject visitorObj = resourceList.getJSONObject(i);
                String retVal = visitorObj.getString(Constants.CREATED_DATE);
                vehicleModel.setInTime(retVal);

                //dataModel.add(visitorModel);
                dataModel[i] = "Visit ID : " + visitorObj.getString(Constants.VISIT_ID) + "\n"
                        + "Vehicle Type : " + visitorObj.getString(Constants.VISITOR_VEHICLE_TYPE) + "\n"
                        + "Vehicle Number : " + visitorObj.getString(Constants.VISITOR_VEHICLE_NO) + "\n"
                        + "No Of Vehicles: " + visitorObj.getString(Constants.NO_OF_VEHICLES) + "\n"
                        + "In Time: " + vehicleModel.getInDate() + "\n ----------";
            }

            //Log.d("PARSE", dataModel.toString());

            showDialog("Vehicle List", dataModel);
        } catch (Exception e) {
            Log.d("PARSE", e.getMessage());
        }

    }

    private void callMaterialList(String gateNo, String securityPerson, String filterDays, boolean societyPurpose, boolean visitorIn) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        String URL = Constants.BASE_URL+"SocietyInwordMaterials/?FilterDays="+filterDays+"&GateNo="+gateNo;

        if(!visitorIn) {
            URL += "&IsVisitClose=true";
        } else {
            URL += "&IsVisitClose=false";
        }

        Log.d("URL", URL);

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            popup.dismiss();
                            JSONArray resourceList = object.getJSONArray("ResourceList");
                            if (resourceList.length() > 0) {
                                parseMaterialModel(resourceList);
                            } else {
                                //empty_list.setVisibility(View.VISIBLE);
                                //visitorView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Material list is empty", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            popup.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //VolleyLog.d("responseError", "Error: " + volleyError);
                popup.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };

        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void callMaterialList(DashboardFilterModel dashboardFilterModel) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        String URL = Constants.BASE_URL+"SocietyInwordMaterials/?FilterDays="+dashboardFilterModel.getFilterDays()+"&GateNo="+dashboardFilterModel.getGateNo();

        if(!dashboardFilterModel.isVisitorIn()) {
            URL += "&IsVisitClose=true";
        } else {
            URL += "&IsVisitClose=false";
        }

        Log.d("URL", URL);

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            popup.dismiss();
                            JSONArray resourceList = object.getJSONArray("ResourceList");
                            if (resourceList.length() > 0) {
                                parseMaterialModel(resourceList);
                            } else {
                                //empty_list.setVisibility(View.VISIBLE);
                                //visitorView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Material list is empty", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            popup.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //VolleyLog.d("responseError", "Error: " + volleyError);
                popup.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };

        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void parseMaterialModel(JSONArray resourceList) {
        String[] dataModel = new String[resourceList.length()];

        try {
            for (int i = 0; i < resourceList.length(); i++) {
                MaterialModel materialModel = new MaterialModel();
                //Log.d("VISITOR", resourceList.get(i).toString());
                JSONObject visitorObj = resourceList.getJSONObject(i);
                String retVal = visitorObj.getString(Constants.CREATED_DATE);
                materialModel.setInDateTime(retVal);

                //dataModel.add(visitorModel);
                dataModel[i] = "Visit ID : " + visitorObj.getString(Constants.VISIT_ID) + "\n"
                        + "Name Of Staff : " + visitorObj.getString(Constants.NAME_OF_STAFF) + "\n"
                        + "Material Details : " + visitorObj.getString(Constants.MATERIAL_DETAILS) + "\n"
                        + "Material Storage Place: " + visitorObj.getString(Constants.MATERIAL_STORAGE_PLACE) + "\n"
                        + "In Time: " + materialModel.getInDateTime() + "\n ----------";
            }

            //Log.d("PARSE", dataModel.toString());

            showDialog("Material List", dataModel);
        } catch (Exception e) {
            Log.d("PARSE", e.getMessage());
        }

    }

    private void callStaffList(DashboardFilterModel dashboardFilterModel) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        String URL = Constants.BASE_URL+"DomesticStaffSubscription/?FilterDays="+dashboardFilterModel.getFilterDays()+"&GateNo="+dashboardFilterModel.getGateNo();
        if(dashboardFilterModel.getType().equalsIgnoreCase(Constants.SECURITY_CONCERN)) {
            URL += "&IsQrcode=false";
        }
        Log.d("URL", URL);

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            popup.dismiss();
                            JSONArray resourceList = object.getJSONArray("ResourceList");
                            if (resourceList.length() > 0) {
                                parseStaffModel(resourceList);
                            } else {
                                Toast.makeText(getActivity(), "Staff list is empty", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            popup.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //VolleyLog.d("responseError", "Error: " + volleyError);
                popup.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };

        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void parseStaffModel(JSONArray resourceList) {
        String[] dataModel = new String[resourceList.length()];

        try {
            for (int i = 0; i < resourceList.length(); i++) {
                StaffModel staffModel = new StaffModel();
                JSONObject staffObj = resourceList.getJSONObject(i);
                String retVal = staffObj.getString(Constants.CREATED_DATE);
                staffModel.setInDateTime(retVal);

                //dataModel.add(visitorModel);
                dataModel[i] = "Staff ID : " + staffObj.getString(Constants.STAFF_ID) + "\n"
                        + "Name Of Staff : " + staffObj.getString(Constants.STAFF_NAME) + "\n"
                        + "Remark : " + staffObj.getString(Constants.STAFF_REMARK) + "\n"
                        + "In Time: " + staffModel.getInDateTime() + "\n ----------";
            }

            //Log.d("PARSE", dataModel.toString());

            showDialog("Material List", dataModel);
        } catch (Exception e) {
            Log.d("PARSE", e.getMessage());
        }

    }



    private void callStaffAttendanceList(final DashboardFilterModel dashboardFilterModel) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        //http://103.48.51.62:8018/SecurityStaffAttendanceDashboard/?FilterDays=2&SocietyId=132&GateNo=0&Attendancetype=Absent
        String URL = Constants.BASE_URL+"SecurityStaffAttendanceDashboard/?FilterDays="
                +dashboardFilterModel.getFilterDays()
                +"&GateNo="+dashboardFilterModel.getGateNo()
                +"&SocietyId="+societyId
                +"&Attendancetype="+dashboardFilterModel.getType();
        Log.d("URL", URL);

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            //JSONObject object = new JSONObject(s);
                            popup.dismiss();
                            //JSONArray resourceList = object.getJSONArray();
                            JSONArray resourceList = new JSONArray(s);
                            if (resourceList.length() > 0) {
                                parseStaffAttendanceModel(resourceList, dashboardFilterModel);
                            } else {
                                Toast.makeText(getActivity(), "Staff list is empty", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            popup.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //VolleyLog.d("responseError", "Error: " + volleyError);
                popup.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };

        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void parseStaffAttendanceModel(JSONArray resourceList, DashboardFilterModel dashboardFilterModel) {
        String[] dataModel = new String[resourceList.length()];

        try {
            for (int i = 0; i < resourceList.length(); i++) {
                StaffAttendanceModel staffModel = new StaffAttendanceModel();
                JSONObject staffObj = resourceList.getJSONObject(i);
                String retVal = staffObj.getString(Constants.LOG_IN_DATE);
                staffModel.setLoginDateTime(retVal);

                retVal = staffObj.getString(Constants.LOG_OUT_DATE);
                staffModel.setLogOutDateTime(retVal);

                //dataModel.add(visitorModel);
                dataModel[i] = "Staff ID : " + staffObj.getString(Constants.USERID) + "\n";
                if(dashboardFilterModel.getType().equalsIgnoreCase(Constants.PROXY)) {
                    dataModel[i] += "Proxy Name : " + staffObj.getString(Constants.PROXY_NAME) + "\n";
                }
                dataModel[i] += "Name Of Staff : " + staffObj.getString(Constants.STAFF_NAME) + "\n"
                        + "Log In Time: " + staffModel.getLoginDateTime() + "\n"
                        + "Log Out Time: " + staffModel.getLogOutDateTime() + "\n ----------";
            }

            //Log.d("PARSE", dataModel.toString());

            showDialog("Staff List", dataModel);
        } catch (Exception e) {
            Log.d("PARSE", e.getMessage());
        }

    }

    private void visitorActivityPopup(int position) {
        DashboardFilterModel dashboardFilterModel = new DashboardFilterModel();
        dashboardFilterModel.setType(Constants.VISITOR_ACTIVITY);
        dashboardFilterModel.setGateNo(""+gateNo.getSelectedItemPosition());
        dashboardFilterModel.setSecurityPerson(""+securityPerson.getSelectedItemPosition());
        dashboardFilterModel.setFilterDays(""+filterDays.getSelectedItemPosition());
        switch (position) {
            //Residence Visitor IN
            case 9:
                dashboardFilterModel.setSocietyPurpose(false);
                dashboardFilterModel.setVisitorIn(true);
                dashboardFilterModel.setOTPVerified(false);
                dashboardFilterModel.setVisitRejected(false);
                //showVisitors(Constants.VISITOR_ACTIVITY,""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), false, true,false, false);
                showVisitors(dashboardFilterModel);
                break;
            //Residence Visitor OUT
            case 12:
                dashboardFilterModel.setSocietyPurpose(false);
                dashboardFilterModel.setVisitorIn(false);
                dashboardFilterModel.setVisitRejected(false);
                //showVisitors(Constants.VISITOR_ACTIVITY,""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), false, false, false, false);
                showVisitors(dashboardFilterModel);
                break;
            //Society Visitor IN
            case 17:
                dashboardFilterModel.setSocietyPurpose(true);
                dashboardFilterModel.setVisitorIn(true);
                dashboardFilterModel.setVisitRejected(false);
                //showVisitors(Constants.VISITOR_ACTIVITY,""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, true, false, false);
                showVisitors(dashboardFilterModel);
                break;
            //Society Visitor OUT
            case 20:
                dashboardFilterModel.setSocietyPurpose(true);
                dashboardFilterModel.setVisitorIn(false);
                dashboardFilterModel.setVisitRejected(false);
                //showVisitors(Constants.VISITOR_ACTIVITY,""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, false, false, false);
                showVisitors(dashboardFilterModel);
                break;
            //Society Material IN
            case 18:
                showMaterials(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, true);
                break;
            //Residence Vehicle IN
            case 11:
                showVehicles(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), false, true);
                break;
            //Residence Vehicle OUT
            case 14:
                showVehicles(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), false, false);
                break;
            //Society Vehicle IN
            case 19:
                showVehicles(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, true);
                break;
            //Society Vehicle OUT
            case 22:
                showVehicles(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, false);
                break;
            //Residence Staff IN
            case 25:
                showStaff(dashboardFilterModel);
                //showStaff(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, true);
                break;
            //Society Staff IN
            case 33:
                showStaff(dashboardFilterModel);
                //showStaff(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, true);
                break;
        }
    }


    private void securityConcernPopup(int position) {
        DashboardFilterModel dashboardFilterModel = new DashboardFilterModel();
        dashboardFilterModel.setType(Constants.SECURITY_CONCERN);
        //dashboardFilterModel.setGateNo(""+gateNo.getSelectedItemPosition());
        dashboardFilterModel.setSecurityPerson(""+securityPerson.getSelectedItemPosition());
        dashboardFilterModel.setFilterDays(""+filterDays.getSelectedItemPosition());

        Log.d("POSITION", ""+position);
        final int gateSize = gateItems.length+2;
        //OTP   (6,7,8,9)           -> (5-9)
        if(position > (1+gateItems.length) && position <= (1+(2*gateItems.length))) {
            showVisitors(Constants.SECURITY_CONCERN,""+(position - (2+gateItems.length)), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), false, true, false, false);
        }
        //NO ID  (11,12,13,14)      -> (10-14)
        if(position > (2+(2*gateItems.length)) && position <= (2+(3*gateItems.length))) {
            dashboardFilterModel.setType(Constants.SECURITY_CONCERN);
            dashboardFilterModel.setGateNo(""+(position - (3+(2*gateItems.length))));
            showStaff(dashboardFilterModel);
            //http://103.48.51.62:8018/DomesticStaffSubscription/?IsQrcode=false
            //showSecurity(Constants.SECURITY_CONCERN,""+(position - (3+(2*gateItems.length))), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), false, true, false, false);
        }
        //(16,17,18,19)
        //ByPass Incidence  (21,22,23,24)  ->(20-24)
        if(position > (4+(4*gateItems.length)) && position <= (4+(5*gateItems.length))) {
            //showVisitors(Constants.SECURITY_CONCERN,""+(position - (5+(4*gateItems.length))), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), false, true, false, false);
            dashboardFilterModel.setType(Constants.INCIDENCE_BYPASSED);
            dashboardFilterModel.setGateNo(""+(position - (5+(4*gateItems.length))));
            showVisitors(dashboardFilterModel);
        }
        //Rejected Incidence  (26,27,28,29) ->(25-29)
        if(position > (5+(5*gateItems.length)) && position <= (5+(6*gateItems.length))) {
            showVisitors(Constants.SECURITY_CONCERN,""+(position - (6+(5*gateItems.length))), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), false, true, true, true);
        }
        //(31,32,33,34)
        //Visitor 24 Hrs(36,37,38,39)  ->(35-39)
        //Log.d("POS", ""+(1+(9*gateItems.length)) + " - " + position + " - " + (1+(10*gateItems.length)));
        if(position > (7+(7*gateItems.length)) && position <= (7+(8*gateItems.length))) {
            dashboardFilterModel.setType(Constants.VISITOR_24_HRS);
            dashboardFilterModel.setGateNo(""+(position - (8+(7*gateItems.length))));
            dashboardFilterModel.setVisitRejected(false);
            dashboardFilterModel.setVisitor24Hrs("1");
            showVisitors(dashboardFilterModel);
        }

        //Material 24 hrs  (41,42,43,44)  -> (40-44)
        //Log.d("POS", ""+(1+(9*gateItems.length)) + " - " + (1+(10*gateItems.length)));
        //http://103.48.51.62:8018/SocietyInwordMaterials/?GateNo=1&FilterDays=0
        if(position > (8+(8*gateItems.length)) && position <= (8+(9*gateItems.length))) {
            dashboardFilterModel.setType(Constants.MATERIALS_24_HRS);
            dashboardFilterModel.setGateNo(""+(position - (9+(8*gateItems.length))));
            showMaterials(dashboardFilterModel);
        }

        //Vehicle 24 hrs  (46,47,48,49)  -> (45-49)
        if(position > (9+(9*gateItems.length)) && position <= (9+(10*gateItems.length))) {
            dashboardFilterModel.setType(Constants.VEHICLES_24_HRS);
            dashboardFilterModel.setGateNo(""+(position - (10+(9*gateItems.length))));
            dashboardFilterModel.setSocietyPurpose(true);
            showVehicles(dashboardFilterModel);
        }

        //?Visitor24Hrs=1&IsVisit=false&GateNo=1&FilterDays=0&IsVisitRejected=false
        /*
        switch (position) {
            //OTP NOT VERIFIED ALL
            case (gateItems.length+2):
                showVisitors(Constants.SECURITY_CONCERN,""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), false, true, false, false);
                break;
            //OTP NOT VERIFIED G1
            case 7:
                showVisitors(Constants.SECURITY_CONCERN,""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), false, true, false, false);
                break;
            //Residence Visitor OUT
            case 12:
                showVisitors(Constants.SECURITY_CONCERN,""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), false, false,false, false);
                break;
            //Society Visitor IN
            case 17:
                showVisitors(Constants.SECURITY_CONCERN,""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, true, false, false);
                break;
            //Society Visitor OUT
            case 20:
                showVisitors(Constants.SECURITY_CONCERN,""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, false, false, false);
                break;
            //Society Material IN
            case 18:
                showMaterials(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, true);
                break;
            //Residence Vehicle IN
            case 11:
                showVehicles(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), false, true);
                break;
            //Residence Vehicle OUT
            case 14:
                showVehicles(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), false, false);
                break;
            //Society Vehicle IN
            case 19:
                showVehicles(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, true);
                break;
            //Society Vehicle OUT
            case 22:
                showVehicles(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, false);
                break;
            //Residence Staff IN
            case 25:
                showStaff(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, true);
                break;
            //Society Staff IN
            case 33:
                showStaff(""+gateNo.getSelectedItemPosition(), ""+securityPerson.getSelectedItemPosition(), ""+filterDays.getSelectedItemPosition(), true, true);
                break;
        }*/
    }


    private void staffAttendancePopup(int position) {
        DashboardFilterModel dashboardFilterModel = new DashboardFilterModel();
        dashboardFilterModel.setType(Constants.PRESENT);
        dashboardFilterModel.setSecurityPerson(""+securityPerson.getSelectedItemPosition());
        dashboardFilterModel.setFilterDays(""+filterDays.getSelectedItemPosition());

        Log.d("POSITION", ""+position);
        //final int gateSize = gateItems.length+2;
        if(position > (1+gateItems.length) && position <= (1+(2*gateItems.length))) {
            dashboardFilterModel.setGateNo(""+(position - ((2+gateItems.length))));
            showStaffAttendance(dashboardFilterModel);
        }
        if(position > (2+(2*gateItems.length)) && position <= (2+(3*gateItems.length))) {
            dashboardFilterModel.setType(Constants.ABSENT);
            dashboardFilterModel.setGateNo(""+(position - (3+(2*gateItems.length))));
            showStaffAttendance(dashboardFilterModel);
        }
        if(position > (3+(3*gateItems.length)) && position <= (3+(4*gateItems.length))) {
            dashboardFilterModel.setType(Constants.PROXY);
            dashboardFilterModel.setGateNo(""+(position - (4+(3*gateItems.length))));
            showStaffAttendance(dashboardFilterModel);
        }
    }


}
