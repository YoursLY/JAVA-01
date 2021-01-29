package netty_two.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author yun.lu
 * @date 2021/1/26 22:40
 * @desc
 */
public class NettyHttpRequestFilter implements HttpRequestFilter {

    public void filter(FullHttpRequest fullRequest) {
        fullRequest.headers().set("netty_request","testDemo");
    }
}
