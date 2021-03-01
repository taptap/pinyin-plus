package com.taptap.pinyin;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * @author kl (http://kailing.pub)
 * @since 2021/2/26
 */
class PinYinWebApiTest {

    public static void main(String[] args) {
        Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new PinYinPlusHandler())
                .build()
                .start();
    }


    /**
     * 测试脚本：wrk -t16 -c100 -d15s --latency http://localhost:8080/%E7%8E%87%E5%9C%9F%E4%B9%8B%E6%BB%A8
     */
    static class PinYinPlusHandler implements HttpHandler {
        @Override
        public void handleRequest(HttpServerExchange exchange){
            String text = exchange.getRequestPath().replace("/","");
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            String result = PinyinPlus.to(text);
            exchange.getResponseSender().send(result);
        }
    }
}
