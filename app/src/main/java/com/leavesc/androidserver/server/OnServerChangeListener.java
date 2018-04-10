package com.leavesc.androidserver.server;

/**
 * 作者：leavesC
 * 时间：2018/4/5 16:30
 * 描述：https://github.com/leavesC/AndroidServer
 * https://www.jianshu.com/u/9df45b87cfdf
 */
public interface OnServerChangeListener {

    void onServerStarted(String ipAddress);

    void onServerStopped();

    void onServerError(String errorMessage);

}
