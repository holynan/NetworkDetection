package com.holynan.networklibrary.interfaces;

/**
 * 网络检测接口
 * Created by kusunoki on 2021-01-09.
 */
public interface OnPingListener {
    /**
     * 监听当前是否可以上网
     *
     * @param isConnected
     */
    void onIsConnectListener(boolean isConnected);
    /**
     * NETWORK_WIFI = 1;//接入wifi 但是否有网络未定
     * NETWORK_GPRS = 2;//接入gprs 但是否有网络未定
     * NETWORK_ETHERNET = 3;//接入以太网 但是否有网络未定
     * NETWORK_WIFI_DISCONNECT = 4;//从wifi到未连接wifi
     * NETWORK_GPRS_DISCONNECT = 5;//从gprs到未连接gprs
     * NETWORK_ETHERNET_DISCONNECT = 6;//从以太网到未连接以太网
     * NETWORK_WIFI_CONNECT = 7;//连接到wifi
     * NETWORK_GPRS_CONNECT = 8;//连接到gprs
     * NETWORK_ETHERNET_CONNECT = 9;//连接到以太网
     *
     * @param networkType
     */
    void onNetworkChange(int networkType);
}
