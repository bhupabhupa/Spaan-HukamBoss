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
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.securityapp.hukamboss.securityapp.AddIncidencePopup;
import com.securityapp.hukamboss.securityapp.CloseVisitPopup;
import com.securityapp.hukamboss.securityapp.MaterialEntryPopup;
import com.securityapp.hukamboss.securityapp.PastIncidencesPopup;
import com.securityapp.hukamboss.securityapp.R;
import com.securityapp.hukamboss.securityapp.VisitorPhotoPopup;
import com.securityapp.hukamboss.securityapp.VolleyController;
import com.securityapp.hukamboss.securityapp.constants.Constants;
import com.securityapp.hukamboss.securityapp.interfaces.VisitorInterface;
import com.securityapp.hukamboss.securityapp.model.VisitorModel;

import java.util.ArrayList;

/**
 * Created by Samman on 04/02/18.
 */

public class VisitorAdapter extends ArrayAdapter<VisitorModel> implements View.OnClickListener {
    private ArrayList<VisitorModel> originalDataSet;
    private ArrayList<VisitorModel> dataSet;
    Context mContext;
    private int lastPosition = -1;
    ImageLoader mimageLoader = null;

    VisitorInterface visitorInterface = null;

    String accessToken, societyId, societyGateNo;
    FragmentManager fragment;
    private VisitorFilter filter;

    //private int CLOSE_VISIT_REQUEST_CODE = 8;

    @Override
    public void onClick(View v) {

    }

    private static class ViewHolder {
        NetworkImageView image;
        //TextView name, mobile, in_time, out_time, type, purpose, in_gate, out_gate;
        TextView name, mobile, in_time, type;
        String visitId;
        String hasVehicle;
        Button incidence, material, visitClose;
        LinearLayout parentView;
        LinearLayout imageParent;
    }

    public VisitorAdapter(ArrayList<VisitorModel> data, Context context, FragmentManager fragment, String accessToken, String societyId, String societyGateNo, VisitorInterface _visitorInterface) {
        super(context, R.layout.visitor_list, data);
        this.dataSet = data;
        this.mContext = context;
        this.fragment = fragment;
        this.accessToken = accessToken;
        this.societyId = societyId;
        this.societyGateNo = societyGateNo;
        originalDataSet = new ArrayList<VisitorModel>();
        originalDataSet.addAll(data);
        this.visitorInterface = _visitorInterface;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new VisitorFilter();
        }
        return filter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data for this position
        VisitorModel dataModel = getItem(position);

        ViewHolder viewHolder = null;
        final View result;

        if (null == mimageLoader) {
            mimageLoader = VolleyController.getInstance(mContext).getImageLoader();
        }
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.visitor_list, parent, false);

            viewHolder.parentView = convertView.findViewById(R.id.parentView);

            viewHolder.image = (NetworkImageView) convertView.findViewById(R.id.vi_image);
            VisitorModel visObj = dataSet.get(position);
            //String URL = Constants.BASE_URL + "VisitorImages/" + visObj.getVisitorId() + ".jpeg";
            String URL = Constants.BASE_URL + "VisitImage/" + visObj.getVisitId() + ".jpeg";
            viewHolder.image.setImageUrl(URL, mimageLoader);



            viewHolder.name = (TextView) convertView.findViewById(R.id.vi_name);
            viewHolder.mobile = (TextView) convertView.findViewById(R.id.vi_mobile);
            viewHolder.in_time = (TextView) convertView.findViewById(R.id.vi_in_time);
            //viewHolder.out_time = (TextView)convertView.findViewById(R.id.vi_out_time);
            viewHolder.type = (TextView) convertView.findViewById(R.id.vi_type);
            //viewHolder.purpose = (TextView)convertView.findViewById(R.id.vi_purpose);
            //viewHolder.in_gate = (TextView)convertView.findViewById(R.id.vi_in_gate);
            //viewHolder.out_gate = (TextView)convertView.findViewById(R.id.vi_out_gate);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        //Bitmap bmp = Utility.getImageFromPath(dataModel.getImage());
        //viewHolder.image.setImageBitmap(bmp);
        viewHolder.image = (NetworkImageView) convertView.findViewById(R.id.vi_image);
        VisitorModel visObj = dataSet.get(position);
        //final String URL = Constants.BASE_URL + "VisitorImages/" + visObj.getVisitorId() + ".jpeg";
        final String URL = Constants.BASE_URL + "VisitImage/" + visObj.getVisitId() + ".jpeg";
        viewHolder.image.setImageUrl(URL, mimageLoader);

        viewHolder.name.setText(dataModel.getName());
        viewHolder.name.setTag(position);
        viewHolder.mobile.setText(dataModel.getMobile());
        viewHolder.in_time.setText(dataModel.getIn_time());
        //viewHolder.out_time.setText(dataModel.getOut_time());
        viewHolder.type.setText(dataModel.getType());
        //viewHolder.purpose.setText(dataModel.getPurpose());
        //viewHolder.in_gate.setText(dataModel.getIn_gate());
        //viewHolder.out_gate.setText(dataModel.getOut_gate());
        // Return the completed view to render on screen



        final String tempMobile = viewHolder.mobile.getText().toString();
        final String tempVisitId = dataModel.getVisitId();
        final String tempVisitorId = dataModel.getVisitorId();

        viewHolder.incidence = convertView.findViewById(R.id.vi_btn1);
        viewHolder.incidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((VisitorReportFragment)mContext).displayPastIncidences(position);
                //displayPastIncidences(tempMobile);
                addIncidence(tempVisitId, tempVisitorId);
            }
        });

//        final String tempVisitId = dataModel.getVisitorId();
        final String hasVehicle = dataModel.getHasVehicle();
        viewHolder.visitClose = convertView.findViewById(R.id.vi_btn3);
        viewHolder.visitClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((VisitorReportFragment)mContext).displayPastIncidences(position);
                displayCloseVisit(tempVisitId, hasVehicle);
            }
        });

        viewHolder.material = convertView.findViewById(R.id.vi_btn2);
        viewHolder.material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((VisitorReportFragment)mContext).displayPastIncidences(position);
                displayMaterialEntry(tempVisitId);
            }
        });

        final ImageView imageView = viewHolder.image;
        //final ViewHolder _viewHolder = viewHolder;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //displayImage(URL);
                displayLargeImagePopup(URL);
            }
        });

        return convertView;
    }

    public void displayPastIncidences(String mobile) {
        //FragmentManager fm = ((VisitorHomeActivity) mContext).getFragmentManager();
        PastIncidencesPopup popup = new PastIncidencesPopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.VISITOR_MOBILE_NO, mobile);

        popup.setArguments(bundle);
        popup.setCancelable(false);
        popup.show(fragment, "PastIncidences");
    }

    public void addIncidence(String visitId, String visiotrId) {
        //FragmentManager fm = ((VisitorHomeActivity) mContext).getFragmentManager();
        AddIncidencePopup popup = new AddIncidencePopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
        bundle.putString(Constants.VISITOR_ID, visiotrId);
        bundle.putString(Constants.VISIT_ID, visitId);

        popup.setArguments(bundle);
        popup.setCancelable(false);
        popup.show(fragment, "Add Incidence");
    }


    public void displayCloseVisit(String visitId, String hasVehicle) {
        //FragmentManager fm = ((VisitorHomeActivity) mContext).getFragmentManager();
        CloseVisitPopup popup = new CloseVisitPopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.VISIT_ID, visitId);
        bundle.putString(Constants.SOCIETY_GATE_NO, societyGateNo);
        bundle.putString(Constants.HAS_VEHICLE, hasVehicle);

        //popup.setTargetFragment(visitorReportFragment, CLOSE_VISIT_REQUEST_CODE);

        popup.setArguments(bundle);
        popup.setCancelable(false);
        popup.show(fragment, "Close Visit");
    }

    public void displayMaterialEntry(String visitId) {
        //FragmentManager fm = ((VisitorHomeActivity) mContext).getFragmentManager();
        MaterialEntryPopup popup = new MaterialEntryPopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.VISIT_ID, visitId);

        popup.setArguments(bundle);
        popup.setCancelable(false);
        popup.show(fragment, "Material");
    }

    public void displayImage(String imageUri) {
        //visitorInterface.didClickImage(imageUri, mimageLoader);
    }

    private class VisitorFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<VisitorModel> filteredItems = new ArrayList<VisitorModel>();

                for (int i = 0, l = originalDataSet.size(); i < l; i++) {
                    VisitorModel visObj = originalDataSet.get(i);
                    if (visObj.getName().toString().toLowerCase().contains(constraint))
                        filteredItems.add(visObj);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = originalDataSet;
                    result.count = originalDataSet.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            dataSet = (ArrayList<VisitorModel>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = dataSet.size(); i < l; i++)
                add(dataSet.get(i));
            notifyDataSetInvalidated();
        }
    }

    public void displayLargeImagePopup(String url) {
        VisitorPhotoPopup popup = new VisitorPhotoPopup();
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        bundle.putString(Constants.SOCIETY_ID, societyId);
        bundle.putString(Constants.IMAGE_URL, url);
        bundle.putString(Constants.TITLE, "Visitor Photo");

        popup.setArguments(bundle);
        popup.setCancelable(false);
        popup.show(fragment, "Visit Photo");
    }

    /*
    public void refreshEvents(ArrayList<VisitorModel> events) {
        //this.dataSet.clear();
        //this.dataSet.addAll(events);
        notifyDataSetChanged();
    }*/
}
