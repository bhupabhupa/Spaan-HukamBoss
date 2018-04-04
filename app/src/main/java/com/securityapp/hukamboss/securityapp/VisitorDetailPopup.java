package com.securityapp.hukamboss.securityapp;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.securityapp.hukamboss.securityapp.adapters.PastIncidencesAdapter;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.model.PastIncidenceModel;
import com.securityapp.hukamboss.securityapp.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bhupa on 07/02/18.
 */

public class VisitorDetailPopup extends DialogFragment {
    private ListView listView = null;
    private ArrayList<PastIncidenceModel> dataModels;
    private static PastIncidencesAdapter adapter;

    private String accessToken;
    private String societyId;
    private String visitorMobile;

    private ProgressBar progressView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_incidences_popup, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        visitorMobile  = getArguments().getString(Constants.VISITOR_MOBILE_NO);

        progressView = view.findViewById(R.id.progress);


        listView = (ListView) view.findViewById(R.id.pi_list);

        getIncidence(accessToken, societyId, visitorMobile);
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
            for (int i =0; i < resourceList.length(); i++) {
                JSONObject resource = resourceList.getJSONObject(i);

                dataModels.add(new PastIncidenceModel("31 Dec, 2017", "10:30 PM", "A, 202", resource.getString(Constants.NOTES)));
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
                        popup.dismiss();
                        //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
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

        //showProgress(true);
        //VolleyController commInstanceObj = VolleyController.getInstance(this);
        //commInstanceObj.getRequestQueue().add(jsonObjReq);
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
