package com.securityapp.hukamboss.securityapp.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bhupa on 16/01/18.
 */

public class Utility {

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    /*

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, Resources resources, final View scroll, final View progress) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime);

            scroll.setVisibility(show ? View.GONE : View.VISIBLE);
            scroll.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    scroll.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            scroll.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    */


    /*
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, Context context, final View progressView) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

            //parentScroll.setVisibility(show ? View.GONE : View.VISIBLE);
            //parentScroll.animate().setDuration(shortAnimTime).alpha(
            //        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            //    @Override
            //    public void onAnimationEnd(Animator animation) {
            //       parentScroll.setVisibility(show ? View.GONE : View.VISIBLE);
            //    }
            //});

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            //parentScroll.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    */

    /*
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, Context context, final View progressView, final View parentScroll) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

            parentScroll.setVisibility(show ? View.GONE : View.VISIBLE);
            parentScroll.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    parentScroll.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            parentScroll.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    */

    public static Bitmap getImageFromPath(String path) {
        File imgFile = new  File(path);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            if (myBitmap != null) {
                return myBitmap;
            }
        }
        return null;
    }

    public static String saveToInternalStorage(Activity abc, Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(abc.getApplication().getBaseContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        //String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        long timeStamp = System.currentTimeMillis();
        String mImageName="Image_"+ timeStamp +".jpg";
        File mypath=new File(directory, mImageName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
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

    public static void setVisitorHomeFragmentBundle(Bundle bundle, String gateNo, String visitorMobile, String visitorName, String noOfVisitors, String visitorAddress, String visitorType, String orgName, String picPath, Boolean forSocietyVisit ) {
        bundle.putString(Constants.GATE_NO, gateNo);
        bundle.putString(Constants.VISITOR_MOBILE_NO, visitorMobile);
        bundle.putString(Constants.VISITOR_NAME, visitorName);
        bundle.putString(Constants.NO_OF_VISTORS, noOfVisitors);
        bundle.putString(Constants.VISITOR_ADDRESS, visitorAddress);
        bundle.putString(Constants.VISITOR_TYPE, visitorType);
        bundle.putString(Constants.ORGANIZATION_NAME, orgName);
        bundle.putString(Constants.VISITOR_PHOTO_PATH, picPath);
        bundle.putBoolean(Constants.SOCIETY_PURPOSE, forSocietyVisit);
    }

    public static void setVisitorNextFragmentBundle(Bundle bundle, String flatNo, String residentName, String vehicleNo, String vehicleType, String vehicleCount, String narration, String proofOfID, String idNo) {
        bundle.putString(Constants.FLAT_NUMBER, flatNo);
        bundle.putString(Constants.RESIDENT_NAME, residentName);
        bundle.putString(Constants.VISITOR_VEHICLE_NO, vehicleNo);
        bundle.putString(Constants.VISITOR_VEHICLE_TYPE, vehicleType);
        bundle.putString(Constants.NO_OF_VEHICLES, vehicleCount);
        bundle.putString(Constants.NARRATION, narration);
        bundle.putString(Constants.VISITOR_GOVT_ID, proofOfID);
        bundle.putString(Constants.VISITOR_ID_NO, idNo);
    }

    public static void setMaterialEntryFragmentBundle(Bundle bundle, String staffName, String materialDetails, String materialStoragePlace, String materialDetailsPath, String materialStoragePath) {
        bundle.putString(Constants.NAME_OF_STAFF, staffName);
        bundle.putString(Constants.MATERIAL_DETAILS, materialDetails);
        bundle.putString(Constants.MATERIAL_STORAGE_PLACE, materialStoragePlace);
        bundle.putString(Constants.MATERIAL_DETAILS_PATH, materialDetailsPath);
        bundle.putString(Constants.MATERIAL_STORAGE_PATH, materialStoragePath);
    }

    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //bitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/4, bitmap.getHeight()/4, false);
        bitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth()*3, bitmap.getHeight()*3, false);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        Log.d("LENGTH", ""+byteArrayOutputStream.size());
        return byteArrayOutputStream.toByteArray();
    }
    public static Boolean  IsUserLoggedIn(Context context)
    {
        boolean retVal = false;

        DBHelper dbhelper = new DBHelper(context);
        retVal = dbhelper.isUserLoginInfoPresentInSettingsTbl();
        dbhelper.close();
        return retVal;
    }

    public static Boolean  SaveUserLoginDetails(Context icontext, String itoken)
    {
        boolean retVal = false;

        DBHelper dbhelper = new DBHelper(icontext);
        retVal = dbhelper.insertIntoSettingsTbl(Constants.ACCESS_TOKEN, itoken);
        dbhelper.close();
        return retVal;
    }
    public static String getUserLoginDetails(Context icontext)
    {
        String retVal = null;

        DBHelper dbhelper = new DBHelper(icontext);
        retVal = dbhelper.getFromSettingsTbl(Constants.ACCESS_TOKEN);
        dbhelper.close();
        return retVal;
    }
    public static void removeUserLoginDetails(Context icontext) {
        DBHelper dbhelper = new DBHelper(icontext);
        dbhelper.removeFromSettingsTbl(Constants.ACCESS_TOKEN);
        dbhelper.close();
    }
    public static Boolean  SaveSocietyID(Context icontext, String isocID)
    {
        boolean retVal = false;

        DBHelper dbhelper = new DBHelper(icontext);
        retVal = dbhelper.insertIntoSettingsTbl(Constants.SOCIETY_ID, isocID);
        dbhelper.close();
        return retVal;
    }
    public static String getSocietyID(Context icontext)
    {
        String retVal = null;

        DBHelper dbhelper = new DBHelper(icontext);
        retVal = dbhelper.getFromSettingsTbl(Constants.SOCIETY_ID);
        dbhelper.close();
        return retVal;
    }
    public static void removeSocietyID(Context icontext) {
        DBHelper dbhelper = new DBHelper(icontext);
        dbhelper.removeFromSettingsTbl(Constants.SOCIETY_ID);
        dbhelper.close();
    }
    public static Boolean  SaveSocietyGateNo(Context icontext, String iGateNo)
    {
        boolean retVal = false;

        DBHelper dbhelper = new DBHelper(icontext);
        retVal = dbhelper.insertIntoSettingsTbl(Constants.SOCIETY_GATE_NO, iGateNo);
        dbhelper.close();
        return retVal;
    }
    public static String getSocietyGateNo(Context icontext)
    {
        String retVal = null;

        DBHelper dbhelper = new DBHelper(icontext);
        retVal = dbhelper.getFromSettingsTbl(Constants.SOCIETY_GATE_NO);
        dbhelper.close();
        return retVal;
    }
    public static void removeSocietyGateNo(Context icontext) {
        DBHelper dbhelper = new DBHelper(icontext);
        dbhelper.removeFromSettingsTbl(Constants.SOCIETY_GATE_NO);
        dbhelper.close();
    }

    public static float getDeviceScreenDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }

    public static int getResponseCode(String urlString) throws MalformedURLException, IOException {
        URL u = new URL(urlString);
        HttpURLConnection huc =  (HttpURLConnection)  u.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        return huc.getResponseCode();
    }

    public static boolean urlExists(String URLName){
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con =  (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void hideKeyboard(Activity activity, View viewToHide) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewToHide.getWindowToken(), 0);
    }



}
