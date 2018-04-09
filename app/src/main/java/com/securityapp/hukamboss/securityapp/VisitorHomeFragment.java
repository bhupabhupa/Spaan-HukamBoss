package com.securityapp.hukamboss.securityapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.model.VisitorHomeFragmentModel;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitorHomeFragment extends Fragment {

    //String gateNo, visitorMobile, visitorName, noOfVisitors, visitorAddress, visitorType, orgName, picPath;
    String flatNo, residentName, vehicleNo, vehicleType, vehicleCount, narration, proofOfID, idNo;
    String staffName, materialDetails, materialStoragePlace, materialDetailsPath, materialStoragePath;
    String[] gateItems = new String[]{"Gate No", "1", "2", "3"};
    JSONArray typeList;

    Button nextButton, search_incedence;

    EditText mobile, address, orgName, edit1_10, visitor_name;
    //EditText edit1_8, edit1_4;
    //Spinner purpose;
    Spinner type;
    Spinner gate_no;
    EditText vh_visitor_count;
    //CheckBox mobile_checkbox;
    //ImageView resultPic;
    //ImageButton photo;
    NetworkImageView photo;
    String picturePath;
    TextInputLayout orgNameInput;

    JSONObject visitorJSON = new JSONObject();
    JSONObject photoJSON = new JSONObject();
    JSONObject residenceJSON = new JSONObject();
    JSONObject idProofJSON = new JSONObject();

    String picName;
    Uri imageUri;
    File tempPic;

    String accessToken;
    String societyId;
    String societyGateNo;
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    private static final int GENERATE_OTP_REQUEST_CODE = 5;
    private static final int BYPASS_REQUEST_CODE = 6;
    private static final int GENERATE_BYPASS_REQUEST_CODE = 7;
    private static final int CAMERA_PREVIEW_POPUP = 3;

    CheckBox society_office;
    boolean forSocietyVisit = false;

    ProgressBar progressView;

    boolean fromBackFragment = false;
    boolean fromCameraCapture = false;

    //boolean isPhotoCaptured;

    VisitorHomeFragmentModel vhfModel;
    ScrollView parentScroll;

    String mandatory = "<font color=#cc0029> *</font>";
    //boolean isBypassFilled;
    TextView mobileError, nameError, addressError, visitorTypeError, orgNameError;

    private String mobileErrorTxt = "Mobile number should be 10 digits";
    private String mobileBlankErrorTxt = "Mobile No field cannot be blank";
    private String nameErrorTxt = "Name field cannot be blank";
    private String addressErrorTxt = "Address field cannot be blank";
    private String visitorTypeErrorTxt = "Please Select Visitor Type";
    private String orgNameErrorTxt = "Organization Name cannot be blank";

    ImageLoader mimageLoader = null;

    Bitmap visitorPic;
    ImageView photoClick;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visitor_home, container, false);


        accessToken = getArguments().getString(Constants.ACCESS_TOKEN);
        societyId = getArguments().getString(Constants.SOCIETY_ID);
        societyGateNo = getArguments().getString(Constants.SOCIETY_GATE_NO);

        fromBackFragment = getArguments().getBoolean(Constants.FROM_BACK_FRAGMENT);

        fromCameraCapture = getArguments().getBoolean(Constants.FROM_BACK_FRAGMENT);

        progressView = view.findViewById(R.id.progress);
        //parentScroll = view.findViewById(R.id.parent_scroll);

        mimageLoader = VolleyController.getInstance(getActivity().getApplicationContext()).getImageLoader();

        String jsonStr = "[{Group1:'Visitor Details',Fields:[{Field1:'Name',Type:'TextView'},{Field2:'Telephone',Type:'TextView'},{Field3:'Address',Type:'TextView'},{Field4:'Email',Type:'TextView'},{Field5:'Purpose',Type:'Dropdown',Values:['Purpose', 'Personal','Official','Delivery']},{Field6:'Visitor Type',Type:'Dropdown',Values:['-- Select Visitor Type --', 'Guest','Official','Salesman']},{Field7:'Person Name whom Notified',Type:'TextView'},{Field8:'Pincode',Type:'TextView'}, {Field9:'Organization Name',Type:'TextView'},{Field10:'No. Of additional visitors',Type:'TextView'},{Field11:'Vehicle No',Type:'TextView'},{Field12:'Prefix',Type:'Dropdown',Values:['Mr.','Ms.']}]},{Group2:'Visitor Pic',Fields:[{Field1:'Photo',Type:'Camera'}]},{Group3:'Residence Details',Fields:[{Field1:'Name',Type:'TextView'},{Field2:'Flat_No',Type:'TextView'}]},{Group4:'Proof of Visitor',Fields:[{Field1:'Govt Id',Type:'Dropdown',Values:['PAN Card','Adhar card']},{Field2:'No',Type:'TextView'},{Field3:'Photo',Type:'Camera'}]}]";

        try {

            JSONArray reader = new JSONArray(jsonStr);

            visitorJSON = reader.getJSONObject(0);
            photoJSON = reader.getJSONObject(1);
            residenceJSON = reader.getJSONObject(2);
            idProofJSON = reader.getJSONObject(3);

            //VISITOR DETAILS JSON
            JSONArray visitorDetails = visitorJSON.getJSONArray("Fields");
            //Log.d("visitorDetails", visitorDetails.toString());

            JSONObject visitorField1 = visitorDetails.getJSONObject(0);
            JSONObject visitorField2 = visitorDetails.getJSONObject(1);
            JSONObject visitorField3 = visitorDetails.getJSONObject(2);
            JSONObject visitorField4 = visitorDetails.getJSONObject(3);
            JSONObject visitorField5 = visitorDetails.getJSONObject(4);
            JSONObject visitorField6 = visitorDetails.getJSONObject(5);
            JSONObject visitorField7 = visitorDetails.getJSONObject(6);

            JSONObject visitorField8 = visitorDetails.getJSONObject(7);
            JSONObject visitorField9 = visitorDetails.getJSONObject(8);
            JSONObject visitorField10 = visitorDetails.getJSONObject(9);
            JSONObject visitorField11 = visitorDetails.getJSONObject(10);
            JSONObject visitorField12 = visitorDetails.getJSONObject(11);

            //addItemsOnSpinner2(visitorField12.getJSONArray("Values"), view.findViewById(R.id.prefix));
            //spinner1_3 = findViewById(R.id.prefix);
            //spinner1_3.setOnItemSelectedListener(this);

            //purpose = view.findViewById(R.id.purpose);
            gate_no = view.findViewById(R.id.vh_gate_no);
            populateGateNos(gate_no);
            if (societyGateNo != null) {
                for (int i = 0; i < gateItems.length; i++) {
                    if (gateItems[i].equals(societyGateNo)) {
                        gate_no.setSelection(i);
                    }
                }
            }



            orgNameInput = view.findViewById(R.id.orgName);
            orgNameInput.setVisibility(View.GONE);
            //JSONArray purposeList = visitorField5.getJSONArray("Values"), view.findViewById(R.id.purpose);
            vh_visitor_count = view.findViewById(R.id.vh_visitor_count);
            vh_visitor_count.setInputType(InputType.TYPE_CLASS_NUMBER);
            vh_visitor_count.setText("0");
            vh_visitor_count.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus && (vh_visitor_count.getText().toString().equalsIgnoreCase("0"))) {
                        vh_visitor_count.setText("");
                    }
                    if (!hasFocus && TextUtils.isEmpty(vh_visitor_count.getText().toString())) {
                        vh_visitor_count.setText("0");
                    }
                }
            });
            //vh_visitor_count.setFocusable(true);
            //vh_visitor_count.setFocusableInTouchMode(true);
            //populateVisitorCount(vh_visitor_count);

            type = view.findViewById(R.id.type);
            //spinner1_1.setOnItemSelectedListener();
            type.setFocusable(true);
            type.setFocusableInTouchMode(true);

            //addItemsOnSpinner2(visitorField5.getJSONArray("Values"), view.findViewById(R.id.purpose));
            addItemsOnSpinner2(visitorField6.getJSONArray("Values"), view.findViewById(R.id.type));

            typeList = visitorField6.getJSONArray("Values");

            //visit_type.setOnItemSelectedListener(this);


            visitor_name = view.findViewById(R.id.vh_visitor_name);

            final TextInputLayout text_input_mobile = view.findViewById(R.id.text_input_mobile);
            mobile = view.findViewById(R.id.vh_mobile);
            mobile.setInputType(InputType.TYPE_CLASS_NUMBER);


            //Error Text
            mobileError = view.findViewById(R.id.mobile_error);
            mobileError.setVisibility(View.GONE);
            nameError = view.findViewById(R.id.name_error);
            nameError.setVisibility(View.GONE);
            addressError = view.findViewById(R.id.address_error);
            addressError.setVisibility(View.GONE);
            visitorTypeError = view.findViewById(R.id.visitor_type_error);
            visitorTypeError.setVisibility(View.GONE);
            orgNameError = view.findViewById(R.id.org_name_error);
            orgNameError.setVisibility(View.GONE);
            mobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

            /*mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        mobileError.setVisibility(View.GONE);
                    }
                    if(!hasFocus && mobile.getText().toString().length() != 10){
                        mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        mobileError.setVisibility(View.VISIBLE);
                        //mobile.setError("Mobile no should be 10 digits");
                        //text_input_mobile.setError("Mobile required");
                        //int ecolor = R.color.colorPrimaryDark; // whatever color you want

                    }
                }
            });*/

            //mobile_checkbox = view.findViewById(R.id.vh_mobile_checkbox);

            address = view.findViewById(R.id.edit1_3);
            address.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});

            /*
            edit1_4 = view.findViewById(R.id.edit1_4);

            edit1_8 = view.findViewById(R.id.edit1_8);
            edit1_8.setInputType(InputType.TYPE_CLASS_NUMBER);
            edit1_8.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            */


            orgName = view.findViewById(R.id.org_name);
            orgName.setEnabled(false);
            orgName.setVisibility(View.GONE);

            edit1_10 = view.findViewById(R.id.vh_visitor_name);
            //edit1_10.setInputType(InputType.TYPE_CLASS_TEXT);

            type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("Spinner", type.getSelectedItem().toString());
                    if (!type.getSelectedItem().toString().equalsIgnoreCase(Constants.GUEST) && type.getSelectedItemPosition() != 0) {
                        //Log.d("Spinner inside if",spinner1_2.getSelectedItem().toString());
                        //t1_9.setVisibility(View.VISIBLE);
                        orgNameInput.setVisibility(View.VISIBLE);
                        orgName.setEnabled(true);
                        orgName.setVisibility(View.VISIBLE);
                        orgName.requestFocus();
                    } else {
                        orgNameInput.setVisibility(View.GONE);
                        orgName.setText("");
                        orgName.setEnabled(false);
                        orgName.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            photo = view.findViewById(R.id.photo);
//            resultPic = view.findViewById(R.id.resultPic);
//            resultPic.setVisibility(View.GONE);
            photo.setDefaultImageResId(R.drawable.ic_menu_camera);
            photo.setVisibility(View.GONE);

            photo.setOnClickListener(photoClickListener);

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        photoClick = view.findViewById(R.id.photo_capture);
        photoClick.setOnClickListener(photoClickListener);

        mobile.setOnFocusChangeListener(focusChangeListener);
        visitor_name.setOnFocusChangeListener(focusChangeListener);
        address.setOnFocusChangeListener(focusChangeListener);
        orgName.setOnFocusChangeListener(focusChangeListener);

        mobile.setOnKeyListener(gotoNextListener);

        address.setOnKeyListener(gotoNextListener);

        /*
        mobile.setOnKeyListener(gotoNextListener);
        visitor_name.setOnKeyListener(gotoNextListener);
        vh_visitor_count.setOnKeyListener(gotoNextListener);
        address.setOnKeyListener(gotoNextListener);
        */


        /*Button viewHistory = (Button) view.findViewById(R.id.past_incident);
        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().trim().length() == 10) {
                    //vhfModel.setCheckedPastIncidence(true);
                    //displayPastIncidences(mobile.getText().toString().trim());
                    //focusView = mobile;
                } else {
                    //mobile.setError("Mobile no must be 10 digit");
                    mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                    mobileError.setText(mobileErrorTxt);
                    mobileError.setVisibility(View.VISIBLE);
                }
            }
        });*/

        Button sendOTPBtn = (Button) view.findViewById(R.id.verify_otp);
        sendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().trim().length() == 10) {
                    sendVisitorOTP(accessToken, mobile.getText().toString().trim());
                    //displayOTPPopup();
                } else {
                    //mobile.setError("Mobile no must be 10 digit");
                    if(mobile.getText().toString().trim().length() == 0) {
                        mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        mobileError.setText(mobileBlankErrorTxt);
                        mobileError.setVisibility(View.VISIBLE);
                    } else if(mobile.getText().toString().trim().length() != 10) {
                        mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        mobileError.setText(mobileErrorTxt);
                        mobileError.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        //mobile = view.findViewById(R.id.vh_mobile);
        //edit1_3 = view.findViewById(R.id.edit1_3);
        //edit1_9 = view.findViewById(R.id.edit1_9);
        //edit1_10 = view.findViewById(R.id.vh_visitor_name);


        /*
        gate_no.setSelection(1);
        mobile.setText("9742992716");
        visitor_name.setText("Ajit");
        vh_visitor_count.setSelection(2);
        edit1_4.setText("abc@abc.com");
        edit1_8.setText("400104");
        edit1_3.setText("Santacruz");
        purpose.setSelection(1);
        type.setSelection(1);
        */


        /*
        gate_no.setSelection(1);
        //mobile.setText("9742992716");
        mobile.setText("7021584323");
        visitor_name.setText("Ajit");
        //vh_visitor_count.setSelection(2);
        edit1_3.setText("Santacruz");
        type.setSelection(1);
        */

        society_office = view.findViewById(R.id.society_office);
        society_office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked) {
                    forSocietyVisit = true;
                } else {
                    forSocietyVisit = false;
                }
            }
        });


        nextButton = view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(nextButtonListener);

        if (fromBackFragment || fromCameraCapture) {
            vhfModel = (VisitorHomeFragmentModel) getArguments().getSerializable(Constants.VHFMODEL);
            //getVisitorHomeFragmentValues(vhfModel);
            setVisitorHomeFragmentValues(vhfModel);
        } else {
            vhfModel = new VisitorHomeFragmentModel();
        }


        return view;
    }

    public void populateGateNos(Spinner dropdown) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_dropdown_item, gateItems);
        dropdown.setAdapter(adapter);
    }

    public void populateVisitorCount(Spinner dropdown) {
        String[] items = new String[]{"0", "1", "2", "3", "4", "5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    public void addItemsOnSpinner2(JSONArray purposeList, View view) {

        Spinner sprCoun = (Spinner) view;
        List<String> list = new ArrayList<String>();
        try {
            for (int i = 0; i < purposeList.length(); i++) {
                list.add(purposeList.getString(i));
            }
        } catch (Exception e) {

        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.my_spinner_dropdown_item, list);
        dataAdapter.setDropDownViewResource(R.layout.my_spinner_dropdown_item);
        sprCoun.setAdapter(dataAdapter);
    }


    public File createImageFile(String picName) throws IOException {
        // Create an image file name
        //String imageFileName = picName + String.valueOf(System.currentTimeMillis());
        Log.d("FILE create picName", picName);
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                picName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        Log.d("FILE image", image.getAbsolutePath());

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PREVIEW_POPUP && resultCode == Activity.RESULT_OK) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            vhfModel.setPreviousPhoto(false);
            vhfModel.setCameraCaptured(true);

            try {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                int width1 = bmp.getWidth();
                int height1 = bmp.getHeight();
                Log.d("ORG SIZE :- ", "" + width1);
                Log.d("ORG SIZE :- ", "" + height1);

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // convert byte array to Bitmap

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);
                bitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth()*3, bitmap.getHeight()*3, false);
                //photo.setImageBitmap(bitmap);
                //photo.setVisibility(View.VISIBLE);
                photo.setVisibility(View.GONE);
                photoClick.setImageBitmap(bitmap);
                photoClick.setVisibility(View.VISIBLE);
                vhfModel.setVisitorPicCaptured(true);

                picturePath = saveToInternalStorage(bitmap);
                vhfModel.setPicPath(picturePath);
            } catch (Exception e) {
                Log.e("SAVE_IMAGE", e.getMessage());
            }

        } else if (requestCode == GENERATE_OTP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            /*if (data.getExtras().containsKey("ENTERED_OTP")) {
                Log.d("RETVAL", data.getExtras().getString("ENTERED_OTP"));
                checkOTP(mobile.getText().toString(), data.getExtras().getString("ENTERED_OTP"));
            }*/
            if (data.getExtras().containsKey(Constants.IS_OTP_VERIFIED)) {
                vhfModel.setOTPVerified(true);
            } else {
                vhfModel.setOTPVerified(false);
            }
        } else if (requestCode == BYPASS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //vhfModel = (VisitorHomeFragmentModel) getArguments().getSerializable(Constants.VHFMODEL);
            displayBypassPopup();
        } else if (requestCode == BYPASS_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
            if (!data.getExtras().getString(Constants.PAST_INCIDENCE).equals("0")) {
                vhfModel.setHasPastIncidence(true);
                //vhfModel.setCheckedPastIncidence(true);
            }
        } else if (requestCode == BYPASS_REQUEST_CODE && resultCode == Activity.DEFAULT_KEYS_SHORTCUT) {
            if (!data.getExtras().getString(Constants.PAST_INCIDENCE).equals("0")) {
                vhfModel.setRejected(true);
                rejectVisitor();
            }
        } else if (requestCode == GENERATE_BYPASS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //vhfModel.setCheckedPastIncidence(true);
            vhfModel.setHasPastIncidence(true);
            if (data.getExtras().containsKey(Constants.BYPASS_NAME) && data.getExtras().containsKey(Constants.BYPASS_REMARK)) {
                //vhfModel = (VisitorHomeFragmentModel) getArguments().getSerializable(Constants.VHFMODEL);
                setBypassValues(data.getExtras().getString(Constants.BYPASS_NAME), data.getExtras().getString(Constants.BYPASS_REMARK));
            }
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getActivity().getApplication().getBaseContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        String mImageName = "Image_" + timeStamp + ".jpg";
        File mypath = new File(directory, mImageName);


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            /*int width1 = bitmapImage.getWidth();
            int height1 = bitmapImage.getHeight();
            Log.d("SIZE :- ", "" + width1);
            Log.d("SIZE :- ", "" + height1);*/
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            /*int width = bitmapImage.getWidth();
            int height = bitmapImage.getHeight();
            Log.d("SIZE :- ", "" + width);
            Log.d("SIZE :- ", "" + height);*/
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

    /*private String saveToExternalStorage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            return "";
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("File not found", e.getMessage());
        } catch (IOException e) {
            Log.d("IO Exception", e.getMessage());
        }

        return pictureFile.getAbsolutePath();
    }*/

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getActivity().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "Image_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public void displayPastIncidences(String visitorMobile) {
        FragmentManager fm = getFragmentManager();
        PastIncidencesPopup popup = new PastIncidencesPopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.VISITOR_MOBILE_NO, visitorMobile);
        //bundle.putSerializable(Constants.VHFMODEL, vhfModel);
        popup.setTargetFragment(this, BYPASS_REQUEST_CODE);
        popup.setArguments(bundle);
        popup.setCancelable(false);
        popup.show(fm, "PastIncidences");
    }

    private void sendVisitorOTP(final String iaccessToken, final String visitorMobile) {
        final Map<String, String> myParams = new HashMap<>();
        myParams.put(Constants.OTP_MOBILE_NO, visitorMobile);
        String URL = Constants.BASE_URL + "Visitorotp";
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //showProgress(false);
                        Log.d("OTP", s);
                        displayOTPPopup(visitorMobile);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // progressDialog.dismiss();
                //progress.setVisibility(View.GONE);
                VolleyLog.d("responseError", "Error: " + volleyError);
                //showProgress(false);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + iaccessToken);
                //params.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return myParams;
            }

        };

        //showProgress(true);
        //VolleyController commInstanceObj = VolleyController.getInstance(this);
        //commInstanceObj.getRequestQueue().add(jsonObjReq);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    public void displayOTPPopup(String mobileNo) {
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.VISITOR_MOBILE_NO, mobileNo);

        FragmentManager fm = getFragmentManager();
        VerifyOTPPopup popup = new VerifyOTPPopup();
        popup.setArguments(bundle);
        popup.setTargetFragment(this, GENERATE_OTP_REQUEST_CODE);
        popup.setCancelable(false);
        popup.show(fm, "Resident Names");
    }

    private void checkOTP(final String mobileNo, final String OTP) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_URL + "/Visitorotp/?MobileNo=" + mobileNo + "&OtpNumber=" + OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        popup.dismiss();
                        //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                        try {
                            Log.d("SUCCESS OTP", s);

                            try {
                                JSONObject object = new JSONObject(s);

                                int totalRes = object.getInt("TotalResults");
                                if (totalRes > 0) {
                                    vhfModel.setOTPVerified(true);
                                    Toast.makeText(getActivity().getApplicationContext(), "Correct OTP", 5000).show();
                                } else {
                                    vhfModel.setOTPVerified(false);
                                    Toast.makeText(getActivity().getApplicationContext(), "Wrong OTP entry", 5000).show();
                                }
                            } catch (JSONException e) {

                            }

                        } catch (Exception e) {
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
                Toast.makeText(getActivity().getApplicationContext(), "Wrong OTP", 5000).show();
                //showProgress(false);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
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

    public View.OnClickListener nextButtonListener = new View.OnClickListener() {
        boolean isValidData = true;

        @Override
        public void onClick(View v) {
            isValidData = true;
            if (mobile.getText().toString().length() == 0) {
                mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                mobileError.setText(mobileBlankErrorTxt);
                mobileError.setVisibility(View.VISIBLE);
                mobile.clearFocus();
            } else if (mobile.getText().toString().length() != 10) {
                mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                mobileError.setText(mobileErrorTxt);
                mobileError.setVisibility(View.VISIBLE);
                mobile.clearFocus();
            }
            if (address.getText().toString().trim().length() == 0) {
                address.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                addressError.setText(addressErrorTxt);
                addressError.setVisibility(View.VISIBLE);
                isValidData = false;
            }
            if (visitor_name.getText().toString().trim().length() == 0) {
                visitor_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                nameError.setText(nameErrorTxt);
                nameError.setVisibility(View.VISIBLE);
                isValidData = false;
            }
            if (type.getSelectedItemPosition() == 0) {
                //((TextView)type.getSelectedView()).setError("Visitor Type");
                ((TextView) type.getSelectedView()).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                isValidData = false;
            }

            if (!type.getSelectedItem().toString().equalsIgnoreCase(Constants.GUEST) && type.getSelectedItemPosition() != 0) {
                if (orgName.getText().toString().trim().length() == 0) {
                    orgName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                    orgNameError.setText(orgNameErrorTxt);
                    orgNameError.setVisibility(View.VISIBLE);
                    isValidData = false;
                }
            }

            if (!vhfModel.isVisitorPicCaptured()) {
                isValidData = false;
                //photo.setb
                photo.setImageResource(R.drawable.warning);
                photoClick.setImageResource(R.drawable.warning);
            }
            /*if (!vhfModel.isCheckedPastIncidence()) {
                Toast.makeText(getActivity().getApplicationContext(), "Please check for Past Incidences", 5000).show();
            } else */

            if(!isValidData) {
                //mobile.requestFocus();
            }
            if (vhfModel.isHasPastIncidence() && !vhfModel.isBypassFilled()) {
                Toast.makeText(getActivity().getApplicationContext(), "Please fill Bypass data for this user", 5000).show();
            } else if (isValidData) {
                VisitorNextFragment nextFrag = new VisitorNextFragment();
                vhfModel.setGateNo(gate_no.getSelectedItem().toString());
                vhfModel.setVisitorMobile(mobile.getText().toString());
                vhfModel.setVisitorName(visitor_name.getText().toString());
                vhfModel.setNoOfVisitors(vh_visitor_count.getText().toString());
                vhfModel.setVisitorAddress(address.getText().toString());
                vhfModel.setVisitorType(type.getSelectedItem().toString());
                vhfModel.setOrgName(orgName.getText() == null ? "" : orgName.getText().toString());
                vhfModel.setPicPath((picturePath != null && picturePath.length() > 0) ? picturePath : (vhfModel.getPicPath() == null) ? "" : vhfModel.getPicPath());
                vhfModel.setSocietyPurpose(forSocietyVisit);


                final Bundle bundle = new Bundle();


                bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                bundle.putString(Constants.SOCIETY_ID, societyId);
                bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

                //Utility.setVisitorHomeFragmentBundle(bundle, gateNo, visitorMobile, visitorName, noOfVisitors, visitorAddress, visitorType, orgName, picPath, forSocietyVisit);
                //Utility.setVisitorNextFragmentBundle(bundle, flatNo, residentName, vehicleNo, vehicleType, vehicleCount, narration, proofOfID, idNo);
                //Utility.setMaterialEntryFragmentBundle(bundle, staffName, materialDetails, materialStoragePlace, materialDetailsPath, materialStoragePath);

                bundle.putSerializable(Constants.VHFMODEL, vhfModel);


                nextFrag.setArguments(bundle);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.rootLayout, nextFrag);
                ft.addToBackStack(nextFrag.toString());
                ft.commit();

            }

        }
    };

    private void getVisitorHomeFragmentValues(VisitorHomeFragmentModel vhfModel) {

        /*
        gateNo = getArguments().getString(Constants.GATE_NO);
        visitorMobile  = getArguments().getString(Constants.VISITOR_MOBILE_NO);
        visitorName = getArguments().getString(Constants.VISITOR_NAME);
        noOfVisitors  = getArguments().getString(Constants.NO_OF_VISTORS);
        visitorAddress = getArguments().getString(Constants.VISITOR_ADDRESS);
        visitorType = getArguments().getString(Constants.VISITOR_TYPE);
        orgName = getArguments().getString(Constants.ORGANIZATION_NAME);
        picPath = getArguments().getString(Constants.VISITOR_PHOTO_PATH);
        forSocietyVisit = getArguments().getBoolean(Constants.SOCIETY_PURPOSE);
        */

        vhfModel.setGateNo(getArguments().getString(Constants.GATE_NO));
        vhfModel.setVisitorMobile(getArguments().getString(Constants.VISITOR_MOBILE_NO));
        vhfModel.setVisitorName(getArguments().getString(Constants.VISITOR_NAME));
        vhfModel.setNoOfVisitors(getArguments().getString(Constants.NO_OF_VISTORS));
        vhfModel.setVisitorAddress(getArguments().getString(Constants.VISITOR_ADDRESS));
        vhfModel.setVisitorType(getArguments().getString(Constants.VISITOR_TYPE));
        vhfModel.setOrgName(getArguments().getString(Constants.ORGANIZATION_NAME));
        vhfModel.setPicPath(getArguments().getString(Constants.VISITOR_PHOTO_PATH));
        vhfModel.setSocietyPurpose(getArguments().getBoolean(Constants.SOCIETY_PURPOSE));

        setVisitorHomeFragmentValues(vhfModel);

    }

    private void setVisitorHomeFragmentValues(VisitorHomeFragmentModel vhfModel) {
        //photo.setImageURI();
        if(vhfModel.hasPreviousPhoto()) {
            String URL = vhfModel.getPicPath();
            vhfModel.setPicPath(URL);
            photo.setVisibility(View.VISIBLE);
            photo.setImageUrl(URL, mimageLoader);
            photoClick.setVisibility(View.GONE);
        } else {
            File imgFile = new File(vhfModel.getPicPath());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                photoClick.setVisibility(View.VISIBLE);
                photoClick.setImageBitmap(myBitmap);
                photo.setVisibility(View.GONE);
            }
        }


        for (int i = 0; i < gateItems.length; i++) {
            if (gateItems[i].equals(vhfModel.getGateNo())) {
                gate_no.setSelection(i);
            }
        }

        mobile.setText(vhfModel.getVisitorMobile());
        visitor_name.setText(vhfModel.getVisitorName());
        vh_visitor_count.setText(vhfModel.getNoOfVisitors());
        address.setText(vhfModel.getVisitorAddress());
        try {
            for (int i = 0; i < typeList.length(); i++) {
                if ((typeList.get(i)).equals(vhfModel.getVisitorType())) {
                    type.setSelection(i);
                }
            }
        } catch (Exception e) {
        }

        orgName.setText(vhfModel.getOrgName());
        forSocietyVisit = vhfModel.isSocietyPurpose();
        society_office.setChecked(forSocietyVisit);
    }

    public void displayBypassPopup() {
        FragmentManager fm = getFragmentManager();
        SecurityPopup popup = new SecurityPopup();
        popup.setTargetFragment(this, GENERATE_BYPASS_REQUEST_CODE);
        final Bundle bundle = new Bundle();
        /*if(vhfModel.isBypassFilled()) {
            bundle.putBoolean(Constants.IS_BYPASS_FILLED, vhfModel.isBypassFilled());
            bundle.putString(Constants.BYPASS_NAME, vhfModel.getBypassName());
            bundle.putString(Constants.BYPASS_REMARK, vhfModel.getBypassRemark());
        } else {
            bundle.putBoolean(Constants.IS_BYPASS_FILLED, vhfModel.isBypassFilled());
            bundle.putString(Constants.BYPASS_NAME, vhfModel.getBypassName());
            bundle.putString(Constants.BYPASS_REMARK, vhfModel.getBypassRemark());
        }*/
        bundle.putBoolean(Constants.IS_BYPASS_FILLED, vhfModel.isBypassFilled());
        bundle.putString(Constants.BYPASS_NAME, vhfModel.getBypassName());
        bundle.putString(Constants.BYPASS_REMARK, vhfModel.getBypassRemark());
        popup.setArguments(bundle);
        //final Bundle bundle = new Bundle();
        //bundle.putSerializable(Constants.VHFMODEL, vhfModel);
        popup.setCancelable(false);
        popup.show(fm, "Bypass");
    }

    private void setBypassValues(String bypassName, String bypassRemark) {
        if(bypassName.trim().length() != 0) {
            vhfModel.setBypassName(bypassName);
        }
        if(bypassRemark.trim().length() != 0) {
            vhfModel.setBypassRemark(bypassRemark);
        }
        if(bypassName.trim().length() > 0 && bypassRemark.trim().length() > 0) {
            vhfModel.setBypassFilled(true);
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int scaleUnit) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = width * scaleUnit;
        float scaleHeight = height * scaleUnit;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.vh_mobile:
                    if (hasFocus) {
                        mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        mobileError.setVisibility(View.GONE);
                    }
                    if (!hasFocus && (mobile.getText().toString().length() == 0)) {
                        mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        mobileError.setText(mobileBlankErrorTxt);
                        mobileError.setVisibility(View.VISIBLE);
                    } else if (!hasFocus && (mobile.getText().toString().length() != 10)) {
                        mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        mobileError.setText(mobileErrorTxt);
                        mobileError.setVisibility(View.VISIBLE);
                    }
                    if ((!hasFocus) && mobile.getText().toString().length() == 10) {
                        getVisitorDetails(mobile.getText().toString());
                    } /*else {
                        vhfModel.setVisitorName("");
                        visitor_name.setText("");
                        vhfModel.setVisitorAddress("");
                        address.setText("");
                    }*/
                    break;
                case R.id.vh_visitor_name:
                    if (hasFocus) {
                        visitor_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        nameError.setVisibility(View.GONE);
                    }
                    if (!hasFocus && visitor_name.getText().toString().length() == 0) {
                        visitor_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        nameError.setText(nameErrorTxt);
                        nameError.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.edit1_3:
                    if (hasFocus) {
                        address.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        addressError.setVisibility(View.GONE);
                    }
                    if (!hasFocus && address.getText().toString().length() == 0) {
                        address.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        addressError.setText(addressErrorTxt);
                        addressError.setVisibility(View.VISIBLE);
                    }
                    if(!hasFocus) {
                        //type.requestFocus();
                        //type.performClick();
                    }
                    break;
                case R.id.type:
                    if(hasFocus) {
                        ((TextView) type.getSelectedView()).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        //type.requestFocus();
                        //type.performClick();
                        //addressError.setVisibility(View.GONE);
                    }

                    break;
                case R.id.org_name:
                    if (hasFocus) {
                        orgName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        orgNameError.setVisibility(View.GONE);
                    }
                    if (!hasFocus && orgName.getText().toString().length() == 0) {
                        orgName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        orgNameError.setText(orgNameErrorTxt);
                        orgNameError.setVisibility(View.VISIBLE);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private View.OnKeyListener gotoNextListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            Log.d("Key Pressed", "--> " + keyCode);
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                //type.requestFocus();
                switch (view.getId()) {
                    case R.id.mobile:
                        visitor_name.requestFocus();
                        break;
                    case R.id.vh_visitor_name:
                        vh_visitor_count.requestFocus();
                        break;
                    case R.id.vh_visitor_count:
                        address.requestFocus();
                        break;
                    case R.id.edit1_3:
                        Utility.hideKeyboard(getActivity(), getView());
                        type.requestFocus();
                        type.performClick();
                        break;
                    default:
                        break;

                }
            }
            return false;
        }
    };


    private void getVisitorDetails(final String mobileNo) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_URL + "/Visitors/?Mobile=" + mobileNo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                        popup.dismiss();;
                        try {
                            Log.d("SUCCESS OTP", s);
                            //if (null == mimageLoader) {
                                //mimageLoader = VolleyController.getInstance(getActivity().getApplicationContext()).getImageLoader();
                            //}
                            vhfModel.setStartDateTime(""+System.currentTimeMillis());
                            try {
                                JSONObject object = new JSONObject(s);
                                JSONArray resourceList = object.getJSONArray("ResourceList");

                                if (resourceList.length() > 0) {
                                    JSONObject resource = resourceList.getJSONObject(resourceList.length() - 1);
                                    String URL = Constants.BASE_URL + "VisitorImages/" + resource.getString(Constants.ID) + ".jpeg";
                                    checkURL(getActivity().getApplicationContext(), URL);

                                    vhfModel.setPicPath(URL);
                                    photo.setImageUrl(URL, mimageLoader);

                                    vhfModel.setVisitorName(resource.getString(Constants.VISITOR_NAME));
                                    visitor_name.setText(resource.getString(Constants.VISITOR_NAME));
                                    vhfModel.setVisitorAddress(resource.getString(Constants.VISITOR_ADDRESS));
                                    address.setText(resource.getString(Constants.VISITOR_ADDRESS));
                                    address.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    addressError.setVisibility(View.GONE);

                                    photo.setVisibility(View.VISIBLE);
                                    photoClick.setVisibility(View.GONE);

                                    //type.requestFocus();

                                    //displayPastIncidences(mobileNo);
                                    getIncidence(accessToken, societyId, mobileNo);
                                } else {
                                    vhfModel.setPicPath("");
                                    //photoClick.setImageBitmap(null);
                                    photoClick.setImageResource(R.drawable.ic_menu_camera);
                                    photoClick.setVisibility(View.VISIBLE);
                                    photo.setVisibility(View.GONE);
                                    vhfModel.setVisitorName("");
                                    visitor_name.setText("");
                                    vhfModel.setVisitorAddress("");
                                    address.setText("");
                                    vhfModel.setVisitorType("");
                                    type.setSelection(0);
                                    vhfModel.setCameraCaptured(false);

                                    vhfModel.setBypassName("");
                                    vhfModel.setBypassRemark("");
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
                Toast.makeText(getActivity().getApplicationContext(), "Error getting Visitor", 5000).show();
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


    boolean statusError = true;

    public void checkURL(Context context, final String url) {
        final boolean status = false;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("responseError", "Error: " + volleyError);
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView);
            }
        }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("My RESPONSE", "" + mStatusCode);
                if (mStatusCode != 404) {
                    statusError = false;
                    vhfModel.setCameraCaptured(true);
                    vhfModel.setVisitorPicCaptured(true);
                    vhfModel.setPreviousPhoto(true);
                    vhfModel.setCorrectPhotoURL(true);
                } else {
                    vhfModel.setVisitorPicCaptured(false);
                    vhfModel.setCorrectPhotoURL(false);
                }
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView);
        RequestQueue queue = Volley.newRequestQueue(context);
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    private void getIncidence(final String accessToken, final String societyId, final String mobileNo) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);

        String URL = Constants.BASE_URL + "SecurityIncidences/?MobileNo=" + mobileNo;
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

                            Log.d("INCIDENCE LIST", "" + object.toString());
                            Log.d("INCIDENCE LIST", "" + resourceList.length());

                            if (resourceList.length() == 0) {
                                //incidenceText = "No incidence associated with the member";
                            } else {
                                //DISPLAY PAST INCIDENCE
                                FragmentManager fm = getFragmentManager();
                                PastIncidencesPopup popup = new PastIncidencesPopup();
                                final Bundle bundle = new Bundle();
                                bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                                bundle.putString(Constants.SOCIETY_ID, societyId);
                                bundle.putString(Constants.VISITOR_MOBILE_NO, mobileNo);
                                //bundle.putSerializable(Constants.VHFMODEL, vhfModel);
                                bundle.putString(Constants.PAST_INCIDENCE, resourceList.toString());
                                popup.setTargetFragment(VisitorHomeFragment.this, BYPASS_REQUEST_CODE);
                                popup.setArguments(bundle);
                                popup.setCancelable(false);
                                popup.show(fm, "PastIncidences");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                Map<String, String> headers = new HashMap<String, String>();
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


    private void rejectVisitor() {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.SOCIETY_ID, "" + societyId);
        params.put(Constants.GATE_NO, gate_no.getSelectedItem().toString());
        params.put(Constants.VISITOR_MOBILE_NO, mobile.getEditableText().toString());
        params.put(Constants.IS_OTP_VERIFIED, "" + vhfModel.isOTPVerified());
        params.put(Constants.VISITOR_PREFIX, "Mr.");
        params.put(Constants.VISITOR_NAME, vhfModel.getVisitorName());
        params.put(Constants.NO_OF_VISTORS, "");
        params.put(Constants.VISITOR_ADDRESS, vhfModel.getVisitorAddress());
        params.put(Constants.VISITOR_TYPE, type.getSelectedItemId() == 0 ? "" : type.getSelectedItem().toString());
        params.put(Constants.ORGANIZATION_NAME, orgName.getEditableText().toString());
        params.put(Constants.SOCIETY_PURPOSE, "" + vhfModel.isSocietyPurpose());
        params.put(Constants.VISITOR_PHOTO_PATH, vhfModel.getPicPath());

        if (vhfModel.getPicPath() != null && (!vhfModel.hasPreviousPhoto())) {
          visitorPic = Utility.getImageFromPath(vhfModel.getPicPath());
        }

        //Second Fragment
        params.put(Constants.VISITOR_VEHICLE_NO, "");
        params.put(Constants.VISITOR_VEHICLE_TYPE, "");
        params.put(Constants.NO_OF_VEHICLES, "");
        params.put(Constants.NARRATION, "");
        params.put(Constants.VISITOR_GOVT_ID, "");
        params.put(Constants.VISITOR_ID_NO, "");
        params.put(Constants.SOCIETY_OFFICE_BEARER, "");

        params.put(Constants.BYPASS_NAME, "");
        params.put(Constants.BYPASS_REMARK, "");
        //params.put(Constants.IS_VISITOR_REJECTED, "" + vhfModel.isRejected());
        params.put(Constants.IS_VISIT_REJECTED, "" + vhfModel.isRejected());


        params.put(Constants.NOTIFICATION_TYPE, "Visitor Alert");
        params.put(Constants.START_DATE_TIME, vhfModel.getStartDateTime());
        params.put(Constants.END_DATE_TIME, ""+System.currentTimeMillis());

        //saveToServer(params);
        addVisitorsToServer(params);
    }

    private void addVisitorsToServer(Map<String, String> myParams) {
        Map<String, String> params = myParams;

        /*if(selectedFlatNo.size() > 0) {
            saveToServer(params, uploadCount);
        }*/
        saveToServer(params, vhfModel);

    }

    private void saveToServer(final Map<String, String> myParams, final VisitorHomeFragmentModel vhfModel) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_URL + "Visitors",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            Log.d("RESPONSE SUCCESS - ", "" + s.toString());

                            String visitorId = object.getString(Constants.ID);
                            //String visitId = object.getString("VisitId");
                            String visitId = object.getString(Constants.VISIT_ID);

                            if (!vhfModel.hasPreviousPhoto() && vhfModel.isCameraCaptured()) {
                                saveVisitorPic(visitorId, visitId);
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
                                ft.replace(R.id.rootLayout, homeFragment);
                                ft.addToBackStack(homeFragment.toString());
                                ft.commit();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView, parentScroll);
                //Toast.makeText(getApplicationContext(), myParams.toString(), 5000).show();
                Log.d("RESPONSE ERROR", volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //String accesstoken = "Oo5_Wrq-Xl0YgPlQPvDImcPab9m_o5own8Vtfa9jBJH0DSmH71BCs62spp9uyyUfHqKx-gmGc89DHIUCcQAKEyYbhoziFUbMUPg5_CrTc5fIc7Ji9o0QNIsHiWpe4YIW9hJb5Ht3A8j5qtgp_pow3B7tZF1_06JBT-zDNGbai3uJrjoQVCEiwiDiBbvhG94k44mIId2QTfa_koM1u0dzzF-QB65h5_0kzIpELeQHKhzjGEFxSvh5RQYFyyIJtZq_Hvy3EpUfc7dDrA5if3Xpy9LAu2u5PkwmoHLEppUNrXyfrhQhtb3al8HUxaaH0mOqn3T4GqcZZ-DlaSZ1ljpbLuvZnIRzj3_CsGD8TEq5CkwnU3KHEC6zdgrOrxdgL8u2";
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                //params.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (vhfModel.isSocietyPurpose()) {
                    //String societyMem = vhfModel.getSelectedSocietyBearer().get(uploadingIndex);
                    //myParams.put(Constants.SOCIETY_OFFICE_BEARER, societyMem);
                    //myParams.put(Constants.MEMBER_ID, vhfModel.getBearerMemId().get(societyMem));
                    myParams.put(Constants.FLAT_NUMBER, "");
                    myParams.put(Constants.RESIDENT_NAME, "");
                    myParams.put(Constants.SOCIETY_OFFICE_BEARER, vhfModel.getSocietyBearerStr());
                    myParams.put(Constants.MEMBER_ID, vhfModel.getSocietyBearerMemberIdStr());

                } else {
                    myParams.put(Constants.FLAT_NUMBER, "");
                    myParams.put(Constants.MEMBER_ID, "");
                    myParams.put(Constants.RESIDENT_NAME, "");
                }

                return myParams;
            }
        };


        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView, parentScroll);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }

    private void saveVisitorPic(final String visitorId, final  String visitId) {
        //final String URL = Constants.BASE_URL + "api/DocumentUpload/MediaUpload";
        final String URL = Constants.BASE_URL+"Visitors/VisitorImage";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            //JSONObject obj = new JSONObject(new String(response.data));
                            //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            //Log.d("RESPONSE SUCCESS PIC", response.toString());
                            Log.d("RESPONSE SUCCESS PIC", response.toString());

                            Toast.makeText(getActivity().getApplicationContext(), "Visitor added successfully!!", 5000).show();

                            final Bundle bundle = new Bundle();
                            bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                            bundle.putString(Constants.SOCIETY_ID, societyId);
                            bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            VisitorHomeFragment homeFragment = new VisitorHomeFragment();
                            homeFragment.setArguments(bundle);
                            ft.replace(R.id.rootLayout, homeFragment);
                            ft.addToBackStack(homeFragment.toString());
                            ft.commit();

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
                params.put("ClientImage", new DataPart(imagename + ".png", getFileDataFromDrawable(visitorPic)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);


    }


    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //bitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/4, bitmap.getHeight()/4, false);
        bitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth()*3, bitmap.getHeight()*3, false);

        int width1 = bitmap.getWidth();
        int height1 = bitmap.getHeight();
        Log.d("ORG RE-SIZE :- ", "" + width1);
        Log.d("ORG RE-SIZE :- ", "" + height1);

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        Log.d("LENGTH", ""+byteArrayOutputStream.size());
        return byteArrayOutputStream.toByteArray();
    }

    Uri fileUri;

    private View.OnClickListener photoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (vhfModel.isCameraCaptured()) {
                FragmentManager fm = getFragmentManager();
                CameraPreviewPopup popup = new CameraPreviewPopup();
                final Bundle bundle = new Bundle();
                        /*
                        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
                        bundle.putString(Constants.SOCIETY_ID, societyId);
                        bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
                        bundle.putSerializable(Constants.VHFMODEL, vhfModel);
                        */
                bundle.putString(Constants.VISITOR_PHOTO_PATH, vhfModel.getPicPath());
                popup.setTargetFragment(VisitorHomeFragment.this, CAMERA_PREVIEW_POPUP);

                popup.setArguments(bundle);
                popup.setCancelable(false);
                popup.show(fm, "Camera Preview");

            } else {
                //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                picName = "pic_";
                try {
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            }

        }
    };


}
