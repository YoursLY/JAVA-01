package day07;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yun.lu
 * @date 2021/2/2 22:46
 * @desc
 */
public class Method10 {
    private static ReentrantLock lock=new ReentrantLock();
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int num=1;
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
            return addNum(num);
        });

        System.out.println("结果为:"+integerCompletableFuture.get());
    }

    private static int addNum(int num)  {
        System.out.println("等一会会儿");
        lock.lock();
        try {
            Thread.sleep(2000);
            return ++num;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return 0;
    }


}
