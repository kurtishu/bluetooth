package com.dreamfactory.bluetooth.view.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dreamfactory.bluetooth.R;

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
    }

}
