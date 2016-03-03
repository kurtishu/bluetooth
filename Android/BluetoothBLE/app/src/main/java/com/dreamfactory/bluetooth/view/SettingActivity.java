package com.dreamfactory.bluetooth.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    private ConfigSettingFragment configSettingFragment;
    private ConfigDisplayFragment configDisplayFragment;
    private RadioGroup tabGroup;
    private BluetoothDevice mDevice;
    private MenuItem saveItem;

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

        sendCommand(BluetoothLeService.ACTION_READ_DATA);
    }

    private RadioGroup.OnCheckedChangeListener mChangedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.setting_button) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.content, configSettingFragment)
                        .commit();
                if (null != saveItem) {
                    saveItem.setVisible(true);
                }
            } else {
                getFragmentManager().beginTransaction()
                        .replace(R.id.content, configDisplayFragment)
                        .commit();
                if (null != saveItem) {
                    saveItem.setVisible(false);
                }
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
        saveItem = menu.findItem(R.id.action_save);
        saveItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                configSettingFragment.writeSettings();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onReceived(Context context, Intent intent) {
        super.onReceived(context, intent);
    }
}
