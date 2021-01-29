package netty_two.inbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.List;


/**
 * @description:
 * @author: yun.lu.o
 * @date 2021/1/22 17:53
 */
public class InboundHttpInitializer extends ChannelInitializer<SocketChannel> {
    private List<String> proxyServers;

    public InboundHttpInitializer(List<String> proxyServers) {
        this.proxyServers = proxyServers;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(1024 * 1024));
        p.addLast(new InboundHttpHandler(this.proxyServers));
    }
}
