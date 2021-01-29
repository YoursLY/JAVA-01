package netty_two.inbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import netty_two.filter.HttpRequestFilter;
import netty_two.filter.NettyHttpRequestFilter;
import netty_two.outbound.OutboundHttpHandler;

import java.util.List;


/**
 * @description:
 * @author: yun.lu.o
 * @date 2021/1/22 15:53
 */
public class InboundHttpHandler extends ChannelInboundHandlerAdapter {
    private final List<String> proxyServer;
    private OutboundHttpHandler outboundHttpHandler;
    private HttpRequestFilter httpRequestFilter = new NettyHttpRequestFilter();

    public InboundHttpHandler(List<String> proxyServer) {
        this.proxyServer = proxyServer;
        this.outboundHttpHandler = new OutboundHttpHandler(this.proxyServer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
        try {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            //请求前置过滤器,在请求到业务服务之前的处理
            httpRequestFilter.filter(fullHttpRequest);
            //业务处理,并返回
            outboundHttpHandler.handler(fullHttpRequest, channelHandlerContext);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        channelHandlerContext.close();
    }


}
