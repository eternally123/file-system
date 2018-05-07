package disk;

import common.ConstVar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

//补充函数接口
public class FAT {
    char[] fat1 = new char[ConstVar.fatSize];
    char[] fat2 = new char[ConstVar.fatSize];
    private int fatStartPoint = ConstVar.fatStart;//fat表起始地址(byte为单位)
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

    /*
    给定某个表项号和值，将值写入表项号
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
            randomAccessFile.seek(number * 2+ConstVar.fatStart);
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

    public void writeMany(int[] number) {
        //对多个fat表项赋值
        for (int i = 0; i < number.length; i++)
            if (number[i] < 0 | number[i] >= ConstVar.fatSize) {
                System.out.println("FAT.writeMany: number invalid");
                return;
            }
        initRandomAccessFile();
        int value;
        for (int i = 0; i < number.length; i++) {
            if (i == number.length - 1) {
                //终结符号
                value = ConstVar.endFAT;
            } else {
                value = number[i + 1];
            }

            byte[] val = new byte[2];//byte存放二进制value值
            val[1] = (byte) value;
            val[0] = (byte) (value >> 8);
            try {
                randomAccessFile.seek(number[i] * 2+ConstVar.fatStart);
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

    public int read(int number) {
        //读取某个fat表项的表项值
        if (number < 0 | number >= ConstVar.fatSize) {
            System.out.println("FAT.write: number invalid");
            return -1;
        }
        initRandomAccessFile();
        try {
            randomAccessFile.seek(number * 2+ConstVar.fatStart);
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

    public int[] readFileNumbers(int startNumber) {
        //给起始簇号，读取文件所有簇号
        if (read(startNumber) == 0)//当前起始簇号没有记录文件
            return null;

        List<Integer> list = new ArrayList();
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

    public int[] getEmpty(int number) {
        //获取多个空fat表项
        int[] result = new int[number];
        int find = 0;
        int index = 0;
        int value;
        while (find < number) {
            value = read(index);
            if (value == 0) {
                result[find] = index;
                find = find + 1;
            }
            index = index + 1;
        }
        return result;
    }

    public void formate() {
        byte[] b=new byte[ConstVar.fatSize];
        initRandomAccessFile();
        try {
            randomAccessFile.seek(ConstVar.fatStart);
            randomAccessFile.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //格式化磁盘--格式化fat表
    }
}
