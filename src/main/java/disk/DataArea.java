package disk;

import common.ConstVar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/*
dataArea的Handler
 */
public class DataArea {
    private int dataAreaStart = ConstVar.dataAreaStartAddress;//fat表起始地址(byte为单位)
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
    给出簇号和内容,写入簇中
    如果内容大于ConstVar.cluster return void
    如果number超出范围 return void
     */
    public void write(int number, byte[] contents) {
        if (number < 0 | number > ConstVar.fatSize) {//簇号超出磁盘寻址范围
            System.out.println("DataArea write: number");
            return;
        }

        if (contents.length > ConstVar.cluster) {//contents内容超过一个簇大小
            System.out.println("DataArea write: contents");
            return;
        }
        initRandomAccessFile();
        try {
            randomAccessFile.seek(number * ConstVar.cluster + ConstVar.dataAreaStartAddress);
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

    public void writeMany(int[] number, byte[] contents) {
        if (contents.length > number.length * ConstVar.cluster | contents.length < (number.length - 1) * ConstVar.cluster) {
            System.out.println("DataArea writeMany: error");
            return;//contents大小与簇数量不符合
        }
        initRandomAccessFile();
        try {
            for (int i = 0; i < number.length; i++) {
                randomAccessFile.seek(number[i] * ConstVar.cluster + ConstVar.dataAreaStartAddress);
                if (i != number.length - 1)
                    randomAccessFile.write(contents, i * ConstVar.cluster, ConstVar.cluster);//每次写入一个簇
                else
                    randomAccessFile.write(contents, i * ConstVar.cluster, contents.length - i * ConstVar.cluster);//最后一次计算下剩余内容大小，将剩余的写入
            }
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
    给出簇号，读出其中的内容，返回一个 ConstVar.cluster 的byte数组
    如果number超出范围 return void
     */
    public byte[] read(int number) {
        //读出一个簇的内容
        if (number < 0 | number > ConstVar.fatSize) {//簇号超出磁盘索引范围
            System.out.println("DataArea write: number");
            return null;
        }
        initRandomAccessFile();
        byte[] b = new byte[ConstVar.cluster];
        try {
            randomAccessFile.seek(number * ConstVar.cluster + ConstVar.dataAreaStartAddress);
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

    public byte[] readMany(int[] number) {
        //读出多个簇内容
        initRandomAccessFile();
        byte[] result = new byte[number.length * ConstVar.cluster];
        try {
            for (int i = 0; i < number.length; i++) {
                randomAccessFile.seek(number[i] * ConstVar.cluster + ConstVar.dataAreaStartAddress);
                randomAccessFile.read(result, i * ConstVar.cluster, ConstVar.cluster);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /*
    格式化数据区
    将根目录写入数据区
     */
    public boolean formate() {
        FileHeader fileHeader = new FileHeader();
        fileHeader.setFolder(true);
        fileHeader.setFileLength(ConstVar.fileHeaderLength);
        fileHeader.setFileName("Root");
        fileHeader.setStartCluster(0);
        byte[] b = fileHeader.getBytes();

        initRandomAccessFile();
        try {
            randomAccessFile.seek(dataAreaStart);
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

        return true;
    }

}
