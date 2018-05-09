package disk;

import common.ConstVar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

//补充函数接口
public class FAT {
    //    char[] fat1 = new char[ConstVar.fatSize];
//    char[] fat2 = new char[ConstVar.fatSize];
    private int fatStartPoint = ConstVar.fatStart;//fat表起始地址(byte为单位)
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
    给定某个表项号和值，将值写入表项号
    如果表项号超出范围 return void
     */
    public void write(int number, int value) {
        if (number < 0 | number >= ConstVar.fatSize) {
            System.out.println("FAT.write: number invalid");
            return;
        }
        initRandomAccessFile();
        byte[] val = new byte[2];//byte存放二进制value值
        val[1] = (byte) value;
        val[0] = (byte) (value >> 8);
        try {
            randomAccessFile.seek(number * 2 + ConstVar.fatStart);
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
            randomAccessFile.seek(number * 2 + ConstVar.fatStart);
            byte[] b = new byte[2];
            randomAccessFile.read(b);
            int value = (int) ((b[0] << 8) & 0xffff) | (b[1] & 0xffff);
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
     */
    public int[] getEmptyItem(int number) {
        int[] result = new int[number];//result数组记录获取的空的表项号
        int find = 0;//find记录找到的第n个值，没找到一个值，find=find+1
        int index = 0;//index是表项号的索引，从0开始遍历表项，如果index超出最大表项值，则抛出异常
        int value;
        while (find < number) {
            value = read(index);
            if (value == 0) {
                result[find] = index;
                find = find + 1;
            }
            index = index + 1;
            if (index > ConstVar.endCluster) {//剩余空间不足
                System.out.println("FAT getEmptyItem: no enough empty item");
            }
        }
        return result;
    }

    /*
    对多个表项写入值
    默认多个表项按照int[] 顺序构成一个文件的存储
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
            if (i == number.length - 1) {
                value = ConstVar.endFAT;
            } else {
                value = number[i + 1];
            }

            byte[] val = new byte[2];//byte存放二进制value值
            val[1] = (byte) value;
            val[0] = (byte) (value >> 8);
            try {
                randomAccessFile.seek(number[i] * 2 + ConstVar.fatStart);
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
     */
    public int[] readFileNumbers(int startNumber) {
        if (read(startNumber) == 0)//当前起始簇号没有记录文件
            return null;

        List<Integer> list = new ArrayList();
        list.add(startNumber);
        int number = read(startNumber);
        while (number != ConstVar.endFAT) {
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
    将fat表全部写入0x00  第0号表项写入结束值
     */
    public void formate() {
        //格式化磁盘--格式化fat表
        byte[] b = new byte[ConstVar.fatSize];
        initRandomAccessFile();
        try {
            randomAccessFile.seek(ConstVar.fatStart);
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
        write(0, ConstVar.endCluster);
    }
}
