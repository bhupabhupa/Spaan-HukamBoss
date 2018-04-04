package com.securityapp.hukamboss.securityapp;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.fragment.SecurityDashboard;
import com.securityapp.hukamboss.securityapp.model.VisitorHomeFragmentModel;
import com.securityapp.hukamboss.securityapp.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VisitorHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, VisitorReportFragment.OnFragmentInteractionListener, SecurityDashboard.OnFragmentInteractionListener {

    private TextView mTextMessage;
    String accessToken;
    String societyId;
    String societyGateNo;
    private TextView person1, person2, person3;
    BottomNavigationView bottomNavigationView;


    MenuItem materialOut;

    int menuId = 0;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_visitor_home);

        VisitorHomeActivity.verifyStoragePermissions(this);

        Bundle bundle = getIntent().getExtras();
        //accessToken = bundle.getString(Constants.ACCESS_TOKEN);
        accessToken = bundle.getString(Constants.ACCESS_TOKEN);
        societyId = bundle.getString(Constants.SOCIETY_ID);
        societyGateNo = bundle.getString(Constants.SOCIETY_GATE_NO);

        menuId = bundle.getInt(Constants.MENU_ID);// ? 0 : bundle.getInt(Constants.MENU_ID));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*View decorView = navigationView.getRootView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        RecyclerView mRecycle = new RecyclerView(getApplicationContext());
        mRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && bottomNavigationView.isShown()) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else if (dy < 0 ) {
                    bottomNavigationView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        setupNavigationView(menuId);
    }

    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer == null) {
            super.onBackPressed();
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        //super.onBackPressed();
    }*/

    @Override
    public void onBackPressed() {
        /*Bundle bundle = new Bundle();
        Fragment abcd = getFragmentManager().getFragment(bundle, Constants.TITLE);
        Log.d("FRAGMENT COUNT", "--> "+getFragmentManager().getFragment(bundle, "ABCD"));
        if(getFragmentManager().getBackStackEntryCount() >= 2){
            super.onBackPressed();
        } else {
            Intent intent = new Intent(VisitorHomeActivity.this, VisitorHomeActivity.class);
            intent.putExtra(Constants.ACCESS_TOKEN, accessToken);
            intent.putExtra(Constants.SOCIETY_ID, societyId);
            intent.putExtra(Constants.SOCIETY_GATE_NO, societyGateNo);
            startActivity(intent);
            finish();
        }*/

        Intent intent = new Intent(VisitorHomeActivity.this, VisitorHomeActivity.class);
        intent.putExtra(Constants.ACCESS_TOKEN, accessToken);
        intent.putExtra(Constants.SOCIETY_ID, societyId);
        intent.putExtra(Constants.SOCIETY_GATE_NO, societyGateNo);
        startActivity(intent);
        finish();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.visitor_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.person1) {
            // Handle the camera action
            callMember();
        } else if (id == R.id.person2) {
            callMember();
        } else if (id == R.id.person3) {
            callMember();
        } else if (id == R.id.security) {
            displaySecurityDashboard();
        } else if (id == R.id.logout) {
            implementLogout();
        }/*
         else if (id == R.id.change_password) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void implementLogout()
    {
        Utility.removeUserLoginDetails(getApplicationContext());
        Utility.removeSocietyID(getApplicationContext());
        Utility.removeSocietyGateNo(getApplicationContext());
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void setupNavigationView(int menuID) {
        //bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if (bottomNavigationView != null) {

            // Select first menu item by default and show Fragment accordingly.
            Menu menu = bottomNavigationView.getMenu();
            selectFragment(menu.getItem(menuID));

            // Set action to perform when any menu-item is selected.
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            return selectFragment(item);
                        }
                    });
        }
    }

    protected boolean selectFragment(MenuItem item) {
        item.setChecked(true);
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

        VisitorHomeFragment nextFrag = new VisitorHomeFragment();
        //VisitorNextFragment nextFrag = new VisitorNextFragment();
        StaffEntryExitFragment staffFrag = new StaffEntryExitFragment();
        VisitorReportFragment reportFrag = new VisitorReportFragment();
        MaterialEntryFragment materialFragment = new MaterialEntryFragment();
        SOSFragment sosFragment = new SOSFragment();

        switch (item.getItemId()) {
            case R.id.new_visitor:
                // Action to perform when Home Menu item is selected.
                bundle.putString(Constants.TITLE, "VisitorHomeFragment");
                nextFrag.setArguments(bundle);
                pushFragment(nextFrag);
                break;
            case R.id.staff_entry_exit:
                bundle.putString(Constants.TITLE, "StaffEntryExitFragment");
                staffFrag.setArguments(bundle);
                pushFragmentWithoutStack(staffFrag);
                break;
            case R.id.sos:
                // Action to perform when Account Menu item is selected.
                bundle.putString(Constants.TITLE, "StaffEntryExitFragment");
                sosFragment.setArguments(bundle);
                pushFragment(sosFragment);
                break;
            case R.id.visitor_report:
                bundle.putString(Constants.TITLE, "StaffEntryExitFragment");
                reportFrag.setArguments(bundle);
                pushFragment(reportFrag);
                break;
            /*case R.id.material_out:
                materialFragment.setArguments(bundle);
                pushFragment(materialFragment);
                break;*/
            default:
                return false;
        }
        return true;
    }

    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, fragment);
                ft.addToBackStack(fragment.toString());
                ft.commit();
            }
        }
    }

    protected void pushFragmentWithoutStack(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.rootLayout, fragment);
                ft.commit();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public View.OnClickListener memberListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "9768826335", null));
            startActivity(phoneIntent);
        }
    };

    private void callMember() {
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "9768826335", null));
        startActivity(phoneIntent);
    }

    private void displaySecurityDashboard() {
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
        SecurityDashboard securityDashboard = new SecurityDashboard();
        securityDashboard.setArguments(bundle);
        pushFragment(securityDashboard);
    }



}
