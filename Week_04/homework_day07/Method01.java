package day07;

/**
 * @author yun.lu
 * @date 2021/2/2 22:46
 * @desc
 */
public class Method01 {
    public static void main(String[] args) {
        int num=1;
        int result=addNum(num);
        System.out.println("结果为:"+result);
    }

    private static int addNum(int num) {
        return ++num;
    }


}
