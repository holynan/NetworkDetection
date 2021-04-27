package com.holynan.networklibrary.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.holynan.networklibrary.interfaces.OnPingListener;
import com.holynan.networklibrary.parent.PingParent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.ConnectivityManager.TYPE_ETHERNET;

/**
 * Created by kusunoki on 2021-01-09.
 */
public class Ping extends PingParent {
    private static final String TAG = "Ping";



    private int network = -1;
    private boolean isChangeTheNetworkPing = true;//是否改变网络 ping网络
    private final ExecutorService threadP;
    private Context mContext;
    private boolean isFirst = true;
    private boolean isPinging;
    //    //初始化
    public Ping(Context mContext) {
        this.mContext = mContext;
        threadP = initThreadPool();
        initReceiver(mContext);
        if (super.isEnterAppTest) {
            isOnline = false;
        }

    }

    public boolean isOnline() {
        return isOnline;
    }


    public String[] getTestURL() {
        return TestURL;
    }

    public void setTestURL(String[] testURL) {
        TestURL = testURL;
    }


    public boolean isChangeTheNetworkPing() {
        return isChangeTheNetworkPing;
    }

    public void setChangeTheNetworkPing(boolean changeTheNetworkPing) {
        isChangeTheNetworkPing = changeTheNetworkPing;
    }


    private  boolean isNetworkOnline() {
        Runtime runtime = Runtime.getRuntime();
        Process ipProcess = null;
        try {
            for (int i = 0; i < TestURL.length; i++) {
                ipProcess = runtime.exec("ping -c 4 -w 4 "+TestURL[i]);
                InputStream input = ipProcess.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                StringBuffer stringBuffer = new StringBuffer();
                String content = "";
                while ((content = in.readLine()) != null) {
                    stringBuffer.append(content);
                }

                int exitValue = ipProcess.waitFor();
                if (exitValue == 0) {
                    //WiFi连接，网络正常
                    Log.i(TAG, TestURL[i] + "成功");
                    return true;

                } else {
                    if (stringBuffer.indexOf("100% packet loss") != -1) {
                        //网络丢包严重，判断为网络未连接
                        Log.i(TAG, TestURL[i] + "失败");
                        continue;
                    }  else if (stringBuffer.toString().equals("")){
                        //网络未丢包，判断为网络连接
                        Log.i(TAG, TestURL[i] + "失败");
                        continue;
                    } else {
                        Log.i(TAG, TestURL[i] + "成功");
                        return true;
                    }
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (ipProcess != null) {
                ipProcess.destroy();
            }
            runtime.gc();
        }
        return false;
    }

    private void initReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(netBroad, intentFilter);
    }

    /**
     * android sdk 19 4.4 会回调两次不知道为什么
     */
    BroadcastReceiver netBroad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo gprs = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo ethernet = connectivityManager.getNetworkInfo(TYPE_ETHERNET);
                if (wifi!=null&wifi.isConnected() && networkType != NETWORK_WIFI) {
                    network = NETWORK_WIFI;
                    isConnectToNetwork = true;
                    networkType = NETWORK_WIFI;
                    isOnline = false;
                    Log.e(TAG, "wifi");
                    for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                        if (onPingListener != null) {
                            onPingListener.onNetworkChange(NETWORK_WIFI);
                        }
                    }

                    if (threadP != null) {
                        if (isFirst) {//进入app
                            if (Ping.super.isEnterAppTest) {//是否进入app ping
                                ping(threadP, runnable);
                            }
                        } else {
                            if (isChangeTheNetworkPing) {
                                ping(threadP, runnable);
                            }

                        }

                    }

                } else if (gprs != null && gprs.isConnected() && networkType != NETWORK_GPRS) {
                    network = NETWORK_GPRS;
                    networkType = NETWORK_GPRS;
                    isConnectToNetwork = true;
                    isOnline = false;
                    Log.e(TAG, "gprs");
                    for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                        if (onPingListener != null) {
                            onPingListener.onNetworkChange(NETWORK_GPRS);
                        }
                    }
                    if (threadP != null) {
                        if (isFirst) {//进入app
                            if (Ping.super.isEnterAppTest) {//是否进入app ping
                                ping(threadP, runnable);
                            }
                        } else {
                            if (isChangeTheNetworkPing) {
                                ping(threadP, runnable);
                            }
                        }
                    }
                } else  if (ethernet != null && ethernet.isConnected() && networkType != NETWORK_ETHERNET) {
                    network = NETWORK_ETHERNET;
                    networkType = NETWORK_ETHERNET;
                    isConnectToNetwork = true;
                    isOnline = false;
                    Log.e(TAG, "以太网");
                    for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                        if (onPingListener != null) {
                            onPingListener.onNetworkChange(NETWORK_ETHERNET);
                        }
                    }
                    if (threadP != null) {
                        if (isFirst) {//进入app
                            if (Ping.super.isEnterAppTest) {//是否进入app ping
                                ping(threadP, runnable);
                            }
                        } else {
                            if (isChangeTheNetworkPing) {
                                ping(threadP, runnable);
                            }
                        }
                    }
                }
            } else {
                if (network != 0) {
                    network = 0;
                    if (networkType==NETWORK_WIFI) {//从wifi到未连接wifi
                        networkType = NETWORK_WIFI_DISCONNECT;
                        for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                            if (onPingListener != null) {
                                onPingListener.onNetworkChange(NETWORK_WIFI_DISCONNECT);
                            }
                        }
                    } else if (networkType==NETWORK_GPRS) {//从gprs到未连接gprs
                        networkType = NETWORK_GPRS_DISCONNECT;
                        for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                            if (onPingListener != null) {
                                onPingListener.onNetworkChange(NETWORK_GPRS_DISCONNECT);
                            }
                        }
                    } else if (networkType==NETWORK_ETHERNET) {//从以太网到未连接以太网
                        networkType = NETWORK_ETHERNET_DISCONNECT;
                        for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                            if (onPingListener != null) {
                                onPingListener.onNetworkChange(NETWORK_ETHERNET_DISCONNECT);
                            }
                        }
                    }
                    isConnectToNetwork = false;
                    isOnline = false;
                    Log.e(TAG, "无网络");
                    for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                        if (onPingListener != null) {
                            onPingListener.onIsConnectListener(false);
                        }
                    }
                }

            }
            isFirst = false;
        }
    };



    private void ping(ExecutorService threadpool, Runnable runnable) {
        if (isPinging == false) {
            isPinging = true;
            handler.removeCallbacks(runnable);
            Log.i(TAG, "threadpool.isTerminated()" + threadpool.isTerminated());
            if (threadpool != null) {
                try {
                    threadpool.execute(runnable);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        } else {
            Log.e(TAG, "正在ping return");
        }

    }
    private ExecutorService initThreadPool() {//初始化线程池
        ExecutorService threadpool = Executors.newFixedThreadPool(1);
        return threadpool;
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://
                    Log.e(TAG, "ping成功");
                    if (isOnline == false&&network!=0) {
                        isOnline = true;
                        for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                            if (onPingListener != null) {
                                Log.e(TAG, "更新连接状态:true");
                                onPingListener.onIsConnectListener(true);
                            }
                        }
                        if (networkType==NETWORK_WIFI) {//连接wifi
                            for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                                if (onPingListener != null) {
                                    Log.e(TAG, "连接状态 NETWORK_WIFI_CONNECT");
                                    onPingListener.onNetworkChange(NETWORK_WIFI_CONNECT);
                                }
                            }
                        } else if (networkType==NETWORK_GPRS) {//连接gprs
                            for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                                if (onPingListener != null) {
                                    Log.e(TAG, "连接状态 NETWORK_GPRS_CONNECT");
                                    onPingListener.onNetworkChange(NETWORK_GPRS_CONNECT);
                                }
                            }
                        }else if (networkType==NETWORK_ETHERNET) {//连接以太网
                            for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                                if (onPingListener != null) {
                                    Log.e(TAG, "连接状态 NETWORK_ETHERNET_CONNECT");
                                    onPingListener.onNetworkChange(NETWORK_ETHERNET_CONNECT);
                                }
                            }
                        }
                    }
                    break;
                case 1:
                    Log.e(TAG, "ping失败");
                    if (isConnectToNetwork == true) {
                        isOnline = false;
                        for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                            if (onPingListener != null) {
                                Log.e(TAG, "更新连接状态:false");
                                onPingListener.onIsConnectListener(false);
                            }
                        }
                        if (networkType==NETWORK_WIFI) {
                            for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                                if (onPingListener != null) {
                                    Log.e(TAG, "连接状态 NETWORK_WIFI");
                                    onPingListener.onNetworkChange(NETWORK_WIFI);
                                }
                            }
                        } else if (networkType==NETWORK_GPRS) {//从gprs到未连接gprs上网失败
                            for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                                if (onPingListener != null) {
                                    Log.e(TAG, "连接状态 NETWORK_GPRS");
                                    onPingListener.onNetworkChange(NETWORK_GPRS);
                                }
                            }
                        }else if (networkType==NETWORK_ETHERNET) {//从以太网到未连接以太网上网失败
                            for (OnPingListener onPingListener : Ping.super.onPingListenerList) {
                                if (onPingListener != null) {
                                    Log.e(TAG, "连接状态 NETWORK_ETHERNET");
                                    onPingListener.onNetworkChange(NETWORK_ETHERNET);
                                }
                            }
                        }
                    }
                    break;
            }
        }
    };
    Runnable runnablePing = new Runnable() {
        @Override
        public void run() {
            ping(threadP,runnable);
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "ping......");
            if (network == 0) {
                handler.postDelayed(runnablePing, Ping.super.connection_interval);
                Log.e(TAG, connection_interval+"毫秒后继续ping;");
            } else {
                boolean u = isNetworkOnline();
                if (u) {//可以连接到网络
                    handler.sendEmptyMessage(0);
                    Log.e(TAG, connection_interval+"毫秒后继续ping;");
                    handler.postDelayed(runnablePing, Ping.super.connection_interval);
                } else {
                    handler.sendEmptyMessage(1);
                    if (isConnectAgain == true) {
                        Log.e(TAG, connection_interval+"毫秒后继续ping;");
                        handler.postDelayed(runnablePing, Ping.super.connection_interval);
                    }
                }
            }

            isPinging = false;
        }
    };



}
