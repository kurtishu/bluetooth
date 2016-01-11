package com.dreamfactory.bluetooth.view;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.view.base.BaseActivity;
import com.dreamfactory.bluetooth.view.fragment.SettingDetailFragment;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);


        getFragmentManager().beginTransaction()
                .replace(R.id.content, SettingDetailFragment.newInstance())
                .commit();

    }

    @Override
    protected int rootView() {
        return R.layout.activity_setting;
    }
}
