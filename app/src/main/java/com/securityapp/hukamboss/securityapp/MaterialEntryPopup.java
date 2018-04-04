package com.securityapp.hukamboss.securityapp;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bhupa on 21/02/18.
 */

public class MaterialEntryPopup extends DialogFragment {
    private String accessToken;
    private String societyId;
    private String visitId;

    private ProgressBar progressView;
    TextView noData;
    LinearLayout dataLayout;
    TextView staffName, materialDetails, materialStorage;
    ImageLoader mimageLoader = null;
    NetworkImageView image1, image2;
    ImageView closeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.material_entry_popup, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        visitId = getArguments().getString(Constants.VISIT_ID);

        progressView = view.findViewById(R.id.progress);

        staffName = view.findViewById(R.id.staff_name);
        materialDetails = view.findViewById(R.id.material_details);
        materialStorage = view.findViewById(R.id.material_storage);
        image1 = view.findViewById(R.id.material_details_pic);
        image2 = view.findViewById(R.id.material_storage_pic);

        noData = view.findViewById(R.id.no_data);
        dataLayout = view.findViewById(R.id.data_layout);

        getMaterial(accessToken, societyId, visitId);

        /*Button closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });*/

        closeButton = view.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(closePopupListener);

        return view;
    }

    private void getMaterial(final String accessToken, String societyId, final String visitId) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        String URL = Constants.BASE_URL+"SocietyInwordMaterials/?SocietyId="+societyId+"&VisitId="+visitId;
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
                            if(resourceList.length() == 0) {
                                noData.setText("No Material Entry.");
                                dataLayout.setVisibility(View.GONE);
                            } else  {
                                noData.setVisibility(View.GONE);
                                JSONObject resource = resourceList.getJSONObject(0);

                                String retVal = resource.getString(Constants.NAME_OF_STAFF);
                                staffName.setText(retVal);

                                retVal = resource.getString(Constants.MATERIAL_DETAILS);
                                materialDetails.setText(retVal);

                                retVal = resource.getString(Constants.MATERIAL_STORAGE_PLACE);
                                materialStorage.setText(retVal);

                                if (null == mimageLoader)
                                {
                                    mimageLoader = VolleyController.getInstance(getActivity()).getImageLoader();
                                }

                                String URL = Constants.BASE_URL + "InwordMaterialChalanImages/" + visitId + ".jpeg";
                                image1.setImageUrl(URL, mimageLoader);

                                URL = Constants.BASE_URL + "InwordMaterialImages/" + visitId + ".jpeg";
                                image2.setImageUrl(URL, mimageLoader);
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


}


