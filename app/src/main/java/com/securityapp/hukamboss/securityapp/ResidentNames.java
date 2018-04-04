package com.securityapp.hukamboss.securityapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.securityapp.hukamboss.securityapp.constants.Constants;

public class ResidentNames extends DialogFragment {
    RadioGroup residentNames;
    RadioGroup rg;
    ImageView closeButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_resident_names, container, false);
        getDialog().setTitle("Resident Names");

        residentNames = view.findViewById(R.id.resident_names);

        String residentName = getArguments().getString(Constants.RESIDENT_NAME);

        createRadioButton(residentName);

        Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Radio", ""+rg.getCheckedRadioButtonId());
                RadioButton temp = view.findViewById(rg.getCheckedRadioButtonId());
                String residentName = temp.getText().toString();
                Log.d("Radio", ""+ temp.getText().toString());

                Bundle bundle = new Bundle();
                bundle.putString("RESIDENT_NAME", residentName);
                Intent intent = new Intent().putExtras(bundle);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });

        closeButton = view.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(closePopupListener);

        /*Button cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });*/

        return view;
    }

    private void createRadioButton(String residentName) {
        final RadioButton[] rb = new RadioButton[5];
        rg = new RadioGroup(getActivity()); //create the RadioGroup
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for(int i=0; i<1; i++){
            rb[i]  = new RadioButton(getActivity());
            rb[i].setText(residentName);
            rb[i].setId(i + 100);
            rg.addView(rb[i]);
        }
        residentNames.addView(rg);//you add the whole RadioGroup to the layout
    }

    private View.OnClickListener closePopupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
}
