package com.dreamfactory.bluetooth.view;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.view.base.BaseActivity;
import com.dreamfactory.bluetooth.view.fragment.OthersFragment;
import com.dreamfactory.bluetooth.view.fragment.SettingDetailFragment;

public class SettingActivity extends BaseActivity {


    private SettingDetailFragment settingDetailFragment;
    private OthersFragment othersFragment;
    private RadioGroup tabGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);

        settingDetailFragment = SettingDetailFragment.newInstance();
        othersFragment = new OthersFragment();


        tabGroup = (RadioGroup) findViewById(R.id.buttonPanel);
        tabGroup.setOnCheckedChangeListener(mChangedListener);
        tabGroup.check(R.id.setting_button);

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
}
