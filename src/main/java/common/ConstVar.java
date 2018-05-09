package common;

public class ConstVar {
    /*
    文件名
     */
    public static final String fileName = "file_system";
    /*
    磁盘大小常量 fat16
     */
    public static final int sectors = 512;//扇区大小(byte)
    public static final int cluster = 4 * 512;//簇大小(byte)

    public static final int area0Size = 512;//保留区大小(byte)
    public static final int area1Size = 512;//保留区大小(byte)
    public static final int fatSize = 1024 * 256;//fat表表项大小(byte)   2^16*2
    public static final int fatStart = area0Size + area1Size;//fat表起始地址(byte)
    public static final int dataAreaSize = 1024 * 1024 * 256;//数据区大小(byte)  2^16*4kb
    public static final int dataAreaStart = fatStart + fatSize * 2;//数据区起始地址(byte) *2是两张fat表


    /*
    fat表表项值取值范围
     */
    public static final int endFAT = 1024 * 64 - 1;

    public static final int endCluster = 0xffff;    //表项 结束值
    public static final int emptyCluster = 0x0000;  //表项 空值



    
    /*
    service 层用到的常量
     */
    public static final int rootItemNumber=0;//根目录的起始簇号，系统启动时用rootItemNumber读取根目录

}
