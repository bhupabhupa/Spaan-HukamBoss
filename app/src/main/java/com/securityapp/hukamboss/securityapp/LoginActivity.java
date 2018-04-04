package com.securityapp.hukamboss.securityapp;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.securityapp.hukamboss.securityapp.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private View progressView;
    TextView emailError, passwordError;

    String errorTxt = "This field cannot be blank";

    private boolean isValidData;

    //Button registerSocietyButton;
    TextView newSociety;

    private static int REGISTER_SOCIETY_REQUEST_CODE = 1;

    String accessToken;
    private String societyId = "";
    private String societyGateNo = "";
    Context mCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        progressView = findViewById(R.id.login_progress);

        emailError = findViewById(R.id.email_error);
        passwordError = findViewById(R.id.password_error);

        emailError.setVisibility(View.GONE);
        passwordError.setVisibility(View.GONE);

        mEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mEmailView.getText().toString().length() == 0) {
                    mEmailView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                    emailError.setText(errorTxt);
                    emailError.setVisibility(View.VISIBLE);
                } else {
                    mEmailView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    emailError.setVisibility(View.GONE);
                }

            }
        });

        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mPasswordView.getText().toString().length() == 0) {
                    mPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                    passwordError.setText(errorTxt);
                    passwordError.setVisibility(View.VISIBLE);
                } else {
                    mPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    passwordError.setVisibility(View.GONE);
                }

            }
        });

        mEmailView.setText("1212121212");
        mPasswordView.setText("Pass@1234");

        //registerSocietyButton = findViewById(R.id.register_new_society);
        //registerSocietyButton.setOnClickListener(registerButtonListener);
        newSociety = findViewById(R.id.new_society);
        newSociety.setOnClickListener(registerButtonListener);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mCtx = getApplicationContext();
    }

    public void attemptLogin() {
        isValidData = true;

        if(mEmailView.getText().toString().trim().length() == 0) {
            //mEmailView.setError("This field cannot be blank");
            mEmailView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
            emailError.setText(errorTxt);
            emailError.setVisibility(View.VISIBLE);
            isValidData = false;
        }

        if(mPasswordView.getText().toString().trim().length() == 0) {
            //mPasswordView.setError("This field cannot be blank");
            mPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
            passwordError.setText(errorTxt);
            passwordError.setVisibility(View.VISIBLE);
            isValidData = false;
        }

        if (isValidData) {
            //Utility.showProgress(true, getApplicationContext(), progressView);
            //loginAPI();
            attemptLogin3();
        }

    }


    boolean loginSuccess;

    private boolean attemptLogin3() {
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);

        StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_URL+"token",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            popup.dismiss();
                            JSONObject object = new JSONObject(s);
                            Log.d("response", object.toString());
                            accessToken = object.getString("access_token");
                            Utility.SaveUserLoginDetails(mCtx, accessToken);
                            loginSuccess = true;

                            getSocietyID(accessToken);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getApplicationContext(), progressView);
                Toast.makeText(getApplicationContext(), "Invalid login details.", Toast.LENGTH_LONG).show();

            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "password");
                params.put("username", email);
                params.put("password", password);
                params.put(Constants.SCOPE, Constants.SCOPE_SECURITY);
                //params.put("password", "UGFzc0AxMjM0");

                return params;
            }


        };

        popup.show(fm, "Progress");
        //Utility.showProgress(true, getApplicationContext(), progressView);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

        return loginSuccess;
    }

    private void getSocietyID(final String accessToken) {
        //String URL = "http://103.48.51.62:8018/UserSociety";
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.BASE_URL+"UserSociety/?user",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //showProgress(false);
                        //Utility.showProgress(false, getApplicationContext(), progressView);
                        popup.dismiss();
                        try {
                            JSONObject object = new JSONObject(s);

                            Log.d("JSON", object.toString());
                            JSONArray resourceList = object.getJSONArray("ResourceList");
                            JSONObject resource = resourceList.getJSONObject(0);
                            societyId = resource.getString(Constants.SOCIETY_ID);
                            societyGateNo = resource.getString(Constants.SOCIETY_GATE_NO);

                            Log.d("JSON", societyId);

                            //societyId = object.get();
                            Utility.SaveSocietyID(mCtx, societyId);
                            Utility.SaveSocietyGateNo(mCtx, societyGateNo);
                            Intent intent = new Intent(LoginActivity.this, VisitorHomeActivity.class);
                            //Intent intent = new Intent(LoginActivity.this, MultipleAutoCompleteActivity.class);
                            intent.putExtra(Constants.ACCESS_TOKEN, accessToken);
                            intent.putExtra(Constants.SOCIETY_ID, societyId);
                            intent.putExtra(Constants.SOCIETY_GATE_NO, societyGateNo);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //progress.setVisibility(View.GONE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // progressDialog.dismiss();
                //progress.setVisibility(View.GONE);
                popup.dismiss();
                VolleyLog.d("responseError", "Error parsing SocietyID " + volleyError);
                //showProgress(false);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };

        //showProgress(true);
        popup.show(fm, "Progress");
        RequestQueue queue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }



    public void displayRegisterPopup() {
        FragmentManager fm = getFragmentManager();
        RegisterSocietyPopup popup = new RegisterSocietyPopup();
        popup.setCancelable(false);
        //popup.setTargetFragment(this, BYPASS_REQUEST_CODE);
        //popup.setArguments(null);
        popup.setCancelable(false);
        popup.show(fm, "PastIncidences");
    }

    public View.OnClickListener registerButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            displayRegisterPopup();
        }
    };
}

