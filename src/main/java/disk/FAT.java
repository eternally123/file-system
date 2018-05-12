package disk;

import common.ConstVar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class FAT {
    private int fatStartPoint = ConstVar.fatStartAddress;//fat表起始地址(byte为单位)
    private String diskName = ConstVar.diskName;//磁盘名称
    private RandomAccessFile randomAccessFile;//文件指针


    //初始化randomAccessFile，在执行任何操作之前都会调用次函数
    private void initRandomAccessFile() {
        try {
            randomAccessFile = new RandomAccessFile(diskName, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
    给定某个表项号和值，将值写入表项号
    如果表项号超出范围 return void
     */
    public void write(int number, int value) {
        if (number < 0 | number >= ConstVar.fatSize) {
            System.out.println("FAT.write: number invalid");
            return;
        }
        initRandomAccessFile();
        byte[] val = new byte[2];//byte存放二进制value值，byte[0]存放二进制value高8位 byte[1]存放二进制value低8位
        val[1] = (byte) value;
        val[0] = (byte) (value >> 8);
        try {
            randomAccessFile.seek(number * ConstVar.fatItemLength + ConstVar.fatStartAddress);
            randomAccessFile.write(val);//写入value
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
    给定某个表项号，读取其内存储的值
    如果表项号超出范围 return void
     */
    public int read(int number) {
        //读取某个fat表项的表项值
        if (number < 0 | number >= ConstVar.fatSize) {
            System.out.println("FAT.write: number invalid");
            return -1;
        }
        initRandomAccessFile();
        try {
            randomAccessFile.seek(number * ConstVar.fatItemLength + ConstVar.fatStartAddress);
            byte[] b = new byte[2];
            randomAccessFile.read(b);
            int value = ((b[0] << 8) & 0xffff) | (b[1] & 0xffff);
            return value;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /*
    获取number个空的表项号
    如果磁盘剩余空间不足，返回null
     */
    public int[] getEmptyItem(int number) {
        int[] result = new int[number];//result数组记录获取的空的表项号
        int foundNumber = 0;//记录当前找到的空簇号的个数，没找到一个foundNumber+1
        int index = 0;//index是表项号的索引，从0开始遍历表项，如果index超出最大表项值，则抛出异常
        int value;//记录当前找到的空簇号，每找到一个后加入到result结果集中
        while (foundNumber < number) {
            value = read(index);
            if (value == 0) {
                result[foundNumber] = index;
                foundNumber = foundNumber + 1;
            }
            index = index + 1;
            if (index > ConstVar.fatSize) {//剩余空间不足
                System.out.println("FAT getEmptyItem: no enough empty item");
                return null;
            }
        }
        return result;
    }

    /*
    对多个表项写入值
    默认多个表项按照int[] 顺序构成一个文件的存储
    例如：
        number=[1,5,8,10] 则1,5,8,10表项对应的表项值为5,8,10,结束符号
     */
    public void writeMany(int[] number) {
        for (int i = 0; i < number.length; i++)
            if (number[i] < 0 | number[i] >= ConstVar.fatSize) {
                System.out.println("FAT.writeMany: number invalid");
                return;
            }
        initRandomAccessFile();
        int value;
        for (int i = 0; i < number.length; i++) {
            //if else语句是为了确定写入表项的值
            //如果是最后一个number，那么应该写入结束值，否则写入下一个number的值
            if (i == number.length - 1) {//如果写入的是最后一个表项，应该写入终结符
                value = ConstVar.fatItemEndValue;
            } else {//如果不是最后一个表项，应该写入下一个表项的表项号
                value = number[i + 1];
            }

            byte[] val = new byte[2];//byte存放二进制value值
            val[1] = (byte) value;
            val[0] = (byte) (value >> 8);
            try {
                randomAccessFile.seek(number[i] * 2 + ConstVar.fatStartAddress);
                randomAccessFile.write(val);//写入value
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
    }

    /*
    给起始簇号，读取文件所有簇号
    如果此簇号是0，则返回int[0] result
     */
    public int[] readFileNumbers(int startNumber) {
        if (read(startNumber) == 0)//当前起始簇号没有记录文件
            return new int[0];

        List<Integer> list = new ArrayList();
        list.add(startNumber);
        int number = read(startNumber);
        while (number != ConstVar.fatItemEmptyValue) {
            list.add(number);
            number = read(number);
        }

        int size = list.size();
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    /*
    格式化fat表
    将fat表全部写入0x0000  第0号表项写入结束值
     */
    public boolean formate() {
        //格式化磁盘--格式化fat表
        byte[] b = new byte[ConstVar.fatSize];
        initRandomAccessFile();
        try {
            randomAccessFile.seek(ConstVar.fatStartAddress);
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
        write(0, ConstVar.fatItemEndValue);
        return true;
    }
}
