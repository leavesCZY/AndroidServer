package com.leavesc.androidserver.handler;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.StringEntity;
import org.apache.httpcore.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 作者：leavesC
 * 时间：2018/4/5 16:30
 * 描述：https://github.com/leavesC/AndroidServer
 * https://www.jianshu.com/u/9df45b87cfdf
 */
public class JsonHandler implements RequestHandler {

    @RequestMapping(method = {RequestMethod.POST})
    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        String content = HttpRequestParser.getContentFromBody(httpRequest);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        try {
            jsonObject.put("state", "success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity stringEntity = new StringEntity(jsonObject.toString(), "utf-8");
        httpResponse.setStatusCode(200);
        httpResponse.setEntity(stringEntity);
    }

}
