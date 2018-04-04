package com.securityapp.hukamboss.securityapp;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
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
import com.securityapp.hukamboss.securityapp.constants.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bhupa on 09/03/18.
 */

public class CustomProgressBar  extends DialogFragment {

    private ProgressBar progressView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custome_progress_bar, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        progressView = view.findViewById(R.id.progress);

        return view;
    }




}


