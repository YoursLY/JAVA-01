package day07;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yun.lu
 * @date 2021/2/2 22:46
 * @desc
 */
public class Method13 {
    final static Lock lock = new ReentrantLock();
    static volatile int number = 0;

    public static void main(String[] args) {
        int num = 1;
        Thread thread = Thread.currentThread();
        new Thread(new Runnable() {
            @Override
            public void run() {
                addNum(num, thread);
            }
        }).start();
        if (number == 0) {
            System.out.println("要等了");
            LockSupport.park();
        }
        System.out.println("等待结束,结果为:" + number);
    }

    private static void addNum(int num, Thread thread) {
        lock.lock();
        try {
            number = ++num;
        } finally {
            lock.unlock();
        }
        LockSupport.unpark(thread);
    }
}
