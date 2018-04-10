package com.leavesc.androidserver.handler;

import com.leavesc.androidserver.MainApplication;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;

import org.apache.httpcore.HttpEntity;
import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.entity.ContentType;
import org.apache.httpcore.entity.FileEntity;
import org.apache.httpcore.protocol.HttpContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static com.yanzhenjie.andserver.util.FileUtils.getMimeType;

/**
 * 作者：leavesC
 * 时间：2018/4/5 14:50
 * 描述：
 */
public class DownloadFileHandler implements RequestHandler {

    @RequestMapping(method = {RequestMethod.GET})
    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        File file = createFile();
        HttpEntity httpEntity = new FileEntity(file, ContentType.create(getMimeType(file.getAbsolutePath()), Charset.defaultCharset()));
        httpResponse.setHeader("Content-Disposition", "attachment;filename=File.txt");
        httpResponse.setStatusCode(200);
        httpResponse.setEntity(httpEntity);
    }

    private File createFile() {
        File file = null;
        OutputStream outputStream = null;
        try {
            file = File.createTempFile("File", ".txt", MainApplication.get().getCacheDir());
            outputStream = new FileOutputStream(file);
            outputStream.write("leavesC，这是一段测试文本".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

}
