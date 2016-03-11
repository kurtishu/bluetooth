package com.dreamfactory.bluetooth.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.event.ReadableSettingEvent;
import com.dreamfactory.library.model.BluetoothReadableSetting;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigDisplayFragment extends Fragment {

    private TextView tvSnoringTime;
    private TextView tvQuietTime;
    private TextView tvSGWorkingTime;
    private TextView tvSGWorkingTimes;
    private TextView tvSGStatus;
    private TextView tvPumpStatus;
    private TextView tvInflatedTime;
    private TextView tvDeflateTime;
    private View contentView;

    public ConfigDisplayFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (null == contentView) {
            contentView = inflater.inflate(R.layout.fragment_display, container, false);
            initViews(contentView);
        }

        return contentView;
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
        tvSnoringTime = (TextView) convertView.findViewById(R.id.snoring_time);
        tvQuietTime = (TextView) convertView.findViewById(R.id.quiet_time);
        tvSGWorkingTime = (TextView) convertView.findViewById(R.id.sg_working_time);
        tvSGWorkingTimes = (TextView) convertView.findViewById(R.id.sg_working_times);
        tvSGStatus = (TextView) convertView.findViewById(R.id.sg_status);
        tvPumpStatus = (TextView) convertView.findViewById(R.id.pump_status);
        tvInflatedTime = (TextView) convertView.findViewById(R.id.inflated_time);
        tvDeflateTime = (TextView) convertView.findViewById(R.id.deflate_time);
    }

    public void display(BluetoothReadableSetting setting) {
        tvSnoringTime.setText(setting.getSnoringTime());
        tvQuietTime.setText(setting.getQuietTime());
        tvSGWorkingTime.setText("" + setting.getSgWorkingTime());
        tvSGWorkingTimes.setText(setting.getSgWorkingTimes());
        tvSGStatus.setText(setting.getSgStatus());
        tvPumpStatus.setText(setting.getPumpStatus());
        tvInflatedTime.setText("" + setting.getInflatedTime());
        tvDeflateTime.setText("" + setting.getDeflateTime());
    }

    public void onEventMainThread(ReadableSettingEvent event) {
        if (null != event && null != event.getReadableSetting()) {
            display(event.getReadableSetting());
        }
    }
}
