package com.holynan.networkdetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.holynan.networklibrary.interfaces.OnPingListener;
import com.holynan.networklibrary.manager.NetworkMonitoringManager;

import static com.holynan.networklibrary.parent.PingParent.NETWORK_ETHERNET;
import static com.holynan.networklibrary.parent.PingParent.NETWORK_ETHERNET_CONNECT;
import static com.holynan.networklibrary.parent.PingParent.NETWORK_ETHERNET_DISCONNECT;
import static com.holynan.networklibrary.parent.PingParent.NETWORK_GPRS;
import static com.holynan.networklibrary.parent.PingParent.NETWORK_GPRS_CONNECT;
import static com.holynan.networklibrary.parent.PingParent.NETWORK_GPRS_DISCONNECT;
import static com.holynan.networklibrary.parent.PingParent.NETWORK_WIFI;
import static com.holynan.networklibrary.parent.PingParent.NETWORK_WIFI_CONNECT;
import static com.holynan.networklibrary.parent.PingParent.NETWORK_WIFI_DISCONNECT;

public class MainActivity extends AppCompatActivity implements OnPingListener {
    private static final String TAG = "MainActivity";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NETWORK_WIFI:
                    Toast.makeText(MainActivity.this,"networkType:接入wifi是否连接待定",Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_GPRS:
                    Toast.makeText(MainActivity.this,"networkType:接入GPRS是否连接待定",Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_ETHERNET:
                    Toast.makeText(MainActivity.this,"networkType:接入以太网是否连接待定",Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_WIFI_DISCONNECT:
                    Toast.makeText(MainActivity.this,"networkType:接入wifi但未连接",Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_GPRS_DISCONNECT:
                    Toast.makeText(MainActivity.this,"networkType:接入gprs但未连接",Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_ETHERNET_DISCONNECT:
                    Toast.makeText(MainActivity.this,"networkType:接入以太网但未连接",Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_WIFI_CONNECT:
                    Toast.makeText(MainActivity.this,"networkType:接入wifi已连接",Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_GPRS_CONNECT:
                    Toast.makeText(MainActivity.this,"networkType:接入gprs已连接",Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_ETHERNET_CONNECT:
                    Toast.makeText(MainActivity.this,"networkType:接入以太网已连接",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkMonitoringManager monitoringManager = NetworkMonitoringManager.getInstance(getApplicationContext());
        monitoringManager.getPingParent().setConnection_interval(20 * 1000);
        monitoringManager.getPingParent().setOnPingListener(this);

    }

    @Override
    public void onIsConnectListener(boolean isConnected) {
        Log.e(TAG, "isConnected:" + isConnected);
    }
    /**
     * android sdk 19 4.4 会回调两次 5.0就不会了
     */
    @Override
    public void onNetworkChange(int networkType) {
        switch (networkType) {
            case NETWORK_WIFI:
                handler.sendEmptyMessage(NETWORK_WIFI);
                Log.e(TAG, "networkType:接入wifi是否连接待定");
                break;
            case NETWORK_GPRS:
                handler.sendEmptyMessage(NETWORK_GPRS);
                Log.e(TAG, "networkType:接入GPRS是否连接待定");
                break;
            case NETWORK_ETHERNET:
                handler.sendEmptyMessage(NETWORK_ETHERNET);
                Log.e(TAG, "networkType:接入以太网是否连接待定");
                break;
            case NETWORK_WIFI_DISCONNECT:
                handler.sendEmptyMessage(NETWORK_WIFI_DISCONNECT);
                Log.e(TAG, "networkType:接入wifi但未连接");
                break;
            case NETWORK_GPRS_DISCONNECT:
                handler.sendEmptyMessage(NETWORK_GPRS_DISCONNECT);
                Log.e(TAG, "networkType:接入gprs但未连接");
                break;
            case NETWORK_ETHERNET_DISCONNECT:
                handler.sendEmptyMessage(NETWORK_ETHERNET_DISCONNECT);
                Log.e(TAG, "networkType:接入以太网但未连接");
                break;
            case NETWORK_WIFI_CONNECT:
                handler.sendEmptyMessage(NETWORK_WIFI_CONNECT);
                Log.e(TAG, "networkType:接入wifi已连接");
                break;
            case NETWORK_GPRS_CONNECT:
                handler.sendEmptyMessage(NETWORK_GPRS_CONNECT);
                Log.e(TAG, "networkType:接入gprs已连接");
                break;
            case NETWORK_ETHERNET_CONNECT:
                handler.sendEmptyMessage(NETWORK_ETHERNET_CONNECT);
                Log.e(TAG, "networkType:接入以太网已连接");
                break;
        }

    }
}