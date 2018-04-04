package com.securityapp.hukamboss.securityapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.securityapp.hukamboss.securityapp.R;
import com.securityapp.hukamboss.securityapp.model.PastIncidenceModel;
import com.securityapp.hukamboss.securityapp.model.VisitorModel;
import com.securityapp.hukamboss.securityapp.utils.Utility;

import java.util.ArrayList;

/**
 * Created by Samman on 05/02/18.
 */

public class PastIncidencesAdapter extends ArrayAdapter<PastIncidenceModel> {
    private ArrayList<PastIncidenceModel> dataSet;
    Context mContext;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView date, time, flat, description;
    }

    public PastIncidencesAdapter(ArrayList<PastIncidenceModel> data, Context context) {
        super(context, R.layout.past_incidence, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data for this position
        PastIncidenceModel dataModel = getItem(position);

        PastIncidencesAdapter.ViewHolder viewHolder = null;
        final View result;

        if (convertView == null) {
            viewHolder = new PastIncidencesAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.past_incidence, parent, false);

            viewHolder.date = (TextView)convertView.findViewById(R.id.pi_date);
            viewHolder.time = (TextView)convertView.findViewById(R.id.pi_time);
            viewHolder.flat = (TextView)convertView.findViewById(R.id.pi_flat);
            viewHolder.description = (TextView)convertView.findViewById(R.id.pi_description);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PastIncidencesAdapter.ViewHolder)convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.date.setText(dataModel.getDate());
        viewHolder.date.setTag(position);
        viewHolder.time.setText(dataModel.getTime());
        viewHolder.flat.setText(dataModel.getFlat());
        viewHolder.description.setText(dataModel.getDescription());
        // Return the completed view to render on screen
        return convertView;
    }
}
