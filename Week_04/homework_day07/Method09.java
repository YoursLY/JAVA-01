package day07;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * @author yun.lu
 * @date 2021/2/2 22:46
 * @desc
 */
public class Method09 {
    private static volatile int number = 0;
    private static Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        int num = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                addNum(num, countDownLatch);
            }
        }).start();
        countDownLatch.await();
        System.out.println("结果:" + number);
    }

    private static void addNum(int num, CountDownLatch countDownLatch) {
        try {
            System.out.println("执行中,等一会儿");
            Thread.sleep(2000);
            number = ++num;
            semaphore.acquireUninterruptibly();
            countDownLatch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }

    }


}
