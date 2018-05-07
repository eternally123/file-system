package disk;

import common.ConstVar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

public class DataArea {
    char[] dataArea = new char[ConstVar.dataAreaSize];
    private int dataAreaStartPoint = ConstVar.dataAreaStart;//fat表起始地址(byte为单位)
    private String fileName = ConstVar.fileName;
    private RandomAccessFile randomAccessFile;

    /*
    初始化randomAccessFile，在执行任何操作之前都会调用次函数
     */
    public void initRandomAccessFile() {
        try {
            randomAccessFile = new RandomAccessFile("file_system", "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void write(int number, char[] contents) {
        //对一个簇写入内容
        if (number < 0 | number > ConstVar.fatSize) {//超出范围
            System.out.println("DataArea write: number");
            return;
        }
        String content = new String(contents);
        try {
            byte[] b = content.getBytes("ascii");
            if (b.length > 4 * 1024) {
                System.out.println("DataArea write: contents");
                return;
            }
            initRandomAccessFile();
            try {
                randomAccessFile.seek(number * 4 * 1024 + ConstVar.dataAreaStart);
                randomAccessFile.write(b);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public char[] read(int number) {
        //读出一个簇的内容
        if (number < 0 | number > ConstVar.fatSize) {//超出范围
            System.out.println("DataArea write: number");
            return null;
        }
        initRandomAccessFile();
        byte[] b = new byte[1024];
        try {
            randomAccessFile.seek(number * 4 * 1024 + ConstVar.dataAreaStart);
            randomAccessFile.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            String content = new String(b, "ascii");
            char[] result = content.toCharArray();
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
