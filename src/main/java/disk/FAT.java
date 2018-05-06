package disk;

import common.ConstVar;

//补充函数接口
public class FAT {
    char[] fat1 = new char[ConstVar.fat1Size];
    char[] fat2 = new char[ConstVar.fat1Size];

    public void write(int number, int value) {
        //对某个fat表项赋值
    }

    public void writeMany(int[] number) {
        //对多个fat表项赋值
    }

    public int read(int number) {
        //读取某个fat表项的表项值
        return 0;
    }

    public int[] readFileNumbers(int startNumber) {
        //给起始簇号，读取文件所有簇号
        int size = 20;
        return new int[size];
    }

    public int[] getEmpty(int number) {
        //获取多个空fat表项
        return new int[number];
    }

    public void formate(){
        //格式化磁盘--格式化fat表
    }
}
