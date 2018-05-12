package common;

public class ConstVar {
    /*
    vim 检查文件
    一行16byte
    fat表起始地址：第65行
    dataArea起始地址：第32833行
     */


    /*
    -----------------磁盘名称
     */
    public static final String diskName = "file_system";
    /*
    -----------------磁盘各个分区大小常量以及分区的起始地址 fat16
     */
    public static final int sectors = 512;//扇区大小(byte)
    public static final int cluster = 4 * 512;//簇大小(byte)
    public static final int fatItemLength = 2;//fat表一个表项的长度(byte)

    public static final int area0Size = 512;//保留区大小(byte)
    public static final int area1Size = 512;//保留区大小(byte)
    public static final int fatSize = 1024 * 256;//fat表表项大小(byte)   2^16*2
    public static final int dataAreaSize = 1024 * 1024 * 256;//数据区大小(byte)  2^16*4kb32832

    public static final int fatStartAddress = area0Size + area1Size;//fat表起始地址(byte)
    public static final int dataAreaStartAddress = fatStartAddress + fatSize * 2;//数据区起始地址(byte) *2是两张fat表


    /*
    ------------------fat表表项值取值范围
     */
    //public static final int endFAT = 1024 * 64 - 1;
    public static final int fatItemEndValue = 0xffff;    //表项 结束值
    public static final int fatItemEmptyValue = 0x0000;  //表项 空值

    /*
    -------------------FileHeader
     */
    public static final int fileHeaderLength = 64;//文件头长度



    /*
    --------------------service 层用到的常量
     */
    public static final int rootItemNumber = 0;//根目录的起始簇号，系统启动时用rootItemNumber读取根目录

}
