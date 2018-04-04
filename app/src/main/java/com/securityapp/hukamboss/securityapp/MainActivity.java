package com.securityapp.hukamboss.securityapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.utils.Utility;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null == Utility.getUserLoginDetails(this))
        {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(this, VisitorHomeActivity.class);
            String token = Utility.getUserLoginDetails(getApplicationContext());
            String societyId = Utility.getSocietyID(getApplicationContext());
            String societygateNo = Utility.getSocietyGateNo(getApplicationContext());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.ACCESS_TOKEN, token);
            intent.putExtra(Constants.SOCIETY_ID, societyId);
            intent.putExtra(Constants.SOCIETY_GATE_NO, societygateNo);
            startActivity(intent);
            finish();
        }
    }
}
