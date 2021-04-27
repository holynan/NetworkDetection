package com.holynan.networklibrary;

import android.os.Bundle;
import android.util.Log;

import com.holynan.networklibrary.interfaces.OnPingListener;
import com.holynan.networklibrary.manager.NetworkMonitoringManager;

import androidx.appcompat.app.AppCompatActivity;

public class DemoActivity extends AppCompatActivity implements OnPingListener {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        NetworkMonitoringManager monitoringManager = NetworkMonitoringManager.getInstance(getApplicationContext());
        monitoringManager.getPingParent().setConnection_interval(20 * 1000);
        monitoringManager.getPingParent().setOnPingListener(this);

    }

    @Override
    public void onIsConnectListener(boolean isConnected) {
        Log.e(TAG, "isConnected");
    }

    @Override
    public void onNetworkChange(int networkType) {
        Log.e(TAG, "networkType:" + networkType);
    }


}
