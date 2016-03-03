package com.dreamfactory.bluetooth.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.event.WriteableSettingEvent;
import com.dreamfactory.bluetooth.util.PreferenceUtil;
import com.dreamfactory.library.model.BluetoothWriteableSetting;

import de.greenrobot.event.EventBus;

public class ConfigSettingFragment extends Fragment {

    private EditText etTimingSetting;
    private EditText etStartTimingHour;
    private EditText etStartTimingMis;
    private EditText etEndTimingHour;
    private EditText etEndTimingMis;
    private EditText etInflatedTime;
    private EditText etDeflatedTime;
    private EditText etWorkingThreshold;
    private EditText etDegree;
    private EditText etWokingTime;
    private EditText etRestDevice;
    private EditText etResetData;
    private EditText etClearData;


    public ConfigSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConfigSettingFragment.
     */
    public static ConfigSettingFragment newInstance() {
        ConfigSettingFragment fragment = new ConfigSettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        etTimingSetting = (EditText) view.findViewById(R.id.timming_setting);
        etStartTimingHour = (EditText) view.findViewById(R.id.start_timming_h);
        etStartTimingMis = (EditText) view.findViewById(R.id.start_timming_m);
        etEndTimingHour = (EditText) view.findViewById(R.id.end_timming_h);
        etEndTimingMis = (EditText) view.findViewById(R.id.end_timming_m);
        etInflatedTime = (EditText) view.findViewById(R.id.inflated_time);
        etDeflatedTime = (EditText) view.findViewById(R.id.deflate_time);
        etWorkingThreshold = (EditText) view.findViewById(R.id.working_threshold);
        etWokingTime = (EditText) view.findViewById(R.id.woking_time);
        etRestDevice = (EditText) view.findViewById(R.id.reset);
        etResetData = (EditText) view.findViewById(R.id.reset_data);
        etDegree = (EditText) view.findViewById(R.id.degree);
        etClearData = (EditText) view.findViewById(R.id.clear_data);

        etTimingSetting.setText(String.valueOf(PreferenceUtil.getIntValue("timming_setting")));
        etStartTimingHour.setText(String.valueOf(PreferenceUtil.getIntValue("start_timming_h")));
        etStartTimingMis.setText(String.valueOf(PreferenceUtil.getIntValue("start_timming_m")));
        etEndTimingHour.setText(String.valueOf(PreferenceUtil.getIntValue("end_timming_h")));
        etEndTimingMis.setText(String.valueOf(PreferenceUtil.getIntValue("end_timming_m")));
        etInflatedTime.setText(String.valueOf(PreferenceUtil.getIntValue("inflated_time")));
        etDeflatedTime.setText(String.valueOf(PreferenceUtil.getIntValue("deflate_time")));
        etWorkingThreshold.setText(String.valueOf(PreferenceUtil.getIntValue("working_threshold")));
        etWokingTime.setText(String.valueOf(PreferenceUtil.getIntValue("working_time")));
        etRestDevice.setText(String.valueOf(PreferenceUtil.getIntValue("reset")));
        etResetData.setText(String.valueOf(PreferenceUtil.getIntValue("reset_data")));
        etDegree.setText(String.valueOf(PreferenceUtil.getIntValue("degree")));
        etClearData.setText(String.valueOf(PreferenceUtil.getIntValue("clear_data")));
    }


    public void writeSettings() {
        BluetoothWriteableSetting setting = new BluetoothWriteableSetting();
        setting.setTimingSetting(Integer.parseInt(etTimingSetting.getText().toString()));
        setting.setStartTimingHour(Integer.parseInt(etStartTimingHour.getText().toString()));
        setting.setStartTimingMis(Integer.parseInt(etStartTimingMis.getText().toString()));
        setting.setEndTimingHour(Integer.parseInt(etEndTimingHour.getText().toString()));
        setting.setEndTimingMis(Integer.parseInt(etEndTimingMis.getText().toString()));
        setting.setInflatedTime(Integer.parseInt(etInflatedTime.getText().toString()));
        setting.setDeflatedTime(Integer.parseInt(etDeflatedTime.getText().toString()));
        setting.setWokingTime(Integer.parseInt(etWokingTime.getText().toString()));
        setting.setWorkingThreshold(Integer.parseInt(etWorkingThreshold.getText().toString()));
        setting.setResetData(Integer.parseInt(etResetData.getText().toString()));
        setting.setRestDevice(Integer.parseInt(etRestDevice.getText().toString()));
        setting.setDegree(Integer.parseInt(etDegree.getText().toString()));
        setting.setClearData(Integer.parseInt(etClearData.getText().toString()));

        EventBus.getDefault().post(new WriteableSettingEvent(setting));

        PreferenceUtil.setIntValue("timming_setting", setting.getTimingSetting());
        PreferenceUtil.setIntValue("start_timming_h", setting.getStartTimingHour());
        PreferenceUtil.setIntValue("start_timming_m", setting.getStartTimingMis());
        PreferenceUtil.setIntValue("end_timming_h", setting.getEndTimingHour());
        PreferenceUtil.setIntValue("end_timming_m", setting.getEndTimingMis());
        PreferenceUtil.setIntValue("inflated_time", setting.getInflatedTime());
        PreferenceUtil.setIntValue("deflate_time", setting.getDeflatedTime());
        PreferenceUtil.setIntValue("working_threshold", setting.getWorkingThreshold());
        PreferenceUtil.setIntValue("working_time", setting.getWokingTime());
        PreferenceUtil.setIntValue("reset", setting.getRestDevice());
        PreferenceUtil.setIntValue("reset_data", setting.getResetData());
        PreferenceUtil.setIntValue("degree", setting.getDegree());
        PreferenceUtil.setIntValue("clear_data", setting.getClearData());

    }
}
