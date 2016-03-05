package com.dreamfactory.bluetooth.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.event.ReadableSettingEvent;
import com.dreamfactory.library.model.BluetoothReadableSetting;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigDisplayFragment extends Fragment {

    private TextView etSnoringTime;
    private TextView etQuietTime;
    private TextView etSGWorkingTime;
    private TextView etSGWorkingTimes;
    private TextView etSGStatus;
    private TextView etPumpStatus;
    private TextView etInflatedTime;
    private TextView etDeflateTime;

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

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void initViews(View convertView) {
        etSnoringTime = (TextView) convertView.findViewById(R.id.snoring_time);
        etQuietTime = (TextView) convertView.findViewById(R.id.quiet_time);
        etSGWorkingTime = (TextView) convertView.findViewById(R.id.sg_working_time);
        etSGWorkingTimes = (TextView) convertView.findViewById(R.id.sg_working_times);
        etSGStatus = (TextView) convertView.findViewById(R.id.sg_status);
        etPumpStatus = (TextView) convertView.findViewById(R.id.pump_status);
        etInflatedTime = (TextView) convertView.findViewById(R.id.inflated_time);
        etDeflateTime = (TextView) convertView.findViewById(R.id.deflate_time);
    }

    public void display(BluetoothReadableSetting setting) {
        etSnoringTime.setText("" + setting.getSnoringTime());
        etQuietTime.setText("" + setting.getQuietTime());
        etSGWorkingTime.setText("" + setting.getSgWorkingTime());
        etSGWorkingTimes.setText("" + setting.getSgWorkingTimes());
        etSGStatus.setText("" + setting.getSgStatus());
        etPumpStatus.setText("" + setting.getPumpStatus());
        etInflatedTime.setText("" + setting.getInflatedTime());
        etDeflateTime.setText("" + setting.getDeflateTime());
    }

    public void onEventMainThread(ReadableSettingEvent event) {
        if (null != event && null != event.getReadableSetting()) {
            display(event.getReadableSetting());
        }
    }
}
