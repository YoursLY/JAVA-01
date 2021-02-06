package day07;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author yun.lu
 * @date 2021/2/2 22:46
 * @desc
 */
public class Method04 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int num=1;
        Callable<Integer> task = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return addNum(num);
            }
        };
        FutureTask<Integer> integerFutureTask = new FutureTask<>(task);
        new Thread(integerFutureTask).start();
        System.out.println("结果为:"+integerFutureTask.get());
    }

    private static int addNum(int num) {
        return ++num;
    }


}
