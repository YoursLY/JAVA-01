package day07;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yun.lu
 * @date 2021/2/2 22:46
 * @desc
 */
public class Method07 {

    public static void main(String[] args) {
        int num = 1;
        Operate operate = new Operate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                operate.setValue(num);
            }
        }).start();
        while (true){
            if(operate.getValue()!=0){
                System.out.println("结果:"+operate.getValue());
                break;
            }
        }
    }


   static class  Operate {
        private volatile int number;
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        public Operate() {
        }

        public int getValue() {
            int result = 0;
            lock.readLock().lock();
            try {
                System.out.println("Operate-getValue,threadId:"+Thread.currentThread().getId());
                result = number;
            } finally {
                lock.readLock().unlock();
            }
            return result;
        }

        public void setValue(int value) {
            lock.writeLock().lock();
            try {
                System.out.println("Operate-setValue,threadId:"+Thread.currentThread().getId());
                number = addNum(value);
            } finally {
                lock.writeLock().unlock();
            }
        }

        private int addNum(int num) {
            return ++num;
        }

    }


}
