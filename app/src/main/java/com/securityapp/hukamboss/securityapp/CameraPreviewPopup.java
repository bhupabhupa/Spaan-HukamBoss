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
import android.widget.ProgressBar;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.model.VisitorHomeFragmentModel;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Bhupa on 08/03/18.
 */

public class CameraPreviewPopup extends DialogFragment {
    private String picPath;
    ImageView closeButton;
    //ImageView cameraPic;
    NetworkImageView cameraPic;
    Button retake;
    ImageLoader mimageLoader = null;
    ImageView cameraClickPreview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.camera_preview_popup, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        picPath = getArguments().getString(Constants.VISITOR_PHOTO_PATH);

        cameraPic = view.findViewById(R.id.camera_preview);
        cameraClickPreview = view.findViewById(R.id.camera_click_preview);

        File imgFile = new  File(picPath);
        //Uri fileUri = Uri.fromFile(imgFile);
        if(imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            cameraClickPreview.setImageBitmap(myBitmap);
            cameraPic.setVisibility(View.GONE);
        } else {
            String URL = picPath;
            mimageLoader = VolleyController.getInstance(getActivity().getApplicationContext()).getImageLoader();
            cameraPic.setImageUrl(URL, mimageLoader);
            cameraClickPreview.setVisibility(View.GONE);
        }



        closeButton = view.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(closePopupListener);

        retake = view.findViewById(R.id.retake);
        retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    dismiss();
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            }
        });
        return view;
    }

    private View.OnClickListener closePopupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
}


