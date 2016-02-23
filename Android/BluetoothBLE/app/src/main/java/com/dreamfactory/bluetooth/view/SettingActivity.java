package com.dreamfactory.bluetooth.view;

import android.bluetooth.BluetoothDevice;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.service.BluetoothLeService;
import com.dreamfactory.bluetooth.view.base.BaseActivity;
import com.dreamfactory.bluetooth.view.fragment.OthersFragment;
import com.dreamfactory.bluetooth.view.fragment.SettingDetailFragment;

public class SettingActivity extends BaseActivity {


    private SettingDetailFragment settingDetailFragment;
    private OthersFragment othersFragment;
    private RadioGroup tabGroup;
    private BluetoothDevice mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDevice = getIntent().getParcelableExtra(BluetoothLeService.INTENT_DEVICE);
        if (null == mDevice) {
            //finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);

        settingDetailFragment = SettingDetailFragment.newInstance();
        othersFragment = new OthersFragment();

        tabGroup = (RadioGroup) findViewById(R.id.buttonPanel);
        tabGroup.setOnCheckedChangeListener(mChangedListener);
        tabGroup.check(R.id.setting_button);

        //sendCommand(BluetoothLeService.ACTION_CONNECTDEVICE, mDevice);
    }

    private RadioGroup.OnCheckedChangeListener mChangedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.setting_button) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.content, settingDetailFragment)
                        .commit();
            } else {
                getFragmentManager().beginTransaction()
                        .replace(R.id.content, othersFragment)
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
                // Save data

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
