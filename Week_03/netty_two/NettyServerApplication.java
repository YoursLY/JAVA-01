package netty_two;

import netty_two.inbound.InboundNettyHttpServer;

import java.util.ArrayList;

/**
 * @author yun.lu
 * @date 2021/1/26 23:54
 * @desc
 */
public class NettyServerApplication {
    public static void main(String[] args) {
        //本机端口
        int port = 8888;
        //服务地址
        ArrayList<String> proxyServers = new ArrayList<>();
        proxyServers.add("http://localhost:8801");
        proxyServers.add("http://localhost:8802");
        proxyServers.add("http://localhost:8803");
        try {
            new InboundNettyHttpServer(port,proxyServers).run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
