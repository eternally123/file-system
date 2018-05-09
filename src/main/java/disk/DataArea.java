package disk;

import common.ConstVar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

/*
dataArea的Handler
 */
public class DataArea {
    char[] dataArea = new char[ConstVar.dataAreaSize];
    private int dataAreaStartPoint = ConstVar.dataAreaStart;//fat表起始地址(byte为单位)
    private String fileName = ConstVar.fileName;
    private RandomAccessFile randomAccessFile;


    //初始化randomAccessFile，在执行任何操作之前都会调用次函数
    private void initRandomAccessFile() {
        try {
            randomAccessFile = new RandomAccessFile("file_system", "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
    给出簇号和内容,写入簇中
    如果内容大于4kb return void
    如果number超出范围 return void
     */
    public void write(int number, byte[] contents) {
        if (number < 0 | number > ConstVar.fatSize) {//超出范围
            System.out.println("DataArea write: number");
            return;
        }

        if (contents.length > 4 * 1024) {
            System.out.println("DataArea write: contents");
            return;
        }
        initRandomAccessFile();
        try {
            randomAccessFile.seek(number * 4 * 1024 + ConstVar.dataAreaStart);
            randomAccessFile.write(contents);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    给出簇号，读出其中的内容，返回一个4kb的byte数组
    如果number超出范围 return void
     */
    public byte[] read(int number) {
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

        return b;
    }

}
