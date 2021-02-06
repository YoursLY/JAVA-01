package day07;

import java.util.concurrent.*;

/**
 * @author yun.lu
 * @date 2021/2/2 22:46
 * @desc
 */
public class Method02 {
    private static int cpuNum = Runtime.getRuntime().availableProcessors();
    private final static int THREAD_DEFAULT_ALIVE_TIME = 60;
    private final static int QUEUE_SIZE = 2048;
    public static ThreadFactory threadFactory=new ThreadFactory() {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("Method02-thread-"+thread.getId());
            thread.setDaemon(true);//守护线程
            return thread;
        }
    };
    /**
     * 线程池
     */
    private static ThreadPoolExecutor poolExecutor= new ThreadPoolExecutor(cpuNum, 2 *cpuNum + 1, THREAD_DEFAULT_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingDeque<>(QUEUE_SIZE), threadFactory,new ThreadPoolExecutor.CallerRunsPolicy());;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Integer num=1;
        Callable<Integer> callable= new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return addNum(num);
            }
        };
        Future<Integer> submit = poolExecutor.submit(callable);
        System.out.println("结果为:"+submit.get());

    }

    private static int addNum(int num) {
        return ++num;
    }


}
