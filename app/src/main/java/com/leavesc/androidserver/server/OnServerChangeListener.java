package com.leavesc.androidserver.server;

/**
 * 作者：leavesC
 * 时间：2018/4/5 16:43
 * 描述：
 */
public interface OnServerChangeListener {

    void onServerStarted(String ipAddress);

    void onServerStopped();

    void onServerError(String errorMessage);

}
