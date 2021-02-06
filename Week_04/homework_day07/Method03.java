package day07;

/**
 * @author yun.lu
 * @date 2021/2/2 22:46
 * @desc
 */
public class Method03 {
    public static void main(String[] args) throws InterruptedException {
        int num = 1;
        final Object o = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o) {
                    int result = addNum(num);
                    System.out.println("结果为:" + result);
                    o.notify();
                }

            }
        }).start();
        synchronized (o) {
            o.wait();
        }

    }

    private static int addNum(int num) {
        return ++num;
    }


}
