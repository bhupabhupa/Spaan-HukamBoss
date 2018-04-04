package com.securityapp.hukamboss.securityapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.utils.Utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bhupa on 18/02/18.
 */

public class RegisterSocietyPopup extends DialogFragment {
    String[] roleNames = new String[]{"--Select Designation--", "Supervisor", "Manager", "Chairman"};
    EditText name, mobile, email, message;
    TextView nameError, roleError, mobileError, emailError, messageError;
    Spinner role;
    CheckBox agree, receivePromo;
    boolean hasAgred, canReceivePromo;

    ProgressBar progressView;
    ScrollView parentScroll;

    String errorTxt = "Field cannot be blank";
    String mobileErrorTxt = "Mobile number should be 10 digits";
    String emailErrorTxt = "Invalid email address";

    /*public RegisterSocietyPopup(@NonNull Context context) {
        super(context);
    }*/

    //Button resetButton;
    Button submitButton;
    Dialog mdlg;
    ImageView closeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_society, container, false);
        //getDialog().setTitle("Contact Us");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progressView = view.findViewById(R.id.progress);
        parentScroll = view.findViewById(R.id.parent_scroll);

        nameError = view.findViewById(R.id.name_error);
        nameError.setVisibility(View.GONE);
        roleError = view.findViewById(R.id.role_error);
        roleError.setVisibility(View.GONE);
        mobileError = view.findViewById(R.id.mobile_error);
        mobileError.setVisibility(View.GONE);
        emailError = view.findViewById(R.id.email_error);
        emailError.setVisibility(View.GONE);
        messageError = view.findViewById(R.id.message_error);
        messageError.setVisibility(View.GONE);

        name = view.findViewById(R.id.name);
        name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        mobile = view.findViewById(R.id.mobile);
        mobile.setInputType(InputType.TYPE_CLASS_NUMBER);
        mobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        email = view.findViewById(R.id.email);
        email.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        message = view.findViewById(R.id.message);
        message.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});

        /*
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    //hideKeyboard();
                    textView.clearFocus();
                    role.requestFocus();
                    role.performClick();
                }
                return true;
            }
        });*/

        role = view.findViewById(R.id.role);
        populateRoles(role);
        role.setFocusable(true);
        //role.setFocusableInTouchMode(true);
        /*role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position == 0) {
                    ((TextView)role.getSelectedView()).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                    roleError.setText("Please Select Designation");
                    roleError.setVisibility(View.VISIBLE);
                } else {
                    ((TextView)role.getSelectedView()).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    roleError.setVisibility(View.GONE);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        */

        name.setOnFocusChangeListener(focusChangeListener);
        mobile.setOnFocusChangeListener(focusChangeListener);
        email.setOnFocusChangeListener(focusChangeListener);
        message.setOnFocusChangeListener(focusChangeListener);

        name.setOnKeyListener(gotoNextListener);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (name.getText().toString().length() == 0) {
                    name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                    nameError.setText(errorTxt);
                    nameError.setVisibility(View.VISIBLE);
                } else {
                    name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    nameError.setVisibility(View.GONE);
                }

            }
        });

        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });


        agree = view.findViewById(R.id.agree);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if(checked) {
                    hasAgred = true;
                } else {
                    hasAgred = false;
                }
            }
        });

        receivePromo = view.findViewById(R.id.receive_promo);
        receivePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if(checked) {
                    canReceivePromo = true;
                } else {
                    canReceivePromo = false;
                }
            }
        });

        //resetButton = view.findViewById(R.id.cancel_button);

        closeButton = view.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(closePopupListener);

        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(submitButtonListener);

        return view;
    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register_society);

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(cancelButtonListener);

        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(submitButtonListener);
        mdlg = this;
    }
    */

    public View.OnClickListener cancelButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public View.OnClickListener submitButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //First do the validations....
            boolean isValidData = true;

            if(name.getEditableText().toString().length() == 0) {
                name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                nameError.setText(errorTxt);
                nameError.setVisibility(View.VISIBLE);
                isValidData = false;
                //name.clearFocus();
            }
            if(role.getSelectedItemPosition() == 0) {
                ((TextView)role.getSelectedView()).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                //roleError.setText(errorTxt);
                //roleError.setVisibility(View.VISIBLE);
                isValidData = false;
            }
            if(mobile.getEditableText().toString().length() == 0) {
                mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                mobileError.setText(errorTxt);
                mobileError.setVisibility(View.VISIBLE);
                isValidData = false;
                mobile.clearFocus();
            } else if(mobile.getEditableText().toString().length() != 10){
                mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                mobileError.setText(mobileErrorTxt);
                mobileError.setVisibility(View.VISIBLE);
                isValidData = false;
                mobile.clearFocus();
            }
            if(email.getEditableText().toString().length() == 0) {
                email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                emailError.setText(errorTxt);
                emailError.setVisibility(View.VISIBLE);
                isValidData = false;
                email.clearFocus();
            } else {
                boolean isValidEmail = Utility.isValidEmail(email.getText().toString());
                if(!isValidEmail) {
                    email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                    emailError.setText(emailErrorTxt);
                    emailError.setVisibility(View.VISIBLE);
                    isValidData = false;
                    email.clearFocus();
                }
            }
            if(message.getEditableText().toString().length() == 0) {
                message.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                messageError.setText(errorTxt);
                messageError.setVisibility(View.VISIBLE);
                isValidData = false;
                message.clearFocus();
            }
            if (!hasAgred) {
                Toast.makeText(getActivity().getApplicationContext(), "Please agree to receive calls", 5000).show();
                return;
            }
            if(isValidData) {
                Map<String, String> map = new HashMap<>();
                map.put(Constants.RNAME, name.getEditableText().toString());
                map.put(Constants.DESIGNATION, role.getSelectedItem().toString());
                map.put(Constants.MOBILE_NO, mobile.getEditableText().toString());
                map.put(Constants.EMAIL_ID, email.getEditableText().toString());
                map.put(Constants.MESSAGE, message.getEditableText().toString());
                map.put(Constants.IS_RECEIVE_PROMO_MAIL, ""+canReceivePromo);
                submitRegistrationData(map);
            }


        }
    };

    public void submitRegistrationData(final Map<String, String> myMap) {
        FragmentManager fm = getFragmentManager();
        final CustomProgressBar popup = new CustomProgressBar();
        popup.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_URL + "SocietyRegistration",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        popup.dismiss();
                        //Utility.showProgress(false, getActivity().getApplicationContext(), progressView, parentScroll);
                        dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Your query has been logged in with us. We will call you shortly.", 5000).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                popup.dismiss();
                //Utility.showProgress(false, getActivity().getApplicationContext(), progressView, parentScroll);
                Toast.makeText(getActivity().getApplicationContext(), "Some error occurred. Please try after sometime.", 5000).show();
                dismiss();

            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map params = myMap;
                return params;
            }


        };

        popup.show(fm, "Progress");
        //Utility.showProgress(true, getActivity().getApplicationContext(), progressView, parentScroll);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }

    public void populateRoles(Spinner dropdown) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_dropdown_item, roleNames);
        dropdown.setAdapter(adapter);
    }

    private View.OnClickListener closePopupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };


    private View.OnClickListener resetBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.name:
                    if (hasFocus) {
                        name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        //nameError.setText(errorTxt);
                        nameError.setVisibility(View.GONE);
                    }
                    if (!hasFocus && name.getText().toString().length() == 0) {
                        name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        nameError.setText(errorTxt);
                        nameError.setVisibility(View.VISIBLE);

                        //role.requestFocus();
                        //role.performClick();
                    }
                    if (!hasFocus) {
                        role.requestFocus();
                        role.performClick();
                    }
                    break;
                case R.id.mobile:
                    if (hasFocus) {
                        mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        mobileError.setText(errorTxt);
                        mobileError.setVisibility(View.GONE);
                    }
                    /*if (!hasFocus && (mobile.getText().toString().length() != 10)) {
                        mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        mobileError.setText(mobileErrorTxt);
                        mobileError.setVisibility(View.VISIBLE);
                    }*/
                    if(!hasFocus) {
                        if(mobile.getEditableText().toString().length() == 0) {
                            mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                            mobileError.setText(errorTxt);
                            mobileError.setVisibility(View.VISIBLE);
                            mobile.clearFocus();
                        } else if(mobile.getEditableText().toString().length() != 10){
                            mobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                            mobileError.setText(mobileErrorTxt);
                            mobileError.setVisibility(View.VISIBLE);
                            mobile.clearFocus();
                        }
                    }
                    break;
                case R.id.email:
                    if (hasFocus) {
                        email.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        emailError.setVisibility(View.GONE);
                    }
                    if(!hasFocus) {
                        if (email.getText().toString().length() == 0) {
                            email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                            emailError.setText(errorTxt);
                            emailError.setVisibility(View.VISIBLE);
                        } else if (!Utility.isValidEmail(email.getText().toString())) {
                            email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                            emailError.setText("Invalid email address");
                            emailError.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case R.id.message:
                    if (hasFocus) {
                        message.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        messageError.setVisibility(View.GONE);
                    }
                    if (!hasFocus && message.getText().toString().length() == 0) {
                        message.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        messageError.setText(errorTxt);
                        messageError.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int i, KeyEvent keyEvent) {
            switch (v.getId()) {
                case R.id.name:
                    if (name.getEditableText().toString().length() != 0) {
                        name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        //nameError.setText(errorTxt);
                        nameError.setVisibility(View.GONE);
                    } else {
                        name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                        nameError.setText(errorTxt);
                        nameError.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return false;
        }
    };




    private View.OnKeyListener gotoNextListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            Log.d("Key Pressed", "--> " + keyCode);
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                //type.requestFocus();
                switch (view.getId()) {
                    case R.id.name:
                        role.requestFocus();
                        role.performClick();
                        break;
                    case R.id.role:
                        mobile.requestFocus();
                        break;
                    case R.id.mobile:
                        email.requestFocus();
                        break;
                    case R.id.email:
                        message.requestFocus();
                        break;
                    default:
                        break;

                }
            }
            return false;
        }
    };




}
