package disk;

import common.ConstVar;

import java.io.RandomAccessFile;

public class DataArea {
    char[] dataArea = new char[ConstVar.dataAreaSize];

    public void write(int number, char[] contents) {
        //对一个簇写入内容
    }

    public char[] read(int number) {
        //读出一个簇的内容
        return new char[20];
    }


}
