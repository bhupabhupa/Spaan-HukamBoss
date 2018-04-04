package com.securityapp.hukamboss.securityapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.model.VisitorHomeFragmentModel;
import com.securityapp.hukamboss.securityapp.utils.DataPart;
import com.securityapp.hukamboss.securityapp.utils.Utility;
import com.securityapp.hukamboss.securityapp.utils.VolleyMultipartRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class MaterialEntryFragment extends Fragment {
    String accessToken;
    String societyId;
    String societyGateNo;
    ImageButton photo1, photo2;
    //String picName1, picName2;
    String picturePath1, picturePath2;
    boolean isMaterialDetailCaptured = false;
    boolean isMaterialStorageCaptured = false;
    Button submitButton, backButton;

    EditText nameOfStaff, material_details, material_storage;

    protected static final int CAPTURE_IMAGE_MATERIAL_DETAILS = 0;
    protected static final int CAPTURE_IMAGE_MATERIAL_STORAGE = 1;
    VisitorHomeFragmentModel vhfModel;
    ProgressBar progressView;
    ScrollView parentScroll;
    Bitmap visitorPic;
    Map<String, String> params = new HashMap<String, String>();

    int uploadCount = 0;
    boolean fromVisitor;
    LinearLayout panel4;

    TextView nameOfStaffError, materialError,materialDetailError,materialStorageError;
    String nameOfStaffErrorTxt = "Name of Staff cannot be blank";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_material_entry, container, false);

        progressView = view.findViewById(R.id.progress);
        parentScroll = view.findViewById(R.id.parent_scroll);

        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        societyGateNo = getArguments().getString(Constants.SOCIETY_GATE_NO);

        fromVisitor = getArguments().getBoolean(Constants.FROM_VISITOR);


        if(fromVisitor) {
            vhfModel = (VisitorHomeFragmentModel) getArguments().getSerializable(Constants.VHFMODEL);
        }


        nameOfStaff = view.findViewById(R.id.name_of_staff);
        nameOfStaff.setOnFocusChangeListener(focusChangeListener);
        material_details = view.findViewById(R.id.material_details);
        material_storage = view.findViewById(R.id.material_storage);

        nameOfStaffError = view.findViewById(R.id.name_of_staff_error);
        materialError = view.findViewById(R.id.material_error);

        nameOfStaffError.setVisibility(View.GONE);
        materialError.setVisibility(View.GONE);


        photo1 = view.findViewById(R.id.photo1);
        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //picName1 = "material_1_";
                try {
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_MATERIAL_DETAILS);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            }
        });

        photo2 = view.findViewById(R.id.photo2);
        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //picName2 = "material_2_";
                try {
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_MATERIAL_STORAGE);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            }
        });

        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(backButtonListener);

        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(submitButtonListener);

        if(fromVisitor) {
            setMaterialEntryFragmentValues(vhfModel);
        } else {
            backButton.setVisibility(View.GONE);
            panel4 = view.findViewById(R.id.panel4);
            panel4.setWeightSum(2);
            submitButton.setOnClickListener(submitMaterialListener);
        }

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_MATERIAL_DETAILS && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // convert byte array to Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);
                photo1.setImageBitmap(bitmap);
                photo1.setVisibility(View.VISIBLE);
                picturePath1 = Utility.saveToInternalStorage(getActivity(), bitmap);
                isMaterialDetailCaptured = true;
            } catch (Exception e){
                Log.e("SAVE_IMAGE",e.getMessage());
            }

        } else if (requestCode == CAPTURE_IMAGE_MATERIAL_STORAGE && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // convert byte array to Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);
                photo2.setImageBitmap(bitmap);
                photo2.setVisibility(View.VISIBLE);

                picturePath2 = Utility.saveToInternalStorage(getActivity(), bitmap);
                isMaterialStorageCaptured = true;
            } catch (Exception e){
                Log.e("SAVE_IMAGE",e.getMessage());
            }

        }
    }

    private void saveVisitorData() {
        //Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SOCIETY_ID, ""+societyId);
        params.put(Constants.GATE_NO, vhfModel.getGateNo());
        params.put(Constants.VISITOR_MOBILE_NO, vhfModel.getVisitorMobile());
        params.put(Constants.VISITOR_PREFIX, "Mr.");
        params.put(Constants.IS_OTP_VERIFIED, ""+vhfModel.isOTPVerified());
        params.put(Constants.VISITOR_NAME, vhfModel.getVisitorName());
        params.put(Constants.NO_OF_VISTORS, vhfModel.getNoOfVisitors());
        params.put(Constants.VISITOR_ADDRESS, vhfModel.getVisitorAddress());
        params.put(Constants.VISITOR_TYPE, vhfModel.getVisitorType());
        params.put(Constants.ORGANIZATION_NAME, vhfModel.getOrgName());
        params.put(Constants.SOCIETY_PURPOSE, ""+vhfModel.isSocietyPurpose());
        params.put(Constants.VISITOR_PHOTO_PATH, vhfModel.getPicPath());
        if (Constants.VISITOR_PHOTO_PATH != null) {
            visitorPic = Utility.getImageFromPath(vhfModel.getPicPath());
        }

        //Second Fragment
        params.put(Constants.VISITOR_VEHICLE_NO, vhfModel.getVehicleNo());
        params.put(Constants.VISITOR_VEHICLE_TYPE, vhfModel.getVehicleType());
        params.put(Constants.NO_OF_VEHICLES, vhfModel.getVehicleCount());
        params.put(Constants.NARRATION, vhfModel.getNarration());
        params.put(Constants.VISITOR_GOVT_ID, vhfModel.getProofOfID());
        params.put(Constants.VISITOR_ID_NO,vhfModel.getIdNo());
        params.put(Constants.SOCIETY_OFFICE_BEARER,vhfModel.getSocietyBearerStr());
        //params.put(Constants.SOCIETY_OFFICE_BEARER,vhfModel.getStaffName());
        params.put(Constants.NOTIFICATION_TYPE, "Visitor Alert");

        params.put(Constants.BYPASS_NAME,vhfModel.getBypassName() == null ? "" : vhfModel.getBypassName());
        params.put(Constants.BYPASS_REMARK,vhfModel.getBypassRemark() == null ? "" : vhfModel.getBypassRemark());
        //params.put(Constants.IS_VISITOR_REJECTED, ""+vhfModel.isRejected());



        saveToServer(params);
    }

    /*
    private void addVisitorsToServer(Map<String, String> myParams) {
        Map<String, String> params = myParams;

        //if(vhfModel.getSelectedFlatNo().size() > 0) {
          //saveToServer(params, uploadCount);
        //}
        saveToServer(params, uploadCount);

    }*/

    private void saveMaterialData(final String visitorId, final String visitId) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);

        final String URL = Constants.BASE_URL+"SocietyInwordMaterials";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("RESPONSE SUCCESS", s.toString());
                        popup.dismiss();
                        if(isMaterialDetailCaptured) {
                            saveChalanImageToServer(visitId);
                        } else if(isMaterialStorageCaptured) {
                            saveMaterialImageToServer(visitId);
                        } else {

                            Toast.makeText(getActivity().getApplicationContext(), "Visitor added successfully!!", 5000).show();
                            final Bundle bundle = new Bundle();
                            bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                            bundle.putString(Constants.SOCIETY_ID, societyId);
                            bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();

                            VisitorHomeFragment homeFragment = new VisitorHomeFragment();
                            homeFragment.setArguments(bundle);
                            ft.replace(R.id.rootLayout,homeFragment);
                            ft.commit();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView, parentScroll);
                VolleyLog.d("responseError", "Error: " + volleyError);
                //showProgress(false);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                //params.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> myParams = params;
                //myParams.put(Constants.SOCIETY_ID, societyId);
                myParams.put(Constants.VISIT_ID, visitId);
                myParams.put(Constants.NAME_OF_STAFF, vhfModel.getStaffName());
                myParams.put(Constants.MATERIAL_DETAILS, vhfModel.getMaterialDetails());
                myParams.put(Constants.MATERIAL_STORAGE_PLACE, vhfModel.getMaterialStoragePlace());
                //myParams.put(Constants.INWARD_CHALAN_IMAGE, "default.jpeg");
                //myParams.put(Constants.INWARD_MATERIAL_IMAGE, "default.jpeg");
                return myParams;
            }

        };

        //VolleyController commInstanceObj = VolleyController.getInstance(this);
        //commInstanceObj.getRequestQueue().add(jsonObjReq);
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView, parentScroll);
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);


    }

    private void saveChalanImageToServer(final String visitId) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        final String URL = Constants.BASE_URL+"SocietyInwordMaterials/ChalanImage";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        popup.dismiss();
                        if(isMaterialStorageCaptured) {
                            saveMaterialImageToServer(visitId);
                        } else {

                            Toast.makeText(getActivity().getApplicationContext(), "Visitor added successfully!!", 5000).show();
                            final Bundle bundle = new Bundle();
                            bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                            bundle.putString(Constants.SOCIETY_ID, societyId);
                            bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();

                            VisitorHomeFragment homeFragment = new VisitorHomeFragment();
                            homeFragment.setArguments(bundle);
                            ft.replace(R.id.rootLayout,homeFragment);
                            ft.commit();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        popup.dismiss();
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("RESPONSE SUCCESS ERROR", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.VISIT_ID, visitId);
                return params;
            }

            /*
            * Here we are passing image by renaming it with a unique name
            * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                //Log.d("SUCCESS", visitorPic.toString());
                visitorPic = Utility.getImageFromPath(vhfModel.getMaterialDetailsPicPath());
                Log.d("FILE visitorPic", visitorPic.toString());
                params.put("ClientImage", new DataPart(imagename + ".png", Utility.getFileDataFromDrawable(visitorPic)));
                return params;
            }
        };

        //adding the request to volley
        popup.show(fm, "Progress");
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);

    }

    private void saveMaterialImageToServer(final String visitId) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        final String URL = Constants.BASE_URL+"SocietyInwordMaterials//MaterialImage";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        popup.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Visitor added successfully!!", 5000).show();
                        final Bundle bundle = new Bundle();
                        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                        bundle.putString(Constants.SOCIETY_ID, societyId);
                        bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

                        VisitorHomeFragment homeFragment = new VisitorHomeFragment();
                        homeFragment.setArguments(bundle);
                        ft.replace(R.id.rootLayout,homeFragment);
                        ft.commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        popup.dismiss();
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("RESPONSE SUCCESS ERROR", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.VISIT_ID, visitId);
                return params;
            }

            /*
            * Here we are passing image by renaming it with a unique name
            * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                //Log.d("SUCCESS", visitorPic.toString());
                visitorPic = Utility.getImageFromPath(vhfModel.getMaterialStoragePicPath());
                Log.d("FILE visitorPic", visitorPic.toString());
                params.put("ClientImage", new DataPart(imagename + ".png", Utility.getFileDataFromDrawable(visitorPic)));
                return params;
            }
        };

        //adding the request to volley
        popup.show(fm, "Progress");
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);

    }

    public View.OnClickListener submitButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isValidData = true;
            if (nameOfStaff.getEditableText().toString().trim().length() == 0) {
                //name_of_staff.setError("This field cannot be blank");
                nameOfStaff.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                nameOfStaffError.setText(nameOfStaffErrorTxt);
                nameOfStaffError.setVisibility(View.VISIBLE);
                isValidData = false;
            }
            if(material_details.getEditableText().toString().length() == 0 &&
                    material_storage.getEditableText().toString().length() == 0) {
                materialError.setText("Add Material Details OR Material Storage Place");
                materialError.setVisibility(View.VISIBLE);
                isValidData = false;
            }
            if(material_details.getEditableText().toString().length() != 0 && !isMaterialDetailCaptured) {
                //materialDetailError.setText("Please capture Material Details Photo");
                //materialDetailError.setVisibility(View.VISIBLE);
                photo1.setImageResource(R.drawable.warning);
                isValidData = false;
            }
            if(material_storage.getEditableText().toString().length() != 0 && !isMaterialStorageCaptured) {
                //materialStorageError.setText("Please capture Material Storage Photo");
                //materialStorageError.setVisibility(View.VISIBLE);
                photo2.setImageResource(R.drawable.warning);
                isValidData = false;
            }
            if (isValidData) {
                //saveMaterialData();
                vhfModel.setStaffName(nameOfStaff.getText().toString());
                vhfModel.setMaterialDetails(material_details.getText().toString());
                vhfModel.setMaterialStoragePlace(material_storage.getText().toString());
                vhfModel.setMaterialDetailsPicPath((picturePath1 != null && picturePath1.length() > 0) ? picturePath1 : (vhfModel.getMaterialDetailsPicPath() == null)? "": vhfModel.getMaterialDetailsPicPath());
                vhfModel.setMaterialStoragePicPath((picturePath2 != null && picturePath2.length() > 0) ? picturePath2 : (vhfModel.getMaterialStoragePicPath() == null)? "": vhfModel.getMaterialStoragePicPath());
                saveVisitorData();
            }
        }
    };

    public View.OnClickListener backButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VisitorNextFragment homeFragment = new VisitorNextFragment();
            final Bundle bundle = new Bundle();
            bundle.putString(Constants.ACCESS_TOKEN, accessToken);
            bundle.putString(Constants.SOCIETY_ID, societyId);
            bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

            vhfModel.setStaffName(nameOfStaff.getEditableText().toString());
            vhfModel.setMaterialDetails(material_details.getText().toString());
            vhfModel.setMaterialStoragePlace(material_storage.getText().toString());
            vhfModel.setMaterialDetailsPicPath((picturePath1 != null && picturePath1.length() > 0) ? picturePath1 : (vhfModel.getMaterialDetailsPicPath() == null)? "": vhfModel.getMaterialDetailsPicPath());
            vhfModel.setMaterialStoragePicPath((picturePath2 != null && picturePath2.length() > 0) ? picturePath2 : (vhfModel.getMaterialStoragePicPath() == null)? "": vhfModel.getMaterialStoragePicPath());

            bundle.putSerializable(Constants.VHFMODEL, vhfModel);

            homeFragment.setArguments(bundle);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.rootLayout,homeFragment);
            ft.commit();
        }
    };

    private void setMaterialEntryFragmentValues(VisitorHomeFragmentModel vhfModel) {

        nameOfStaff.setText(vhfModel.getStaffName());
        material_details.setText(vhfModel.getMaterialDetails());
        material_storage.setText(vhfModel.getMaterialStoragePlace());

        if(vhfModel.getMaterialDetailsPicPath() != null) {
            File imgFile1 = new  File(vhfModel.getMaterialDetailsPicPath());
            if(imgFile1.exists()) {
                Bitmap myBitmap1 = BitmapFactory.decodeFile(imgFile1.getAbsolutePath());
                photo1.setImageBitmap(myBitmap1);
            }
        }


        if(vhfModel.getMaterialStoragePicPath() != null) {
            File imgFile2 = new  File(vhfModel.getMaterialStoragePicPath());
            if(imgFile2.exists()) {
                Bitmap myBitmap2 = BitmapFactory.decodeFile(imgFile2.getAbsolutePath());
                photo2.setImageBitmap(myBitmap2);
            }
        }
    }


    private void saveToServer(final Map<String, String> myParams) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);

        StringRequest request = new StringRequest(Request.Method.POST,Constants.BASE_URL+"Visitors",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            popup.dismiss();
                            JSONObject object = new JSONObject(s);
                            Log.d("RESPONSE SUCCESS", object.toString());
                            String visitorId = object.getString("Id");
                            String visitId = object.getString("VisitId");
                            //saveVisitorPic(visitorId, uploadingIndex);
                            saveVisitorPic(visitorId,visitId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView, parentScroll);
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
                /*String flatNumber = vhfModel.getSelectedFlatNo().get(0);*/
                myParams.put(Constants.FLAT_NUMBER, "");
                myParams.put(Constants.FLAT_NUMBER, "");
                myParams.put(Constants.MEMBER_ID, "");
                myParams.put(Constants.RESIDENT_NAME, "");
                return myParams;
            }
        };

        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView, parentScroll);
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }


    private void saveVisitorPic(final String visitorId, final String visitId) {
        //final String URL = Constants.BASE_URL+"api/DocumentUpload/MediaUpload";
        final String URL = Constants.BASE_URL+"Visitors/VisitorImage";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {

                            saveMaterialData(visitorId, visitId);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("RESPONSE SUCCESS ERROR", error.toString());
                    }
                }) {

            /*
            * If you want to add more parameters with the image
            * you can do it here
            * here we have only one parameter with the image
            * which is tags
            * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("VisitorId", visitorId);
                params.put(Constants.VISIT_ID, visitId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                //params.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            /*
            * Here we are passing image by renaming it with a unique name
            * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                //Log.d("SUCCESS", visitorPic.toString());
                Log.d("FILE visitorPic", visitorPic.toString());
                params.put("ClientImage", new DataPart(imagename + ".png", Utility.getFileDataFromDrawable(visitorPic)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }

    public View.OnClickListener submitMaterialListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) {
                nameOfStaff.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                nameOfStaffError.setVisibility(View.GONE);
            }
            if(!hasFocus && nameOfStaff.getText().toString().length() == 0){
                nameOfStaff.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                nameOfStaffError.setText(nameOfStaffErrorTxt);
                nameOfStaffError.setVisibility(View.VISIBLE);
            }

        }
    };
}

