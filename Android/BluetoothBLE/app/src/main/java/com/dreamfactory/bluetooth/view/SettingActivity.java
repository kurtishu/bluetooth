package com.dreamfactory.bluetooth.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.service.BluetoothLeService;
import com.dreamfactory.bluetooth.view.base.BaseActivity;
import com.dreamfactory.bluetooth.view.fragment.ConfigDisplayFragment;
import com.dreamfactory.bluetooth.view.fragment.ConfigSettingFragment;

public class SettingActivity extends BaseActivity {

    // Sync readable data every five seconds
    private static final int READ_DATA_DURATION = 5000;
    private ConfigSettingFragment configSettingFragment;
    private ConfigDisplayFragment configDisplayFragment;
    private RadioGroup tabGroup;
    private BluetoothDevice mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);

        configSettingFragment = ConfigSettingFragment.newInstance();
        configDisplayFragment = new ConfigDisplayFragment();

        tabGroup = (RadioGroup) findViewById(R.id.buttonPanel);
        tabGroup.setOnCheckedChangeListener(mChangedListener);
        tabGroup.check(R.id.diaplay_button);

        mHandler.sendEmptyMessage(READ_DATA_DURATION);
    }

    private RadioGroup.OnCheckedChangeListener mChangedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.setting_button) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.content, configSettingFragment)
                        .commit();
            } else {
                getFragmentManager().beginTransaction()
                        .replace(R.id.content, configDisplayFragment)
                        .commit();
            }
        }
    };

    @Override
    protected int rootView() {
        return R.layout.activity_setting;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onReceived(Context context, Intent intent) {
        super.onReceived(context, intent);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sendCommand(BluetoothLeService.ACTION_READ_DATA);
            if (!isFinishing()) {
                mHandler.sendEmptyMessageDelayed(READ_DATA_DURATION, READ_DATA_DURATION);
            }
        }
    };
}
