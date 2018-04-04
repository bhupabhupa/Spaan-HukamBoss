package com.securityapp.hukamboss.securityapp;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Bhupa on 03/02/18.
 */

public class BottomNavigation {

    public BottomNavigationView.OnNavigationItemSelectedListener myBootomNavigationListener(final TextView mTextMessage) {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.new_visitor:
                        mTextMessage.setText(R.string.title_new_visitor);
                        return true;
                    case R.id.staff_entry_exit:
                        mTextMessage.setText(R.string.title_staff_entry_exit);
                        return true;
                    case R.id.sos:
                        mTextMessage.setText(R.string.title_sos);
                        return true;
                    case R.id.visitor_report:
                        mTextMessage.setText(R.string.title_visitor_report);
                        return true;
                    /*
                    case R.id.material_out:
                        mTextMessage.setText(R.string.title_material_out);
                        return true;*/
                }
                return false;
            }
        };

        return  mOnNavigationItemSelectedListener;

    };


}
