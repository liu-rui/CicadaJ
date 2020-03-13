package com.liurui.rabbitmq.log.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.google.common.base.Strings;
import okhttp3.*;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.MediaType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author liu-rui
 * @date 2019-07-23 19:50
 * @description 对logback扩展，支持钉钉异常通知
 */
public class DingDingAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private ExecutorService executorService;
    private OkHttpClient httpClient;
    private okhttp3.MediaType mediaType = okhttp3.MediaType.parse(MediaType.APPLICATION_JSON_UTF8_VALUE);

    @Override
    public void start() {
        super.start();
        executorService = new ThreadPoolExecutor(0, 50,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadPoolExecutor.DiscardPolicy());
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .build();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        executorService.submit(() -> {
            try {
                String url = eventObject.getLoggerContextVO().getPropertyMap().get("dingDingUrl");
                String secret = eventObject.getLoggerContextVO().getPropertyMap().get("dingDingSecret");

                if (!Strings.isNullOrEmpty(secret)) {
                    Long timestamp = System.currentTimeMillis();
                    String stringToSign = timestamp + "\n" + secret;
                    Mac mac = Mac.getInstance("HmacSHA256");
                    mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
                    byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
                    final String sign = URLEncoder.encode(Base64.getEncoder().encodeToString(signData));

                    url = String.format("%s&timestamp=%s&sign=%s", url, timestamp, sign);
                }
                RequestBody body = RequestBody.create(mediaType, toMessage(eventObject));
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                httpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                    }
                });
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }


    private static String toMessage(ILoggingEvent eventObject) {
        String title = String.format("%s服务发生异常", eventObject.getLoggerContextVO().getPropertyMap().get("APP_NAME"));
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("#### %s\\n", title));
        sb.append(String.format("##### %s\\n", format(eventObject.getFormattedMessage())));
        sb.append(eventObject.getLoggerContextVO().getPropertyMap().get("HOSTNAME") + "\\n\\n");

        if (Objects.nonNull(eventObject.getThrowableProxy()) &&
                ArrayUtils.isNotEmpty(eventObject.getThrowableProxy().getStackTraceElementProxyArray())) {
            sb.append(String.format("##### %s:%s\\n", eventObject.getThrowableProxy().getClassName(),
                    format(eventObject.getThrowableProxy().getMessage())));

            //堆栈信息显示最多2层
            for (int i = 0; i < 2 && i < eventObject.getThrowableProxy().getStackTraceElementProxyArray().length; i++) {
                StackTraceElementProxy elementProxy = eventObject.getThrowableProxy().getStackTraceElementProxyArray()[i];

                sb.append(String.format("%s.%s  line%s %s\\n\\n", (i + 1), elementProxy.getStackTraceElement().getFileName(),
                        elementProxy.getStackTraceElement().getLineNumber(),
                        elementProxy.getStackTraceElement().getMethodName()));
            }
        }
        sb.append(String.format("![error](%s)", eventObject.getLoggerContextVO().getPropertyMap().get("dingDingLogo")));

        return String.format("{\"msgtype\": \"markdown\",\"markdown\": { \"title\":\"%s\",\"text\": \"%s\"}, \"at\": {\"isAtAll\": true}}",
                title,
                sb);
    }

    private static String format(String message) {
        return Strings.isNullOrEmpty(message) ? "" : message.replace("\"", "\\\"");
    }
}
