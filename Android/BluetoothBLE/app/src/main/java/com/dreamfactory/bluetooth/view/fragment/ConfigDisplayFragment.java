package com.dreamfactory.bluetooth.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dreamfactory.bluetooth.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigDisplayFragment extends Fragment {

    private EditText etSnoringTime;
    private EditText etQuietTime;
    private EditText etSGWorkingTime;
    private EditText etSGWorkingTimes;
    private EditText etSGStatus;
    private EditText etPumpStatus;
    private EditText etInflatedTime;
    private EditText etDeflateTime;

    public ConfigDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display, container, false);
        initViews(view);
        return view;
    }


    private void initViews(View convertView) {
        etSnoringTime = (EditText) convertView.findViewById(R.id.snoring_time);
        etQuietTime = (EditText) convertView.findViewById(R.id.quiet_time);
        etSGWorkingTime = (EditText) convertView.findViewById(R.id.sg_working_time);
        etSGWorkingTimes = (EditText) convertView.findViewById(R.id.sg_working_times);
        etSGStatus = (EditText) convertView.findViewById(R.id.sg_status);
        etPumpStatus = (EditText) convertView.findViewById(R.id.pump_status);
        etInflatedTime = (EditText) convertView.findViewById(R.id.inflated_time);
        etDeflateTime = (EditText) convertView.findViewById(R.id.deflate_time);
    }

    public void display() {

    }
}
