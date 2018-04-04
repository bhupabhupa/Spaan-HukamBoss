package com.securityapp.hukamboss.securityapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.securityapp.hukamboss.securityapp.interfaces.BaseActivityListener;
import com.securityapp.hukamboss.securityapp.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samman on 05/03/18.
 */

public abstract class BaseActivityAdapter extends BaseAdapter {
    protected Context mContext;
    protected Integer columns;
    protected ArrayList<String> entries = new ArrayList<String>();
    protected BaseActivityListener baseActivityListener;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int width = 120 ;
        int height = 50;

        float density = Utility.getDeviceScreenDensity(mContext);
        Boolean shouldShowTitles = false;
        if (position < columns) {
            shouldShowTitles = true;
        } else if (position % columns == 0) {
            shouldShowTitles = true;
        }

        if (shouldShowTitles) {
            int singleItemWidth = (int) (width * density);
            int singleItemHeight = (int) (height * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    singleItemWidth, singleItemHeight);

            TextView dummyTextView = new TextView(mContext);
            dummyTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            dummyTextView.setGravity(Gravity.CENTER_VERTICAL);
            dummyTextView.setTypeface(null, Typeface.BOLD);
            dummyTextView.setLayoutParams(params);
            dummyTextView.setBackgroundColor(Color.GRAY);
            dummyTextView.setTextColor(Color.WHITE);
            dummyTextView.setTextSize(18);
            dummyTextView.setText(entries.get(position));
            return dummyTextView;
        } else {
            int singleItemWidth = (int) (width * density);
            int singleItemHeight = (int) (height * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    singleItemWidth, singleItemHeight);

            final Button dummyButton = new Button(mContext);
            dummyButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            dummyButton.setGravity(Gravity.CENTER_VERTICAL);
            dummyButton.setLayoutParams(params);
            dummyButton.setBackgroundColor(Color.LTGRAY);
            dummyButton.setText(entries.get(position));
            dummyButton.setId(position);
            dummyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    baseActivityListener.didClickButton(entries, dummyButton.getId());
                }
            });
            return dummyButton;
        }
    }
}
