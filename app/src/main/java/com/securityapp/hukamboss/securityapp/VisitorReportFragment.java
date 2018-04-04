package com.securityapp.hukamboss.securityapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.securityapp.hukamboss.securityapp.adapters.StaffListAdapter;
import com.securityapp.hukamboss.securityapp.adapters.VehicleListAdapter;
import com.securityapp.hukamboss.securityapp.adapters.VisitorAdapter;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.interfaces.VisitorInterface;
import com.securityapp.hukamboss.securityapp.model.StaffModel;
import com.securityapp.hukamboss.securityapp.model.VehicleModel;
import com.securityapp.hukamboss.securityapp.model.VisitorModel;
import com.securityapp.hukamboss.securityapp.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VisitorReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VisitorReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisitorReportFragment extends Fragment implements VisitorInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //private ListView listView = null;
    private ListView listView = null;
    private ListView staffListView = null;
    private ListView vehcileListView = null;
    private ArrayList<VisitorModel> dataModels;
    private ArrayList<StaffModel> staffDataModels;
    private ArrayList<VehicleModel> vehicleDataModels;
    private static VisitorAdapter adapter;
    private static StaffListAdapter staffAdapter;
    private static VehicleListAdapter vehicleAdapter;

    private static final int CLOSE_VISIT_REQUEST_CODE = 8;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    String accessToken;
    String societyId;
    private String societyGateNo;
    ProgressBar progressView;
    TextView empty_list;
    private EditText searchView;

    private Button visitorBtn,staffBtn, vehicleBtn;

    public VisitorReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VisitorReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VisitorReportFragment newInstance(String param1, String param2) {
        VisitorReportFragment fragment = new VisitorReportFragment();
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
        View view = inflater.inflate(R.layout.fragment_visitor_report, container, false);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        societyGateNo = getArguments().getString(Constants.SOCIETY_GATE_NO);

        progressView = view.findViewById(R.id.progress);

        empty_list = view.findViewById(R.id.empty_list);
        empty_list.setText("Visitor list is empty.");

        listView = view.findViewById(R.id.vr_visitor_list);
        staffListView = view.findViewById(R.id.vr_staff_list);
        vehcileListView = view.findViewById(R.id.vr_vehicle_list);

        //listView.setVisibility(View.VISIBLE);
        staffListView.setVisibility(View.GONE);
        vehcileListView.setVisibility(View.GONE);

        visitorBtn = view.findViewById(R.id.visitor_list_btn);
        staffBtn = view.findViewById(R.id.staff_list_btn);
        vehicleBtn = view.findViewById(R.id.vehicle_list_btn);

        visitorBtn.setOnClickListener(btnListener);
        staffBtn.setOnClickListener(btnListener);
        vehicleBtn.setOnClickListener(btnListener);

        visitorBtn.setBackgroundResource(R.drawable.blue_curve_button);

        searchView = (EditText)view.findViewById(R.id.vr_search);
        searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //  Toast.makeText(getActivity(), cs.toString(), Toast.LENGTH_LONG).show();
                adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        visitorListCall();
        //staffListCall();
        //vehicleListCall();
        return view;
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

    private void visitorListCall() {
        //progress.setVisibility(View.VISIBLE);

        //showProgress(true);
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        //String URL = "http://103.48.51.62:8018/Visitors";
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        //String URL = Constants.BASE_URL+"Visits/?ActiveVisitor=Security&IsVisitRejected=false";
        String URL = Constants.BASE_URL+"Visits/?IsVisit=false&IsVisitRejected=false&SortedBy=Id&SortDir=desc";
        Log.d("URL", URL);
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            //progress.setVisibility(View.GONE);
                            //showProgress(false);
                            //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                            popup.dismiss();
                            String valStr = " ";
                            JSONArray resourceList = object.getJSONArray("ResourceList");
                            if (resourceList.length() > 0) {
                                populateVisitorList(resourceList);
                            } else {
                                empty_list.setVisibility(View.VISIBLE);
                                //visitorView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Visitor list is empty", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            popup.dismiss();
                            //progress.setVisibility(View.GONE);
                            //showProgress(false);
                            //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
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
        queue.getCache().clear();
        queue.add(request);
    }


    private void populateVisitorList(JSONArray resourceList) {
        dataModels= new ArrayList<>();
        String errorID = "";
        try {
            for (int i=0; i<resourceList.length();i++) {
                VisitorModel vm = new VisitorModel();
                Log.d("VISITOR", resourceList.get(i).toString());
                JSONObject visitorObj = resourceList.getJSONObject(i);
                String retVal = visitorObj.getString(Constants.VISITOR_NAME) == null ? "" : visitorObj.getString(Constants.VISITOR_NAME);
                vm.setName(retVal);
                vm.setImage("");

                retVal = visitorObj.getString(Constants.VISITOR_MOBILE_NO) == null ? "" : visitorObj.getString(Constants.VISITOR_MOBILE_NO);
                vm.setMobile(retVal);

                retVal = visitorObj.getString(Constants.CREATED_DATE) == null ? "" : visitorObj.getString(Constants.CREATED_DATE);
                String []tempVal = retVal.split("T");
                Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(tempVal[0]);
                /*SimpleDateFormat weekDayFormat = new SimpleDateFormat("dd");
                SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");*/
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                //String inDate = weekDayFormat.format(date1) + "-" + monthFormat.format(date1) + "-" + yearFormat.format(date1);
                String inDate = dateFormat.format(date1);
                Date date2 = new SimpleDateFormat("HH:mm:ss").parse(tempVal[1]);
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aaa");
                //String inTime = date2.getHours() + ":" + date2.getMinutes() + ":" + date2.getSeconds();
                String inTime = timeFormat.format(date2);
                vm.setIn_time(inDate + "\n" +inTime);

                vm.setOut_time("04:45 PM");

                retVal = visitorObj.getString(Constants.ID) == null ? "" : visitorObj.getString(Constants.ID);
                errorID = retVal;
                vm.setVisitId(retVal);

                retVal = visitorObj.getString(Constants.VISITOR_TYPE) == null ? "" : visitorObj.getString(Constants.VISITOR_TYPE);
                vm.setType(retVal);

                //retVal = visitorObj.getString(Constants.VISITOR_PURPOSE) == null ? "" : visitorObj.getString(Constants.VISITOR_PURPOSE);
                vm.setPurpose("Personal");

                vm.setIn_gate("1");
                vm.setOut_gate("2");

                retVal = visitorObj.getString(Constants.VISITORID) == null ? "" : visitorObj.getString(Constants.VISITORID);
                vm.setVisitorId(retVal);

                retVal = visitorObj.getString(Constants.HAS_VEHICLE) == null ? "" : visitorObj.getString(Constants.HAS_VEHICLE);
                vm.setHasVehicle(retVal);

                dataModels.add(vm);

                //VisitorModel vm1 = new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2");
                //dataModels.add(vm1);
            }

        } catch (Exception e) {
            Log.d("VISITOR ERROR", errorID);
            Log.d("VISITOR ERROR", e.getMessage());
        }



        /*
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        dataModels.add(new VisitorModel("", "name1", "mobile1", "10:30 AM", "4:45 PM", "type", "purpose", "1", "2"));
        */

        FragmentManager fm = getFragmentManager();
        adapter = new VisitorAdapter(dataModels, getActivity().getApplicationContext(), fm, accessToken, societyId, societyGateNo, this);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                VisitorModel dataModel= dataModels.get(position);

                Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });

        //listView.setOnScrollChangeListener(scrollChangeListener);


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

    @Override
    public void didClickImage(final String imageUrl, final ImageLoader imageLoader) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.visitorpopup, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        NetworkImageView image = dialog.findViewById(R.id.bigimage);
        image.setImageUrl(imageUrl, imageLoader);

        dialog.show();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
            }
        });
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CLOSE_VISIT_REQUEST_CODE) {
            //Toast.makeText(getActivity(), "VISIT CLOSING.....", 5000).show();
            adapter.refreshEvents(dataModels);
            //visitorListCall();


        }
    }*/


    private void staffListCall() {
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        String URL = Constants.BASE_URL+"DomesticStaffSubscription/?StaffInn=true&StaffOut=false&SortedBy=Id&SortDir=desc";
        Log.d("URL", URL);
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                            String valStr = " ";
                            JSONArray resourceList = object.getJSONArray("ResourceList");
                            if (resourceList.length() > 0) {
                                populateStaffList(resourceList);
                            } else {
                                empty_list.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "Staff list is empty", Toast.LENGTH_SHORT).show();
                            }
                            popup.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            popup.dismiss();
                            //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
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

        };
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void populateStaffList(JSONArray resourceList) {
        staffDataModels= new ArrayList<>();
        try {
            for (int i=0; i<resourceList.length();i++) {
                StaffModel vm = new StaffModel();
                Log.d("STAFF", resourceList.get(i).toString());
                JSONObject visitorObj = resourceList.getJSONObject(i);
                String retVal = visitorObj.getString(Constants.STAFF_ID) == null ? "" : visitorObj.getString(Constants.STAFF_ID);
                vm.setStaffID(retVal);

                retVal = visitorObj.getString(Constants.ID) == null ? "" : visitorObj.getString(Constants.ID);
                vm.setId(retVal);

                retVal = visitorObj.getString(Constants.STAFF_FORM) == null ? "" : visitorObj.getString(Constants.STAFF_FORM);
                vm.setStaffForm(retVal);

                //retVal = visitorObj.getString(Constants.STAFF_NAME) == null ? "" : visitorObj.getString(Constants.VISITOR_MOBILE_NO);
                vm.setStaffName("NA");

                retVal = visitorObj.getString(Constants.CREATED_DATE) == null ? "" : visitorObj.getString(Constants.CREATED_DATE);
                String []tempVal = retVal.split("T");
                Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(tempVal[0]);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String inDate = dateFormat.format(date1);
                Date date2 = new SimpleDateFormat("HH:mm:ss").parse(tempVal[1]);
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aaa");
                String inTime = timeFormat.format(date2);
                vm.setInDate(inDate + "\n" +inTime);

                staffDataModels.add(vm);
            }

        } catch (Exception e) {
            //Log.d("STAFF ERROR", errorID);
            Log.d("STAFF ERROR", e.getMessage());
        }

        FragmentManager fm = getFragmentManager();
        //adapter = new VisitorAdapter(dataModels, getActivity().getApplicationContext(), fm, accessToken, societyId, societyGateNo, this);
        staffAdapter = new StaffListAdapter(staffDataModels, getActivity().getApplicationContext(), fm, accessToken, societyId, societyGateNo);

        staffListView.setAdapter(staffAdapter);

        staffListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                StaffModel dataModel= staffDataModels.get(position);

                Snackbar.make(view, dataModel.getStaffName()+"\n"+dataModel.getStaffID(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.visitor_list_btn:
                    visitorListCall();
                    listView.setVisibility(View.VISIBLE);
                    staffListView.setVisibility(View.GONE);
                    vehcileListView.setVisibility(View.GONE);
                    visitorBtn.setBackgroundResource(R.drawable.blue_curve_button);
                    staffBtn.setBackgroundResource(R.drawable.light_blue_button);
                    vehicleBtn.setBackgroundResource(R.drawable.light_blue_button);
                    break;
                case R.id.staff_list_btn:
                    staffListCall();
                    listView.setVisibility(View.GONE);
                    staffListView.setVisibility(View.VISIBLE);
                    vehcileListView.setVisibility(View.GONE);
                    visitorBtn.setBackgroundResource(R.drawable.light_blue_button);
                    staffBtn.setBackgroundResource(R.drawable.blue_curve_button);
                    vehicleBtn.setBackgroundResource(R.drawable.light_blue_button);
                    break;
                case R.id.vehicle_list_btn:
                    vehicleListCall();
                    listView.setVisibility(View.GONE);
                    staffListView.setVisibility(View.GONE);
                    vehcileListView.setVisibility(View.VISIBLE);
                    visitorBtn.setBackgroundResource(R.drawable.light_blue_button);
                    staffBtn.setBackgroundResource(R.drawable.light_blue_button);
                    vehicleBtn.setBackgroundResource(R.drawable.blue_curve_button);
                    break;
            }
        }
    };

    private void vehicleListCall() {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        String URL = Constants.BASE_URL+"Vehicles/?IsVehicleClose=false&SortedBy=Id&SortDir=desc";
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            popup.dismiss();
                            JSONArray resourceList = object.getJSONArray("ResourceList");
                            if (resourceList.length() > 0) {
                                populateVehicleList(resourceList);
                            } else {
                                empty_list.setVisibility(View.VISIBLE);
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


    private void populateVehicleList(JSONArray resourceList) {
        vehicleDataModels= new ArrayList<>();
        String errorID = "";
        try {
            for (int i=0; i<resourceList.length();i++) {
                VehicleModel vm = new VehicleModel();
                Log.d("VISITOR", resourceList.get(i).toString());
                JSONObject visitorObj = resourceList.getJSONObject(i);
                String retVal = visitorObj.getString(Constants.VISIT_ID) == null ? "" : visitorObj.getString(Constants.VISIT_ID);
                vm.setVisitID(retVal);

                retVal = visitorObj.getString(Constants.VISITOR_VEHICLE_NO) == null ? "" : visitorObj.getString(Constants.VISITOR_VEHICLE_NO);
                vm.setVehicleNo(retVal);

                retVal = visitorObj.getString(Constants.VISITOR_VEHICLE_TYPE) == null ? "" : visitorObj.getString(Constants.VISITOR_VEHICLE_TYPE);
                vm.setVehicleType(retVal);

                retVal = visitorObj.getString(Constants.NO_OF_VEHICLES) == null ? "" : visitorObj.getString(Constants.NO_OF_VEHICLES);
                vm.setNoOfVehicles(retVal);

                retVal = visitorObj.getString(Constants.CREATED_DATE) == null ? "" : visitorObj.getString(Constants.CREATED_DATE);
                String []tempVal = retVal.split("T");
                Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(tempVal[0]);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String inDate = dateFormat.format(date1);
                Date date2 = new SimpleDateFormat("HH:mm:ss").parse(tempVal[1]);
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aaa");
                String inTime = timeFormat.format(date2);
                vm.setInDate(inDate + "\n" +inTime);

                vehicleDataModels.add(vm);
            }

        } catch (Exception e) {
            Log.d("VISITOR ERROR", e.getMessage());
        }

        FragmentManager fm = getFragmentManager();
        vehicleAdapter = new VehicleListAdapter(vehicleDataModels, getActivity().getApplicationContext(), fm, accessToken, societyId, societyGateNo);

        vehcileListView.setAdapter(vehicleAdapter);

        vehcileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                VehicleModel dataModel= vehicleDataModels.get(position);

                Snackbar.make(view, dataModel.getVisitID()+"\n"+dataModel.getVehicleNo(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }

}
