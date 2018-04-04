package com.securityapp.hukamboss.securityapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.securityapp.hukamboss.securityapp.R;
import com.securityapp.hukamboss.securityapp.model.EmergencyDirectoryModel;

import java.util.ArrayList;

/**
 * Created by Bhupa on 23/02/18.
 */

public class EmergencyDirectoryAdapter extends ArrayAdapter<EmergencyDirectoryModel> {
    private ArrayList<EmergencyDirectoryModel> dataSet;
    Context mContext;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView name, address, phoneNo;
    }

    public EmergencyDirectoryAdapter(ArrayList<EmergencyDirectoryModel> data, Context context) {
        super(context, R.layout.emergency_directory_list, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data for this position
        EmergencyDirectoryModel dataModel = getItem(position);

        EmergencyDirectoryAdapter.ViewHolder viewHolder = null;
        final View result;

        if (convertView == null) {
            viewHolder = new EmergencyDirectoryAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.emergency_directory_list, parent, false);

            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.address = (TextView)convertView.findViewById(R.id.address);
            viewHolder.phoneNo = (TextView)convertView.findViewById(R.id.phone_no);

            viewHolder.phoneNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CLICKED", "CLICKED");
                }
            });

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EmergencyDirectoryAdapter.ViewHolder)convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.name.setText(dataModel.getName());
        viewHolder.address.setText(dataModel.getAddress());
        viewHolder.phoneNo.setText(dataModel.getPhoneNo());
        // Return the completed view to render on screen
        return convertView;
    }
}

