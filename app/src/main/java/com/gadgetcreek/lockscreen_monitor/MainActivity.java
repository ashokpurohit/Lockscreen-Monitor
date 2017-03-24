package com.gadgetcreek.lockscreen_monitor;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int ADMIN_INTENT = 1;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;
    private FloatingActionButton fab;
    SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDevicePolicyManager = (DevicePolicyManager)getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, Admin.class);
        switchCompat=(SwitchCompat)findViewById(R.id.switchButton);

        if( mDevicePolicyManager != null && mDevicePolicyManager.isAdminActive(mComponentName)) {
            switchCompat.setChecked(true);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if ((switchCompat).isChecked()) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Administrator description");
                    startActivityForResult(intent, ADMIN_INTENT);
                }
                else {
                    mDevicePolicyManager.removeActiveAdmin(mComponentName);
                }

            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);
                if (isAdmin) {
                    mDevicePolicyManager.lockNow();
                }else {
                    Snackbar snackbar = Snackbar.make(view, "Turn ON Lockscreen Monitor", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADMIN_INTENT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Registered As Admin", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Device Administrator Process Cancelled", Toast.LENGTH_SHORT).show();
                switchCompat.setChecked(false);
            }
        }
    }
}