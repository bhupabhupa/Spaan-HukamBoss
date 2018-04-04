package com.securityapp.hukamboss.securityapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.model.VisitorHomeFragmentModel;

public class SecurityPopup extends DialogFragment {
    ImageView closeButton;
    EditText bypassName, bypassRemark;
    //VisitorHomeFragmentModel vhfModel;

    String bypassNameStr, bypassRemarkStr;
    boolean bypassFilled;

    TextView bypassNameError, bypassRemarkError;
    boolean isValid = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_security_popup, container, false);
        //getDialog().setTitle("ByPass Security Incidence");

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //vhfModel = (VisitorHomeFragmentModel) getArguments().getSerializable(Constants.VHFMODEL);

        bypassNameStr = getArguments().getString(Constants.BYPASS_NAME);
        bypassRemarkStr = getArguments().getString(Constants.BYPASS_REMARK);
        bypassFilled  = getArguments().getBoolean(Constants.IS_BYPASS_FILLED);

        bypassName = view.findViewById(R.id.bypass_name);
        bypassRemark = view.findViewById(R.id.bypass_remark);

        bypassName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    bypassName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    bypassNameError.setVisibility(View.GONE);
                } else if(bypassName.getText().toString().trim().length() == 0){
                    bypassName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                    bypassNameError.setText("Field cannot be blank");
                    bypassNameError.setVisibility(View.VISIBLE);
                }
            }
        });

        bypassRemark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    bypassRemark.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    bypassRemarkError.setVisibility(View.GONE);
                } else if(bypassRemark.getText().toString().trim().length() == 0){
                    bypassRemark.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                    bypassRemarkError.setText("Field cannot be blank");
                    bypassRemarkError.setVisibility(View.VISIBLE);
                }
            }
        });

        bypassNameError = view.findViewById(R.id.bypass_name_error);
        bypassNameError.setVisibility(View.GONE);
        bypassRemarkError = view.findViewById(R.id.bypass_remark_error);
        bypassRemarkError.setVisibility(View.GONE);

        if(bypassFilled) {
            bypassName.setText(bypassNameStr);
            bypassRemark.setText(bypassRemarkStr);
        }

        Button submit = view.findViewById(R.id.bypass_submit);
        submit.setOnClickListener(submitButtonListener);

        closeButton = view.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(closePopupListener);

        /*Button cancel = rootView.findViewById(R.id.bypass_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });*/

        return view;
    }

    private View.OnClickListener submitButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if(bypassName.getText().toString().trim().length() == 0){
                //bypassName.setError("Enter Name");
                bypassName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                bypassNameError.setText("Field cannot be blank");
                bypassNameError.setVisibility(View.VISIBLE);
                isValid = false;
            } else if(bypassRemark.getText().toString().trim().length() == 0){
                //bypassRemark.setError("Enter Remark");
                bypassRemark.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error, 0);
                bypassRemarkError.setText("Field cannot be blank");
                bypassRemarkError.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BYPASS_NAME, bypassName.getText().toString());
                bundle.putString(Constants.BYPASS_REMARK, bypassRemark.getText().toString());
                Intent intent = new Intent().putExtras(bundle);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }

        }
    };

    private View.OnClickListener closePopupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
}
