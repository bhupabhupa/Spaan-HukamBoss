package com.securityapp.hukamboss.securityapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.utils.DataPart;
import com.securityapp.hukamboss.securityapp.utils.Utility;
import com.securityapp.hukamboss.securityapp.utils.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class StaffEntryExitFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private String accessToken;
    private String societyId;
    private String societyGateNo;

    NetworkImageView photo;
    ImageView photoCapture;
    Spinner gate_no;

    EditText mobileNo, staffID, staffName, designation, staffRemark;
    TextView mobileNoError, staffIDError, staffNameError, staffDesError, staffRemarkError;

    private static int SCAN_REQUEST_CODE = 0;
    private static int STAFF_ATTENDANCE_REQUEST_CODE = 1;
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;
    private static final int CAMERA_PREVIEW_POPUP = 3;
    String[] gateItems = new String[]{"Gate No", "1", "2", "3"};
    private String mobileErrorTxt = "Mobile No must be 10 digits";
    private String mobileBlankErrorTxt = "Mobile No field cannot be blank";
    private String staffRemarkErrorTxt = "Please add a remark";


    ImageLoader mimageLoader = null;

    //boolean staffSearched = false;
    String photoPath = "";
    boolean staffPhotoCaptured, staffDataReceived;
    Button inBtn, outBtn;
    Bitmap staffPic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_staff_entry_exit, container, false);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        societyGateNo = getArguments().getString(Constants.SOCIETY_GATE_NO);

        gate_no = view.findViewById(R.id.vh_gate_no);
        populateGateNos(gate_no);
        if (societyGateNo != null) {
            for (int i = 0; i < gateItems.length; i++) {
                if (gateItems[i].equals(societyGateNo)) {
                    gate_no.setSelection(i);
                }
            }
        }

        mobileNo = view.findViewById(R.id.staff_mobile);
        mobileNo.requestFocus();
        staffID = view.findViewById(R.id.staff_id);
        staffName = view.findViewById(R.id.staff_name);
        designation = view.findViewById(R.id.staff_designation);
        staffRemark = view.findViewById(R.id.staff_remark);
        staffRemark.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});

        mobileNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        mobileNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        mobileNo.setOnFocusChangeListener(focusChangeListener);

        staffID.setEnabled(false);
        staffName.setEnabled(false);
        designation.setEnabled(false);

        mobileNoError = view.findViewById(R.id.staff_mobile_error);
        staffIDError = view.findViewById(R.id.staff_id_error);
        staffNameError = view.findViewById(R.id.staff_name_error);
        staffDesError = view.findViewById(R.id.staff_designation_error);
        staffRemarkError = view.findViewById(R.id.staff_remark_error);

        mobileNoError.setVisibility(View.GONE);
        staffIDError.setVisibility(View.GONE);
        staffNameError.setVisibility(View.GONE);
        staffDesError.setVisibility(View.GONE);
        staffRemarkError.setVisibility(View.GONE);

        staffRemark.setOnFocusChangeListener(focusChangeListener);

        mimageLoader = VolleyController.getInstance(getActivity().getApplicationContext()).getImageLoader();

        showPopup();

        photo = view.findViewById(R.id.photo);
        photo.setVisibility(View.GONE);
        photoCapture = view.findViewById(R.id.photo_capture);

        photo.setOnClickListener(photoClickListener);
        photoCapture.setOnClickListener(photoClickListener);

        inBtn = view.findViewById(R.id.in_button);
        outBtn = view.findViewById(R.id.out_button);

        inBtn.setOnClickListener(inClickListener);
        outBtn.setOnClickListener(outClickListener);

        return view;
    }

    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();
        Log.d("QR2", scanResult);
        String[] resultList = scanResult.split("\n");
        String name = "";
        String telephone = "";
        String id = "";
        String title = "";

        for (int i = 0; i < resultList.length; i++) {
            String[] resultVal = resultList[i].split(":");
            if (resultVal[0].equalsIgnoreCase("BDAY")) {
                id = resultVal[1];
            }

            if (resultVal[0].equalsIgnoreCase("FN")) {
                name = resultVal[1];
            }

            if (resultVal[0].equalsIgnoreCase("TITLE")) {
                title = resultVal[1];
            }

            if (resultVal[0].equalsIgnoreCase("TEL;CELL")) {
                telephone = resultVal[1];
            }
        }


        mScannerView.stopCamera();
        FragmentManager fragment = getFragmentManager();
        StaffScanPopup popup = new StaffScanPopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
        //bundle.putString(Constants.SCAN_DATA, scanResult);
        bundle.putString(Constants.NAME, name);
        bundle.putString(Constants.MOBILE_NO, telephone);
        bundle.putString(Constants.DESIGNATION, title);
        bundle.putString(Constants.ID, id);
        popup.setTargetFragment(this, SCAN_REQUEST_CODE);

        popup.setArguments(bundle);
        popup.setCancelable(false);
        popup.show(fragment, "PastIncidences");

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCAN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mScannerView.removeAllViews();
            mScannerView.stopCamera();
            mScannerView.resumeCameraPreview(StaffEntryExitFragment.this);
            Intent intent = new Intent(getActivity(), VisitorHomeActivity.class);
            intent.putExtra(Constants.ACCESS_TOKEN, accessToken);
            intent.putExtra(Constants.SOCIETY_ID, societyId);
            intent.putExtra(Constants.SOCIETY_GATE_NO, societyGateNo);
            intent.putExtra(Constants.MENU_ID, 0);
            startActivity(intent);
        } else if (requestCode == SCAN_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
            mScannerView.removeAllViews();
            mScannerView.stopCamera();
            mScannerView.resumeCameraPreview(StaffEntryExitFragment.this);
            Intent intent = new Intent(getActivity(), VisitorHomeActivity.class);
            intent.putExtra(Constants.ACCESS_TOKEN, accessToken);
            intent.putExtra(Constants.SOCIETY_ID, societyId);
            intent.putExtra(Constants.SOCIETY_GATE_NO, societyGateNo);
            intent.putExtra(Constants.MENU_ID, 1);
            startActivity(intent);
        } else if (requestCode == STAFF_ATTENDANCE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mScannerView = new ZXingScannerView(getActivity());
            getActivity().setContentView(mScannerView);
            mScannerView.setResultHandler(StaffEntryExitFragment.this);
            mScannerView.startCamera();
        } else if (requestCode == CAMERA_PREVIEW_POPUP && resultCode == Activity.RESULT_OK) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                staffPhotoCaptured = true;
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);
                photo.setVisibility(View.GONE);
                photoCapture.setImageBitmap(bitmap);
                photoCapture.setVisibility(View.VISIBLE);
                String picturePath = saveToInternalStorage(bitmap);
                photoPath = picturePath;
            } catch (Exception e) {
                Log.e("SAVE_IMAGE", e.getMessage());
            }

        }
    }


    private void showPopup() {
        FragmentManager fragment = getFragmentManager();
        StaffAttendancePopup popup = new StaffAttendancePopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
        popup.setTargetFragment(this, STAFF_ATTENDANCE_REQUEST_CODE);

        popup.setArguments(bundle);
        popup.setCancelable(false);
        popup.show(fragment, "PastIncidences");
    }

    public void populateGateNos(Spinner dropdown) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_dropdown_item, gateItems);
        dropdown.setAdapter(adapter);
    }

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.staff_mobile:
                    if (hasFocus) {
                        mobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        mobileNoError.setVisibility(View.GONE);
                    }
                    if (!hasFocus && (mobileNo.getText().toString().length() == 0)) {
                        mobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        mobileNoError.setText(mobileBlankErrorTxt);
                        mobileNoError.setVisibility(View.VISIBLE);
                    } else if (!hasFocus && (mobileNo.getText().toString().length() != 10)) {
                        mobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        mobileNoError.setText(mobileErrorTxt);
                        mobileNoError.setVisibility(View.VISIBLE);
                    }
                    if ((!hasFocus) && mobileNo.getText().toString().length() == 10) {
                        getStaffDetails(mobileNo.getText().toString());
                    }
                    break;
                case R.id.staff_remark:
                    if (hasFocus) {
                        staffRemark.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        staffRemarkError.setVisibility(View.GONE);
                    }
                    if (!hasFocus && (staffRemark.getText().toString().length() == 0)) {
                        staffRemark.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        staffRemarkError.setText(staffRemarkErrorTxt);
                        staffRemarkError.setVisibility(View.VISIBLE);
                    }
                    break;
            }

        }
    };

    private void getStaffDetails(final String mobileNo) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_URL + "DomesticStaffDetails/?Mobile=" + mobileNo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        popup.dismiss();
                        ;
                        try {
                            Log.d("SUCCESS OTP", s);
                            try {
                                JSONObject object = new JSONObject(s);
                                JSONArray resourceList = object.getJSONArray("ResourceList");

                                if (resourceList.length() > 0) {
                                    //staffSearched = true;
                                    staffPhotoCaptured = false;
                                    staffDataReceived = true;
                                    JSONObject resource = resourceList.getJSONObject(resourceList.length() - 1);
                                    photoPath = Constants.BASE_URL + "Staffimages/" + resource.getString(Constants.ID) + ".jpeg";

                                    photo.setImageUrl(photoPath, mimageLoader);

                                    staffID.setText(resource.getString(Constants.ID));
                                    staffName.setText(resource.getString(Constants.STAFF_NAME));
                                    designation.setText(resource.getString(Constants.DESIGNATION));


                                    photo.setVisibility(View.VISIBLE);
                                    photoCapture.setVisibility(View.GONE);
                                } else {
                                    staffDataReceived = false;
                                    photoCapture.setImageResource(R.drawable.ic_menu_camera);
                                    photoCapture.setVisibility(View.VISIBLE);
                                    photo.setVisibility(View.GONE);

                                    staffName.setText("");
                                    staffID.setText("");
                                    designation.setText("");
                                }
                            } catch (JSONException e) {

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                VolleyLog.d("responseError", "Error: " + volleyError);
                Toast.makeText(getActivity().getApplicationContext(), "Wrong OTP", 5000).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };
        popup.show(fm, "Progress");
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private View.OnClickListener photoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (staffDataReceived) {
                FragmentManager fm = getFragmentManager();
                CameraPreviewPopup popup = new CameraPreviewPopup();
                final Bundle bundle = new Bundle();
                bundle.putString(Constants.VISITOR_PHOTO_PATH, photoPath);
                popup.setTargetFragment(StaffEntryExitFragment.this, CAMERA_PREVIEW_POPUP);

                popup.setArguments(bundle);
                popup.setCancelable(false);
                popup.show(fm, "Camera Preview");

            } /* else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            }*/
        }
    };


    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getActivity().getApplication().getBaseContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        String mImageName = "Staff_" + timeStamp + ".jpg";
        File mypath = new File(directory, mImageName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 1, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    private View.OnClickListener inClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isValidData = true;

            if (mobileNo.getEditableText().toString().length() == 0) {
                isValidData = false;
                mobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                mobileNoError.setText(mobileBlankErrorTxt);
                mobileNoError.setVisibility(View.VISIBLE);
                mobileNo.clearFocus();

            } else if (mobileNo.getEditableText().toString().length() != 10) {
                isValidData = false;
                mobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                mobileNoError.setText(mobileErrorTxt);
                mobileNoError.setVisibility(View.VISIBLE);
                mobileNo.clearFocus();
            }

            if (staffRemark.getEditableText().toString().length() == 0) {
                isValidData = false;
                staffRemark.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                staffRemarkError.setText(staffRemarkErrorTxt);
                staffRemarkError.setVisibility(View.VISIBLE);
            }

            if (!staffPhotoCaptured) {
                isValidData = false;
                photo.setImageResource(R.drawable.warning);
                photoCapture.setImageResource(R.drawable.warning);
            } else {
                staffPic = Utility.getImageFromPath(photoPath);
            }


            if (staffDataReceived && isValidData) {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.SOCIETY_ID, societyId);
                params.put(Constants.SOCIETY_GATE_NO, societyGateNo);
                params.put(Constants.STAFF_ID, staffID.getEditableText().toString());
                params.put(Constants.STAFF_REMARK, staffRemark.getEditableText().toString());
                params.put(Constants.STAFF_IN, "" + true);
                params.put(Constants.STAFF_FORM, "" + true);
                sendInData(params);
            }
        }
    };

    private View.OnClickListener outClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Map<String, String> params = new HashMap<>();
            params.put(Constants.SOCIETY_GATE_NO, societyId);
            params.put(Constants.STAFF_ID, staffID.getEditableText().toString());
            params.put(Constants.STAFF_REMARK, staffRemark.getEditableText().toString());
            params.put(Constants.STAFF_OUT, "" + true);
            params.put(Constants.STAFF_FORM, "" + true);
            sendOutData(params);
        }
    };

    private void sendInData(final Map<String, String> myParams) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_URL + "DomesticStaffSubscription",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String message = object.getString(Constants.MESSAGE_INFO);
                            Toast.makeText(getActivity().getApplicationContext(), message, 5000).show();

                            popup.dismiss();

                            String staffEntryID = object.getString(Constants.ID);

                            sendStaffPhoto(staffEntryID);

                            /*final Bundle bundle = new Bundle();
                            bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                            bundle.putString(Constants.SOCIETY_ID, societyId);
                            bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();

                            VisitorHomeFragment homeFragment = new VisitorHomeFragment();
                            homeFragment.setArguments(bundle);
                            ft.replace(R.id.rootLayout,homeFragment);
                            ft.commit();*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                //Toast.makeText(getApplicationContext(), myParams.toString(), 5000).show();
                Log.d("RESPONSE ERROR", volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return myParams;
            }
        };

        popup.show(fm, "Progress");
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void sendOutData(final Map<String, String> myParams) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.PUT, Constants.BASE_URL + "DomesticStaffSubscription" + "/" + staffID.getEditableText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String message = object.getString(Constants.MESSAGE_INFO);
                            Toast.makeText(getActivity().getApplicationContext(), message, 5000).show();
                            popup.dismiss();

                            final Bundle bundle = new Bundle();
                            bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                            bundle.putString(Constants.SOCIETY_ID, societyId);
                            bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();

                            VisitorHomeFragment homeFragment = new VisitorHomeFragment();
                            homeFragment.setArguments(bundle);
                            ft.replace(R.id.rootLayout, homeFragment);
                            ft.commit();

                        } catch (JSONException e) {
                        }
                        //Log.d("RESPONSE SUCCESS - ", "" + s);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Toast.makeText(getApplicationContext(), myParams.toString(), 5000).show();
                Log.d("RESPONSE ERROR", volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
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

    private void sendStaffPhoto(final String staffEntryID) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);

        //http://103.48.51.62:8018/DomesticStaff/UploadStaffImage
        final String URL = Constants.BASE_URL + "DomesticStaff/UploadStaffImage";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        popup.dismiss();
                        final Bundle bundle = new Bundle();
                        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                        bundle.putString(Constants.SOCIETY_ID, societyId);
                        bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

                        VisitorHomeFragment homeFragment = new VisitorHomeFragment();
                        homeFragment.setArguments(bundle);
                        ft.replace(R.id.rootLayout, homeFragment);
                        ft.commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        popup.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Error while saving staff pic", Toast.LENGTH_SHORT).show();
                        Log.d("RESPONSE SUCCESS ERROR", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("SsSubscriptionID", staffEntryID);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                //Log.d("SUCCESS", visitorPic.toString());
                Log.d("FILE visitorPic", staffPic.toString());
                params.put("ClientImage", new DataPart(imagename + ".png", Utility.getFileDataFromDrawable(staffPic)));
                return params;
            }
        };

        //adding the request to volley
        popup.show(fm, "Progress");
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);


    }

}
