package day07;

import java.util.concurrent.Semaphore;

/**
 * @author yun.lu
 * @date 2021/2/2 22:46
 * @desc
 */
public class Method05 {
    final static Semaphore semaphore = new Semaphore(1);
   static volatile int result = 0;

    public static void main(String[] args) throws InterruptedException {
        int num = 1;

       new Thread(new Runnable() {
           @Override
           public void run() {
               addNum(num);
           }
       }).start();
       Thread.sleep(1000);
       while (true) {
           try {
               if (semaphore.tryAcquire()) {
                   System.out.println("结果为:" + result);
                   break;
               }
           } finally {
               semaphore.release();
           }
       }

    }

    private static void addNum(int num) {
        try {
            semaphore.acquireUninterruptibly();
            result= ++num;
        } finally {
            semaphore.release();
        }
    }


}
