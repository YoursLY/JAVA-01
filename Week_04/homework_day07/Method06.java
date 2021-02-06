package day07;

/**
 * @author yun.lu
 * @date 2021/2/2 22:46
 * @desc
 */
public class Method06 {
    private static volatile int result=0;

    public static void main(String[] args) {
        int num=1;
         new Thread(new Runnable() {
             @Override
             public void run() {
                 result=addNum(num);
             }
         }).run();
         while (true){
             if(result!=0){
                 break;
             }
         }
        System.out.println("结果为:"+result);
    }

    private static int addNum(int num) {
        return ++num;
    }


}
