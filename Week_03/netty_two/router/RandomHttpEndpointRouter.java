package netty_two.router;

import java.util.List;
import java.util.Random;

/**
 * @description:随机路由
 * @author: yun.lu.o
 * @date 2021/1/22 15:53
 */
public class RandomHttpEndpointRouter implements HttpEndpointRouter {
    @Override
    public String route(List<String> urls) {
        int size = urls.size();
        Random random = new Random(System.currentTimeMillis());
        return urls.get(random.nextInt(size));
    }
}
