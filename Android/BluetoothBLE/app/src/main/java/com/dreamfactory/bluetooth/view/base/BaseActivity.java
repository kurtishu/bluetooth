package com.dreamfactory.bluetooth.view.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dreamfactory.bluetooth.R;
import com.dreamfactory.bluetooth.wedigt.SystemBarTintManager;

/**
 * Author：kurtishu on 1/11/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(rootView());
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
    }

    protected abstract int rootView();
}
