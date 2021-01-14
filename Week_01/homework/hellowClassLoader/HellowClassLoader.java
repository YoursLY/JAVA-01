package day01.Hello;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author yun.lu
 * @date 2021/1/10 13:25
 * @desc
 */
public class HellowClassLoader extends ClassLoader {
    public static void main(String[] args) {
        try {
            new HellowClassLoader().findClass("Hello.xlass").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes =null;
        try {
            bytes = this.getByteByPath(name);
        } catch (IOException e) {
           throw new ClassNotFoundException();
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] getByteByPath(String path) throws IOException {
        FileInputStream inputStream = new FileInputStream(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            int nextValue;
            while ((nextValue = inputStream.read()) != -1) {
                bos.write(nextValue);
            }
        } catch (IOException e) {
           throw new IOException();
        } finally {
            inputStream.close();
            bos.close();
        }
        //解密
        return decode(bos.toByteArray());
    }


    private byte[] decode(byte[] bytes) {
        for (int i=0;i<bytes.length;i++) {
            bytes[i]=(byte)(255-bytes[i]);
        }
        return bytes;
    }


}
