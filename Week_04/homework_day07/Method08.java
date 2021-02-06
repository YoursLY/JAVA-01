package day07;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yun.lu
 * @date 2021/2/2 22:46
 * @desc
 */
public class Method08 {
    final static Lock lock=new ReentrantLock();
    final static Condition lockCondition=lock.newCondition();


    public static void main(String[] args) throws InterruptedException {
        int num=1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result=0;
                     result = addNum(num);
                    System.out.println("结果为:"+result);


            }
        }).start();
        lockCondition.await();


    }

    private static int addNum(int num)
    {
        lock.lock();
        try {
            ++num;
        } finally {
            lock.unlock();
        }

        return num;
    }


}
