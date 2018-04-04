package com.securityapp.hukamboss.securityapp.adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.securityapp.hukamboss.securityapp.CloseVisitPopup;
import com.securityapp.hukamboss.securityapp.R;
import com.securityapp.hukamboss.securityapp.StaffExitPopup;
import com.securityapp.hukamboss.securityapp.VisitorPhotoPopup;
import com.securityapp.hukamboss.securityapp.VolleyController;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.model.StaffModel;
import com.securityapp.hukamboss.securityapp.model.VisitorModel;

import java.util.ArrayList;

/**
 * Created by Bhupa on 14/03/18.
 */

public class StaffListAdapter extends ArrayAdapter<StaffModel> {

    String accessToken, societyId, societyGateNo;
    private ArrayList<StaffModel> dataSet;
    Context mContext;
    FragmentManager fragment;
    private ArrayList<StaffModel> originalDataSet;

    private int lastPosition = -1;
    ImageLoader mimageLoader = null;
    Button outBtn;

    public StaffListAdapter(ArrayList<StaffModel> data, Context context, FragmentManager fragment, String accessToken, String societyId, String societyGateNo) {
        super(context, R.layout.staff_list, data);
        this.dataSet = data;
        this.mContext = context;
        this.fragment = fragment;
        this.accessToken = accessToken;
        this.societyId = societyId;
        this.societyGateNo = societyGateNo;
        originalDataSet = new ArrayList<StaffModel>();
        originalDataSet.addAll(data);
    }

    private static class ViewHolder {
        NetworkImageView image;
        TextView staffID, staffName, inDate;
        String staffIDStr;
        Button outBtn;
        LinearLayout parentView;
        LinearLayout imageParent;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        StaffModel dataModel = getItem(position);

        StaffListAdapter.ViewHolder viewHolder = null;
        final View result;

        if (null == mimageLoader) {
            mimageLoader = VolleyController.getInstance(mContext).getImageLoader();
        }

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.staff_list, parent, false);

            viewHolder.parentView = convertView.findViewById(R.id.parentView);

            StaffModel visObj = dataSet.get(position);
            viewHolder.image = (NetworkImageView) convertView.findViewById(R.id.vi_image);

            String URL = Constants.BASE_URL + "Staffimages/" + visObj.getStaffID() + ".jpeg";
            if(visObj.getStaffForm().equalsIgnoreCase("TRUE")) {
                //http://103.48.51.62:8018/VisitedStaffImages/3.jpeg
                URL = Constants.BASE_URL + "VisitedStaffImages/" + visObj.getId() + ".jpeg";
            }

            viewHolder.image.setImageUrl(URL, mimageLoader);

            viewHolder.staffID = (TextView) convertView.findViewById(R.id.staff_id);
            viewHolder.staffName = (TextView) convertView.findViewById(R.id.staff_name);
            viewHolder.inDate = (TextView) convertView.findViewById(R.id.in_date);

            outBtn = convertView.findViewById(R.id.out_button);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (StaffListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        StaffModel visObj = dataSet.get(position);

        viewHolder.image = (NetworkImageView) convertView.findViewById(R.id.vi_image);
        //final String URL = Constants.BASE_URL + "VisitorImages/" + visObj.getVisitorId() + ".jpeg";
        String URL = Constants.BASE_URL + "Staffimages/" + visObj.getStaffID() + ".jpeg";
        if(visObj.getStaffForm().equalsIgnoreCase("TRUE")) {
            //http://103.48.51.62:8018/VisitedStaffImages/3.jpeg
            URL = Constants.BASE_URL + "VisitedStaffImages/" + visObj.getId() + ".jpeg";
        }

        final String URL2 = URL;

        viewHolder.image.setImageUrl(URL, mimageLoader);

        viewHolder.staffID.setText(dataModel.getStaffID());
        viewHolder.staffName.setText(dataModel.getStaffName());
        viewHolder.inDate.setText(dataModel.getInDate());
        final String staffID = dataModel.getStaffID();
        final String id = dataModel.getId();


        outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayStaffExitPopup(id, staffID);
            }
        });

        final ImageView imageView = viewHolder.image;
        //final ViewHolder _viewHolder = viewHolder;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //displayImage(URL);
                displayLargeImagePopup(URL2);
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    private void displayStaffExitPopup(String id, String staffID) {
        StaffExitPopup popup = new StaffExitPopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.STAFF_ID, staffID);
        bundle.putString(Constants.ID, id);
        bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);

        popup.setArguments(bundle);
        popup.setCancelable(false);
        popup.show(fragment, "Staff Exit");
    }

    public void displayLargeImagePopup(String url) {
        VisitorPhotoPopup popup = new VisitorPhotoPopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.IMAGE_URL, url);
        bundle.putString(Constants.TITLE, "Staff Photo");

        popup.setArguments(bundle);
        popup.setCancelable(false);
        popup.show(fragment, "Staff Photo");
    }
}
