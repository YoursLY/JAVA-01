package day07;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yun.lu
 * @date 2021/2/2 22:46
 * @desc
 */
public class Method11 {
    private static volatile int number = 0;
    private static ReentrantLock lock=new ReentrantLock();

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        int num = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                addNum(num, cyclicBarrier);
            }
        }).start();
        cyclicBarrier.await();
        System.out.println("结果:" + number);
    }

    private static void addNum(int num, CyclicBarrier cyclicBarrier) {
        try {
            System.out.println("执行中,等一会儿");
            Thread.sleep(2000);
            lock.lock();
            number = ++num;
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }


}
