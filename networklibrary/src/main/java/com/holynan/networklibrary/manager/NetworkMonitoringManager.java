package com.holynan.networklibrary.manager;

import android.content.Context;

import com.holynan.networklibrary.parent.PingParent;
import com.holynan.networklibrary.test.Ping;


/**
 * Created by kusunoki on 2021-01-09.
 */
public class NetworkMonitoringManager {
    private Context context;
    private static NetworkMonitoringManager manager;
    private PingParent pingParent =null;
    public synchronized static NetworkMonitoringManager getInstance(Context context) {
        if (manager == null) {
            manager = new NetworkMonitoringManager(context);
        }
        return manager;
    }

    public static NetworkMonitoringManager getManager() {
        return manager;
    }

    public PingParent getPingParent() {
        if (pingParent != null) {
            return pingParent;
        } else {
            throw new NullPointerException("pingParent==null,请调用NetworkMonitoringManager的getInstance()");
        }

    }



    public NetworkMonitoringManager(Context context) {
        this.context = context;
        pingParent = new Ping(context);

    }

    /**
     * 设置断网后检测的时间间隔
     * @param millisecond
     */
    public void setConnection_interval(long millisecond) {
        if (pingParent != null) {
            pingParent.setConnection_interval(millisecond);
        }
    }
    /**
     * 设置断网后检测的次数
     * @param times
     */
//    public void setConnection_times(int times) {
//        if (testTool != null) {
//            testTool.setConnection_times(times);
//        }
//    }
    /**
     * 检测不到网络后继续检测
     * @param isConnectAgain
     */
    public void setConnectAgain(boolean isConnectAgain) {
        if (pingParent != null) {
            pingParent.setConnectAgain(isConnectAgain);
        }
    }
    /**
     * 是否进入app立即检测
     * @param isEnterAppTest
     */
    public void setEnterAppTest(boolean isEnterAppTest) {
        if (pingParent != null) {
            pingParent.setEnterAppTest(isEnterAppTest);
        }
    }

    /**
     * 是否连接到网络,能否上网不确定
     */
    public boolean isConnectToNetwork() {
        return pingParent.isConnectToNetwork();
    }
    /**
     * 是否可以上网
     */
    public boolean isOnline() {
        return pingParent.isOnline();
    }
    public int networkType() {
        return pingParent.getNetworkType();
    }
    /**
     * 设置ping url
     */
    public void setPingtUrl(String[] pingUrl) {
        pingParent.setTestURL(pingUrl);
    }
}
