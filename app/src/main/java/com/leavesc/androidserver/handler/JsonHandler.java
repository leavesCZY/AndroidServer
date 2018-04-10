package com.leavesc.androidserver.handler;

import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.StringEntity;
import org.apache.httpcore.protocol.HttpContext;

import java.io.IOException;

/**
 * 作者：leavesC
 * 时间：2018/4/5 15:17
 * 描述：
 */
public class JsonHandler implements RequestHandler {

    private static final String TAG = "JsonHandler";

    @RequestMapping(method = {RequestMethod.POST})
    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        String content = HttpRequestParser.getContentFromBody(httpRequest);
        JsonObject jsonObject = null;
        if (!TextUtils.isEmpty(content)) {
            JsonElement jsonElement = new JsonParser().parse(content);
            if (jsonElement.isJsonObject()) {
                jsonObject = jsonElement.getAsJsonObject();
            }
        }
        if (jsonObject == null) {
            jsonObject = new JsonObject();
        }
        jsonObject.addProperty("state", "success");
        StringEntity stringEntity = new StringEntity(jsonObject.toString(), "utf-8");
        httpResponse.setStatusCode(200);
        httpResponse.setEntity(stringEntity);
    }

}
