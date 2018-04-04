package com.securityapp.hukamboss.securityapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.securityapp.hukamboss.securityapp.constants.Constants;

import java.io.File;

/**
 * Created by Bhupa on 03/04/18.
 */

public class DialerPopup extends DialogFragment {
    ImageView closeButton;
    TextView dialerPhone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialer_popup, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        final String mobileNo = getArguments().getString(Constants.MOBILE_NO);

        dialerPhone = view.findViewById(R.id.dialer_phone);

        dialerPhone.setText(mobileNo);
        dialerPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mobileNo, null));
                startActivity(phoneIntent);
            }
        });

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



