package day07;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yun.lu
 * @date 2021/2/2 22:46
 * @desc
 */
public class Method12 {
    private static ReentrantLock reentrantLock=new ReentrantLock();
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int num=1;
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return addNum(num);
            }
        });
        new Thread(futureTask).start();
        System.out.println("结果为:"+ futureTask.get());
    }

    private static int addNum(int num) {
        reentrantLock.lock();
        try {
            System.out.println("等一会会");
            Thread.sleep(2000);
            return ++num;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
        return 0;
    }


}
