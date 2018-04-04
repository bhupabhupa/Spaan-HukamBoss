package com.securityapp.hukamboss.securityapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.model.VisitorModel;

import java.net.URL;

/**
 * Created by Bhupa on 27/02/18.
 */

public class VisitorPhotoPopup extends DialogFragment {
    private String accessToken;
    private String societyId;
    private String url;
    private String title;

    private ProgressBar progressView;
    ImageView closeButton;
    NetworkImageView visitorPic;

    ImageLoader mimageLoader = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.visitor_photo_popup, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        url = getArguments().getString(Constants.IMAGE_URL);

        title = getArguments().getString(Constants.TITLE);

        TextView tt = view.findViewById(R.id.title);
        tt.setText(title);

        progressView = view.findViewById(R.id.progress);

        if (null == mimageLoader)
        {
            mimageLoader = VolleyController.getInstance(getActivity()).getImageLoader();
        }

        visitorPic = view.findViewById(R.id.visitor_pic);
        //VisitorModel visObj = dataSet.get(position);
        String URL = url;
        visitorPic.setImageUrl(URL, mimageLoader);



        closeButton = view.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(closePopupListener);

        return view;
    }

    private View.OnClickListener closePopupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
}
