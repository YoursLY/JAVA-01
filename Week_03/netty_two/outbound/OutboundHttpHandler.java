package netty_two.outbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import netty_two.filter.HttpResponseFilter;
import netty_two.filter.NettyHttpResponseFilter;
import netty_two.router.HttpEndpointRouter;
import netty_two.router.RandomHttpEndpointRouter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @description:
 * @author: yun.lu.o
 * @date 2021/1/22 15:53
 */
public class OutboundHttpHandler {
    private static Logger log = LoggerFactory.getLogger(OutboundHttpHandler.class);
    private static int cpuNum = Runtime.getRuntime().availableProcessors();
    private final static int THREAD_DEFAULT_ALIVE_TIME = 60;
    private final static int QUEUE_SIZE = 2048;
    /**
     * 线程池
     */
    private ThreadPoolExecutor poolExecutor;
    /**
     * 服务地址
     */
    private List<String> proxyServers;
    /**
     * 路由
     */
    private HttpEndpointRouter router = new RandomHttpEndpointRouter();

    /**
     * response过滤器
     *
     * @param proxyServers
     */
    private HttpResponseFilter httpResponseFilter = new NettyHttpResponseFilter();

    public OutboundHttpHandler(List<String> proxyServers) {
        /**
         * 拼接完整服务urlList
         */
        this.proxyServers = proxyServers.stream().map(this::formatUrl).collect(Collectors.toList());
        ;
        this.poolExecutor = new ThreadPoolExecutor(cpuNum, 2 * cpuNum + 1, THREAD_DEFAULT_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(QUEUE_SIZE), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 获取域名
     *
     * @param backend
     * @return
     */
    private String formatUrl(String backend) {
        return backend.endsWith("/") ? backend.substring(0, backend.length() - 1) : backend;
    }

    public void handler(final FullHttpRequest fullHttpRequest, final ChannelHandlerContext channelHandlerContext) {
        String backendUrl = router.route(this.proxyServers);
        final String url = backendUrl + fullHttpRequest.uri();
        poolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                handlerAndResponse(fullHttpRequest, channelHandlerContext, url);
            }
        });

    }

    private void handlerAndResponse(FullHttpRequest fullHttpRequest, ChannelHandlerContext channelHandlerContext, String url) {
        FullHttpResponse response = null;
        try {
            //用httpClient调用http://localhost:8801
            String value = getResultByHttpClient(url);
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());
        } catch (Exception e) {
            System.out.println("HttpHandler-handlerTest,异常,e:" + e.getMessage());
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT);
        } finally {
            if (fullHttpRequest != null) {
                if (!HttpUtil.isKeepAlive(fullHttpRequest)) {
                    channelHandlerContext.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                    channelHandlerContext.write(response);
                }
            }
            channelHandlerContext.flush();
        }
    }


    /**
     * 通过httpClient调用8801端口
     *
     * @return
     */
    private String getResultByHttpClient(String url) {
        log.info("HttpHandler-getResultByHttpClient,通过httpClient调用8801端口,开始");
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            log.info("HttpHandler-getResultByHttpClient,调8801响应状态为:" + response.getStatusLine());
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                log.info("HttpHandler-getResultByHttpClient，调8801响应长度为：" + responseEntity.getContentLength());
                return inputstreamToString(responseEntity.getContent(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 输入流转String
     *
     * @param content
     * @param charset
     * @return
     */
    private String inputstreamToString(InputStream content, String charset) {
        String str = null;
        if (content == null) {
            return str;
        }
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(content, charset));
            char buffer[] = new char[4096];
            int len;
            while ((len = br.read(buffer)) > 0)
                sb.append(new String(buffer, 0, len));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        str = sb.toString();
        return str;
    }


}
