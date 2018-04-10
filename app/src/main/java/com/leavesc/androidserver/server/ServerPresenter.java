package com.leavesc.androidserver.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 作者：leavesC
 * 时间：2018/4/5 16:30
 * 描述：https://github.com/leavesC/AndroidServer
 * https://www.jianshu.com/u/9df45b87cfdf
 */
public class ServerPresenter {

    private static final String ACTION_SERVER_CHANGE = "com.leavesc.androidserver.action_server_change";

    private static final String KEY_SERVER_STATE = "serverState";

    private static final String KEY_SERVER_MESSAGE = "serverMessage";

    private static final int VALUE_STARTED = 100;

    private static final int VALUE_STOPPED = 200;

    private static final int VALUE_ERROR = 300;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(KEY_SERVER_STATE, 0);
            switch (state) {
                case VALUE_STARTED: {
                    if (serverChangeListener != null) {
                        serverChangeListener.onServerStarted(intent.getStringExtra(KEY_SERVER_MESSAGE));
                    }
                    break;
                }
                case VALUE_STOPPED: {
                    if (serverChangeListener != null) {
                        serverChangeListener.onServerStopped();
                    }
                    break;
                }
                case VALUE_ERROR: {
                    if (serverChangeListener != null) {
                        serverChangeListener.onServerError(intent.getStringExtra(KEY_SERVER_MESSAGE));
                    }
                    break;
                }
            }
        }
    };

    private OnServerChangeListener serverChangeListener;

    public ServerPresenter(Context context, OnServerChangeListener serverChangeListener) {
        context.registerReceiver(broadcastReceiver, new IntentFilter(ACTION_SERVER_CHANGE));
        this.serverChangeListener = serverChangeListener;
    }

    public void startServer(Context context) {
        context.startService(new Intent(context, ServerService.class));
    }

    public void stopServer(Context context) {
        context.stopService(new Intent(context, ServerService.class));
    }

    public void unregister(Context context) {
        context.unregisterReceiver(broadcastReceiver);
        this.serverChangeListener = null;
    }

    public static void onServerStarted(Context context, String ipAddress) {
        Intent intent = new Intent(ACTION_SERVER_CHANGE);
        intent.putExtra(KEY_SERVER_STATE, VALUE_STARTED);
        intent.putExtra(KEY_SERVER_MESSAGE, ipAddress);
        context.sendBroadcast(intent);
    }

    public static void onServerStopped(Context context) {
        Intent intent = new Intent(ACTION_SERVER_CHANGE);
        intent.putExtra(KEY_SERVER_STATE, VALUE_STOPPED);
        context.sendBroadcast(intent);
    }

    public static void onServerError(Context context, String message) {
        Intent intent = new Intent(ACTION_SERVER_CHANGE);
        intent.putExtra(KEY_SERVER_STATE, VALUE_ERROR);
        intent.putExtra(KEY_SERVER_MESSAGE, message);
        context.sendBroadcast(intent);
    }

}
