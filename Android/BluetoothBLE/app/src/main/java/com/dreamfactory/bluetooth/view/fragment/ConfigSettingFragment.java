package com.dreamfactory.bluetooth.view.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.event.WriteableSettingEvent;
import com.dreamfactory.bluetooth.util.LogUtil;
import com.dreamfactory.bluetooth.util.PreferenceUtil;
import com.dreamfactory.bluetooth.wedigt.WheelView;
import com.dreamfactory.library.model.BluetoothWriteableSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ConfigSettingFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Switch etTimingSetting;
    private TextView etStartTimingHour;
    private TextView etStartTimingMis;
    private TextView etEndTimingHour;
    private TextView etEndTimingMis;
    private TextView etInflatedTime;
    private TextView etDeflatedTime;
    private TextView etWorkingThreshold;
    private TextView etDegree;
    private TextView etWokingTime;
    private Switch etRestDevice;
    private Switch etResetData;
    private Switch etClearData;


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
        etTimingSetting = (Switch) view.findViewById(R.id.timming_setting);
        etStartTimingHour = (TextView) view.findViewById(R.id.start_timming_h);
        etStartTimingMis = (TextView) view.findViewById(R.id.start_timming_m);
        etEndTimingHour = (TextView) view.findViewById(R.id.end_timming_h);
        etEndTimingMis = (TextView) view.findViewById(R.id.end_timming_m);
        etInflatedTime = (TextView) view.findViewById(R.id.inflated_time);
        etDeflatedTime = (TextView) view.findViewById(R.id.deflate_time);
        etWorkingThreshold = (TextView) view.findViewById(R.id.working_threshold);
        etWokingTime = (TextView) view.findViewById(R.id.woking_time);
        etRestDevice = (Switch) view.findViewById(R.id.reset);
        etResetData = (Switch) view.findViewById(R.id.reset_data);
        etDegree = (TextView) view.findViewById(R.id.degree);
        etClearData = (Switch) view.findViewById(R.id.clear_data);

        etTimingSetting.setChecked(PreferenceUtil.getIntValue("timming_setting") == 1);
        etStartTimingHour.setText(String.valueOf(PreferenceUtil.getIntValue("start_timming_h")));
        etStartTimingMis.setText(String.valueOf(PreferenceUtil.getIntValue("start_timming_m")));
        etEndTimingHour.setText(String.valueOf(PreferenceUtil.getIntValue("end_timming_h")));
        etEndTimingMis.setText(String.valueOf(PreferenceUtil.getIntValue("end_timming_m")));
        etInflatedTime.setText(String.valueOf(PreferenceUtil.getIntValue("inflated_time")));
        etDeflatedTime.setText(String.valueOf(PreferenceUtil.getIntValue("deflated_time")));
        etWorkingThreshold.setText(String.valueOf(PreferenceUtil.getIntValue("working_threshold")));
        etWokingTime.setText(String.valueOf(PreferenceUtil.getIntValue("working_time")));
        etDegree.setText(String.valueOf(PreferenceUtil.getIntValue("degree")));
        etRestDevice.setChecked(PreferenceUtil.getIntValue("reset") == 1);
        etResetData.setChecked(PreferenceUtil.getIntValue("reset_data") == 1);
        etClearData.setChecked(PreferenceUtil.getIntValue("clear_data") == 1);

        etStartTimingHour.setOnClickListener(this);
        etStartTimingMis.setOnClickListener(this);
        etEndTimingHour.setOnClickListener(this);
        etEndTimingMis.setOnClickListener(this);
        etInflatedTime.setOnClickListener(this);
        etDeflatedTime.setOnClickListener(this);
        etWorkingThreshold.setOnClickListener(this);
        etWokingTime.setOnClickListener(this);
        etDegree.setOnClickListener(this);

        etTimingSetting.setOnCheckedChangeListener(this);
        etRestDevice.setOnCheckedChangeListener(this);
        etResetData.setOnCheckedChangeListener(this);
        etClearData.setOnCheckedChangeListener(this);
    }


    public void writeSettings(int[] array) {
//        BluetoothWriteableSetting setting = new BluetoothWriteableSetting();
//        setting.setTimingSetting(PreferenceUtil.getIntValue("timming_setting"));
//        setting.setStartTimingHour(PreferenceUtil.getIntValue("start_timming_h"));
//        setting.setStartTimingMis(PreferenceUtil.getIntValue("start_timming_m"));
//        setting.setEndTimingHour(PreferenceUtil.getIntValue("end_timming_h"));
//        setting.setEndTimingMis(PreferenceUtil.getIntValue("end_timming_m"));
//        setting.setInflatedTime(PreferenceUtil.getIntValue("inflated_time"));
//        setting.setDeflatedTime(PreferenceUtil.getIntValue("deflated_time"));
//        setting.setWokingTime(PreferenceUtil.getIntValue("working_time"));
//        setting.setWorkingThreshold(PreferenceUtil.getIntValue("working_threshold"));
//        setting.setDegree(PreferenceUtil.getIntValue("degree"));
//        setting.setResetData(PreferenceUtil.getIntValue("reset_data"));
//        setting.setRestDevice(PreferenceUtil.getIntValue("reset"));
//        setting.setClearData(PreferenceUtil.getIntValue("clear_data"));

        for (int i : array) {
            LogUtil.i("Kurtis", "writeSettings:" + i );
        }
        EventBus.getDefault().post(new WriteableSettingEvent(array));
    }

    private void showAlert(String title, List<String> items, final String key) {

        View outerView = LayoutInflater.from(getActivity()).inflate(R.layout.wheel_view, null);
        final WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view);
        wv.setOffset(2);
        wv.setItems(items);
        wv.setSeletion(3);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                LogUtil.d("Kurtis", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                //setSelectedValue(key, selectedIndex, item);
            }
        });

        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(outerView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setSelectedValue(key, wv.getSeletedIndex(), wv.getSeletedItem());
                    }
                })
                .show();
    }

    private void showAlert(String title, int maxLimit, final String key) {
        List<String> items = new ArrayList<>();
        for (int i = 0 ; i < maxLimit; i ++) {
            items.add("" + i);
        }

        showAlert(title, items, key);
    }


    @Override
    public void onClick(View v) {
      switch (v.getId()) {
          case R.id.start_timming_h:
              showAlert("请设置开始时间(时)", 24, "start_timming_h");
              break;
          case R.id.start_timming_m:
              showAlert("请设置开始时间(分)", 60, "start_timming_m");
              break;
          case R.id.end_timming_h:
              showAlert("请设置结束时间(时)", 24, "end_timming_h");
              break;
          case R.id.end_timming_m:
              showAlert("请设置结束时间(分)", 60, "end_timming_m");
              break;
          case R.id.inflated_time:
              showAlert("请设置充气时间", 100, "inflated_time");
              break;
          case R.id.deflate_time:
              showAlert("请设置放气时间", 100, "deflated_time");
              break;
          case R.id.working_threshold:
              showAlert("请设置工作阀值", 255, "working_threshold");
              break;
          case R.id.woking_time:
              showAlert("请设置工作耗时", 10, "working_time");
              break;
          case R.id.degree:
              showAlert("请设置软硬程度", 3, "degree");
              break;
      }
    }

    private void setSelectedValue(String key, int selectedIndex, String item) {
        if ("start_timming_h".equals(key)) {
            etStartTimingHour.setText(item);
            PreferenceUtil.setIntValue(key, selectedIndex);
            writeSettings(new int[]{1, selectedIndex});
        } else if ("start_timming_m".equals(key)) {
            etStartTimingMis.setText(item);
            PreferenceUtil.setIntValue(key, selectedIndex);
            writeSettings(new int[]{2, selectedIndex});
        } else if ("end_timming_h".equals(key)) {
            etEndTimingHour.setText(item);
            PreferenceUtil.setIntValue(key, selectedIndex);
            writeSettings(new int[]{3, selectedIndex});
        } else if ("end_timming_m".equals(key)) {
            etEndTimingMis.setText(item);
            PreferenceUtil.setIntValue(key, selectedIndex);
            writeSettings(new int[]{4, selectedIndex});
        } else if ("inflated_time".equals(key)) {
            etInflatedTime.setText(item);
            PreferenceUtil.setIntValue(key, selectedIndex);
            writeSettings(new int[]{5, selectedIndex});
        } else if ("deflated_time".equals(key)) {
            etDeflatedTime.setText(item);
            PreferenceUtil.setIntValue(key, selectedIndex);
            writeSettings(new int[]{6, selectedIndex});
        } else if ("working_threshold".equals(key)) {
            etWorkingThreshold.setText(item);
            PreferenceUtil.setIntValue(key, selectedIndex);
            writeSettings(new int[]{7, selectedIndex});
        } else if ("working_time".equals(key)) {
            etWokingTime.setText(item);
            PreferenceUtil.setIntValue(key, selectedIndex);
            writeSettings(new int[]{9, selectedIndex});
        } else if ("degree".equals(key)) {
            etDegree.setText(item);
            PreferenceUtil.setIntValue(key, selectedIndex);
            writeSettings(new int[]{8, selectedIndex});
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.timming_setting:
                PreferenceUtil.setIntValue("timming_setting", isChecked ? 1 : 0 );
                writeSettings(new int[]{0, isChecked ? 1 : 0});
                break;
            case  R.id.reset:
                PreferenceUtil.setIntValue("reset", isChecked ? 1 : 0 );
                writeSettings(new int[]{10, isChecked ? 1 : 0});
                break;
            case R.id.reset_data:
                PreferenceUtil.setIntValue("reset_data", isChecked ? 1 : 0 );
                writeSettings(new int[]{11, isChecked ? 1 : 0});
                break;
            case R.id.clear_data:
                PreferenceUtil.setIntValue("clear_data", isChecked ? 1 : 0 );
                writeSettings(new int[]{12, isChecked ? 1 : 0});
                break;
        }
    }
}
