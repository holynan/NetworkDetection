package com.holynan.networklibrary.parent;


import com.holynan.networklibrary.interfaces.OnPingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 检测类的父类
 * Created by kusunoki on 2021-01-09.
 */
public class PingParent {
    protected boolean isOnline;//是否可以上网
    public static final int NETWORK_WIFI = 1;//接入wifi 但是否有网络未定
    public static final int NETWORK_GPRS = 2;//接入gprs 但是否有网络未定
    public static final int NETWORK_ETHERNET = 3;//接入以太网 但是否有网络未定
    public static final int NETWORK_WIFI_DISCONNECT = 4;//从wifi到未连接wifi
    public static final int NETWORK_GPRS_DISCONNECT = 5;//从gprs到未连接gprs
    public static final int NETWORK_ETHERNET_DISCONNECT = 6;//从gprs到未连接gprs
    public static final int NETWORK_WIFI_CONNECT = 7;//连接到wifi
    public static final int NETWORK_GPRS_CONNECT = 8;//连接到gprs
    public static final int NETWORK_ETHERNET_CONNECT = 9;//连接到gprs
    protected int networkType;//网络类型用于显示图标
    protected boolean isConnectToNetwork;//是否连接到网络,能否上网不确定
    protected long connection_interval = 1000 * 30;//重连时间间隔
    protected boolean isConnectAgain = true;//检测不到网络后继续检测
    protected boolean isEnterAppTest = true;//是否进入app ping网络
    protected List<OnPingListener> onPingListenerList = new ArrayList<>();
    private OnPingListener onPingListener;
    protected String[] TestURL = new String[]{"www.baidu.com", "223.5.5.5", "114.114.114.114"};

    public String[] getTestURL() {
        return TestURL;
    }

    public void setTestURL(String[] testURL) {
        TestURL = testURL;
    }

    public boolean isConnectToNetwork() {
        return isConnectToNetwork;
    }

    public void setConnectToNetwork(boolean connectToNetwork) {
        isConnectToNetwork = connectToNetwork;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isEnterAppTest() {
        return isEnterAppTest;
    }

    public void setEnterAppTest(boolean enterAppTest) {
        isEnterAppTest = enterAppTest;
    }

    public OnPingListener getOnPingListener() {
        return onPingListener;
    }

    public void setOnPingListener(OnPingListener onPingListener) {
        this.onPingListener = onPingListener;
        onPingListenerList.add(onPingListener);
    }

    public long getConnection_interval() {
        return connection_interval;
    }

    public void setConnection_interval(long connection_interval) {
        this.connection_interval = connection_interval;
    }


    public boolean isConnectAgain() {
        return isConnectAgain;
    }

    public void setConnectAgain(boolean connectAgain) {
        isConnectAgain = connectAgain;
    }

    public void remove(OnPingListener onPingListener) {
        if (onPingListenerList.contains(onPingListener)) {
            onPingListenerList.remove(onPingListener);
        }
    }

}
