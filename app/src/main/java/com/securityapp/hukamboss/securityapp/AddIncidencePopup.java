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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.securityapp.hukamboss.securityapp.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bhupa on 16/03/18.
 */

public class AddIncidencePopup extends DialogFragment {
    private String accessToken;
    private String societyId;
    private String visitId;
    private String societyGateNo;
    private String visitorId;

    ImageView closeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_incidence_popup, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        visitId = getArguments().getString(Constants.VISIT_ID);
        societyGateNo = getArguments().getString(Constants.SOCIETY_GATE_NO);
        visitorId = getArguments().getString(Constants.VISITOR_ID);

        final EditText notes = view.findViewById(R.id.incidence);

        Button saveButton = (Button) view.findViewById(R.id.save_incidence);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.VISIT_ID, visitId);
                params.put(Constants.VISITORID, visitorId);
                params.put(Constants.NOTES, notes.getEditableText().toString());
                addIncidenceAtServer(params);
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

    private void addIncidenceAtServer(final Map<String, String> myParams) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST,Constants.BASE_URL+"SecurityIncidences",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            Log.d("RESPONSE SUCCESS - ", "" + s.toString());
                            popup.dismiss();

                            Toast.makeText(getActivity().getApplicationContext(), "Incidence added successfully!!", 5000).show();

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
                            dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView, parentScroll);
                popup.dismiss();
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


