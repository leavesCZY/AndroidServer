package com.leavesc.androidserver.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.leavesc.androidserver.common.Constants;
import com.leavesc.androidserver.common.NetUtils;
import com.leavesc.androidserver.handler.DownloadFileHandler;
import com.leavesc.androidserver.handler.DownloadImageHandler;
import com.leavesc.androidserver.handler.JsonHandler;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.filter.HttpCacheFilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * 作者：leavesC
 * 时间：2018/4/5 16:30
 * 描述：https://github.com/leavesC/AndroidServer
 * https://www.jianshu.com/u/9df45b87cfdf
 */
public class ServerService extends Service {

    private Server server;

    private InputStream inputStream;

    private OutputStream outputStream;

    private static final String TAG = "ServerService";

    @Override
    public void onCreate() {
        server = AndServer.serverBuilder()
                .inetAddress(NetUtils.getLocalIPAddress())
                .port(Constants.PORT_SERVER)
                .timeout(10, TimeUnit.SECONDS)
                .registerHandler(Constants.GET_FILE, new DownloadFileHandler())
                .registerHandler(Constants.GET_IMAGE, new DownloadImageHandler())
                .registerHandler(Constants.POST_JSON, new JsonHandler())
                .filter(new HttpCacheFilter())
                .listener(new Server.ServerListener() {
                    @Override
                    public void onStarted() {
                        String hostAddress = server.getInetAddress().getHostAddress();
                        Log.e(TAG, "onStarted : " + hostAddress);
                        ServerPresenter.onServerStarted(ServerService.this, hostAddress);
                    }

                    @Override
                    public void onStopped() {
                        Log.e(TAG, "onStopped");
                        ServerPresenter.onServerStopped(ServerService.this);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError : " + e.getMessage());
                        ServerPresenter.onServerError(ServerService.this, e.getMessage());
                    }
                })
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startServer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clean();
        stopServer();
    }

    private void startServer() {
        //如果Server已启动则不再重复启动，此时只是向外发布已启动的状态
        if (server.isRunning()) {
            InetAddress inetAddress = server.getInetAddress();
            if (inetAddress != null) {
                String hostAddress = inetAddress.getHostAddress();
                if (!TextUtils.isEmpty(hostAddress)) {
                    ServerPresenter.onServerStarted(ServerService.this, hostAddress);
                }
            }
        } else {
            server.startup();
        }
    }

    private void stopServer() {
        if (server != null && server.isRunning()) {
            server.shutdown();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void clean() {
        if (inputStream != null) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
                outputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
