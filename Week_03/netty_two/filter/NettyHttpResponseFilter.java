package netty_two.filter;

import io.netty.handler.codec.http.FullHttpResponse;
import netty_two.filter.HttpResponseFilter;

/**
 * @author yun.lu
 * @date 2021/1/26 22:44
 * @desc
 */
public class NettyHttpResponseFilter implements HttpResponseFilter {
    public void filter(FullHttpResponse response) {
        response.headers().set("netty_response","testResponse");
    }
}
