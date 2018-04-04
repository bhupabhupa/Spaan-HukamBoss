package com.securityapp.hukamboss.securityapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.securityapp.hukamboss.securityapp.adapters.PastIncidencesAdapter;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.model.PastIncidenceModel;
import com.securityapp.hukamboss.securityapp.model.VisitorHomeFragmentModel;
import com.securityapp.hukamboss.securityapp.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PastIncidencesPopup extends DialogFragment {
    private ListView listView = null;
    private ArrayList<PastIncidenceModel> dataModels;
    private static PastIncidencesAdapter adapter;

    private String accessToken;
    private String societyId;
    private String visitorMobile;
    private String jsonArrayStr;

    JSONArray jsonArray;


    private ProgressBar progressView;
    TextView noIncidence;
    ImageView closeButton;
    Button bypassBtn, rejectBtn;
    //VisitorHomeFragmentModel vhfModel;
    private int pastListSize = 0;
    android.app.Fragment myFrag;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_incidences_popup, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        visitorMobile  = getArguments().getString(Constants.VISITOR_MOBILE_NO);

        jsonArrayStr  = getArguments().getString(Constants.PAST_INCIDENCE);

        try {
            jsonArray = new JSONArray(jsonArrayStr);
        } catch (JSONException e) {}

        myFrag = getTargetFragment();
        //Log.d("FRAGMENT", myFrag.toString());
        //vhfModel = (VisitorHomeFragmentModel) getArguments().getSerializable(Constants.VHFMODEL);

        progressView = view.findViewById(R.id.progress);

        noIncidence = view.findViewById(R.id.no_incidence);


        listView = (ListView) view.findViewById(R.id.pi_list);
        closeButton = view.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(closePopupListener);

        bypassBtn = view.findViewById(R.id.bypass_btn);
        bypassBtn.setVisibility(View.GONE);
        bypassBtn.setOnClickListener(bypassBtnListener);

        rejectBtn = view.findViewById(R.id.reject_btn);
        rejectBtn.setVisibility(View.GONE);
        rejectBtn.setOnClickListener(rejectBtnListener);

        populatePastIncidences(jsonArray);

        //getIncidence(accessToken, societyId, visitorMobile);
        //populatePastIncidences();

        /*Button close = (Button)view.findViewById(R.id.pi_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });*/



        return view;
    }

    public void populatePastIncidences(JSONArray resourceList) {
        dataModels = new ArrayList<>();
        try {
            pastListSize = resourceList.length();
            for (int i =0; i < resourceList.length(); i++) {
                //vhfModel.setHasPastIncidence(true);
                JSONObject resource = resourceList.getJSONObject(i);

                String rawDate = resource.getString(Constants.INCIDENCE_DATE);
                String []tempVal = rawDate.split("T");
                Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(tempVal[0]);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String inDate = dateFormat.format(date1);
                Date date2 = new SimpleDateFormat("HH:mm:ss").parse(tempVal[1]);
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aaa");
                String inTime = timeFormat.format(date2);

                String notes = resource.getString(Constants.NOTES);

                dataModels.add(new PastIncidenceModel(inDate, inTime, "A, 202", notes));
            }
        } catch (Exception e) {}



        /*
        dataModels.add(new PastIncidenceModel("31 Dec, 2017", "10:30 PM", "A, 202", "Rude Behaviour"));
        dataModels.add(new PastIncidenceModel("1 Jan, 2018", "1:30 AM", "D, 202", "Abusive Behaviour"));
        dataModels.add(new PastIncidenceModel("3 Jan, 2018", "4:15 PM", "A, 202", "Abusive Behaviour"));
        dataModels.add(new PastIncidenceModel("5 Jan, 2018", "3:20 PM", "A, 202", "Rude Behaviour"));
        dataModels.add(new PastIncidenceModel("5 Jan, 2018", "10:30 PM", "A, 202", "Rude Behaviour"));
        dataModels.add(new PastIncidenceModel("31 Dec, 2017", "10:30 PM", "A, 202", "Rude Behaviour"));
        dataModels.add(new PastIncidenceModel("1 Jan, 2018", "1:30 AM", "D, 202", "Abusive Behaviour"));
        dataModels.add(new PastIncidenceModel("3 Jan, 2018", "4:15 PM", "A, 202", "Abusive Behaviour"));
        dataModels.add(new PastIncidenceModel("5 Jan, 2018", "3:20 PM", "A, 202", "Rude Behaviour"));
        dataModels.add(new PastIncidenceModel("5 Jan, 2018", "10:30 PM", "A, 202", "Rude Behaviour"));
        */

        if(dataModels.size() == 0) {
            noIncidence.setVisibility(View.VISIBLE);
            bypassBtn.setVisibility(View.GONE);
            rejectBtn.setVisibility(View.GONE);
        } else {
            noIncidence.setVisibility(View.GONE);
            if(myFrag != null) {
                bypassBtn.setVisibility(View.VISIBLE);
                rejectBtn.setVisibility(View.VISIBLE);
            }

        }

        adapter = new PastIncidencesAdapter(dataModels, getActivity().getApplicationContext());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PastIncidenceModel dataModel= dataModels.get(position);

                Snackbar.make(view, dataModel.getDate()+"\n"+dataModel.getTime(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }


    private void getIncidence(final String accessToken, String societyId, String telephone) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        //String URL = "http://103.48.51.62:8018/SecurityIncidences/?MobileNo="+telephone;
        String URL = Constants.BASE_URL+"SecurityIncidences/?MobileNo="+telephone;
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //showProgress(false);
                        //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                        popup.dismiss();
                        try {
                            JSONObject object = new JSONObject(s);

                            JSONArray resourceList = object.getJSONArray("ResourceList");

                            Log.d("INCIDENCE LIST", ""+object.toString());
                            Log.d("INCIDENCE LIST", ""+resourceList.length());

                            if(resourceList.length() == 0) {
                                //incidenceText = "No incidence associated with the member";
                            } else  {
                                //incidenceText = "";
                                //dataModels = new ArrayList<>();
                                populatePastIncidences(resourceList);
                                /*for (int i=0; i<resourceList.length();i++) {
                                    JSONObject resource = resourceList.getJSONObject(i);
                                    Log.d("LIST ", resource.toString());
                                    //dataModels.add(new PastIncidenceModel("5 Jan, 2018", "1:30 AM", "A, 202", resource.getString(Constants.NOTES)));
                                    //incidenceText += ((i+1) +". " + resource.getString("Notes") + "\n");
                                }*/

                                /*adapter = new PastIncidencesAdapter(dataModels, getActivity().getApplicationContext());

                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        PastIncidenceModel dataModel= dataModels.get(position);

                                        Snackbar.make(view, dataModel.getDate()+"\n"+dataModel.getTime(), Snackbar.LENGTH_LONG)
                                                .setAction("No action", null).show();
                                    }
                                });
                                */
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //progress.setVisibility(View.GONE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                popup.dismiss();
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

        //VolleyController commInstanceObj = VolleyController.getInstance(this);
        //commInstanceObj.getRequestQueue().add(jsonObjReq);
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
            if(myFrag == null) {
                dismiss();
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PAST_INCIDENCE, ""+pastListSize);
                Intent intent = new Intent().putExtras(bundle);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, intent);
                dismiss();
            }

        }
    };

    private View.OnClickListener bypassBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PAST_INCIDENCE, ""+pastListSize);
            Intent intent = new Intent().putExtras(bundle);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            dismiss();
        }
    };



    private View.OnClickListener rejectBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PAST_INCIDENCE, ""+pastListSize);
            Intent intent = new Intent().putExtras(bundle);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.DEFAULT_KEYS_SHORTCUT, intent);
            dismiss();
        }
    };


}
