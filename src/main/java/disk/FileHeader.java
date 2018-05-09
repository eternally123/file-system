package disk;

import java.io.UnsupportedEncodingException;

public class FileHeader {
    byte[] fileName = new byte[48];//文件名
    byte[] attributes = new byte[1];//属性
    byte[] seconds = new byte[1];//秒进位
    byte[] createTime = new byte[2];//创建时间
    byte[] createDate = new byte[2];//创建日期
    byte[] updateTime = new byte[2];//update时间
    byte[] updateDate = new byte[2];//update日期
    byte[] startCluster = new byte[2];//起始簇号
    byte[] fileLength = new byte[4];//文件长度---占用簇大小*4

    public FileHeader() {
    }

    public FileHeader(byte[] b) {
        if (b.length != 64)
            System.out.println("construct error FileHeader: the length of the parameter isn't 64");
        for (int i = 0; i < 48; i++)
            fileName[i] = b[i];//文件名
        attributes[0] = b[48];
        seconds[0] = b[49];
        createTime[0] = b[50];
        createTime[1] = b[51];
        createDate[0] = b[52];
        createDate[1] = b[53];
        updateTime[0] = b[54];
        updateTime[1] = b[55];
        updateDate[0] = b[56];
        updateDate[1] = b[57];
        startCluster[0] = b[58];
        startCluster[1] = b[59];
        for (int i = 0; i < 4; i++)
            fileLength[i] = b[60 + i];
    }

//    //从数组的第start元素开始到end-1元素结束，利用这些byte构建FileHeader对象
//    public FileHeader(byte[] b, int start, int end) {
//        if (b.length < end | (end - start) != 64) {
//            System.out.println("FileHeader constructor error: byte[] b,int start,int end");
//            return;
//        }
//        for (int i = 0; i < 48; i++)
//            fileName[i] = b[i+start];//文件名
//        attributes[0] = b[48+start];
//        seconds[0] = b[49+start];
//        createTime[0] = b[50+start];
//        createTime[1] = b[51+start];
//        createDate[0] = b[52+start];
//        createDate[1] = b[53+start];
//        updateTime[0] = b[54+start];
//        updateTime[1] = b[55+start];
//        updateDate[0] = b[56+start];
//        updateDate[1] = b[57+start];
//        startCluster[0] = b[58+start];
//        startCluster[1] = b[59+start];
//        for (int i = 0; i < 4; i++)
//            fileLength[i] = b[60 + i+start];
//    }

    public byte[] getBytes() {
        byte[] b = new byte[64];
        for (int i = 0; i < 48; i++)
            b[i] = fileName[i];//文件名
        b[48] = attributes[0];
        b[49] = seconds[0];
        b[50] = createTime[0];
        b[51] = createTime[1];
        b[52] = createDate[0];
        b[53] = createDate[1];
        b[54] = updateTime[0];
        b[55] = updateTime[1];
        b[56] = updateDate[0];
        b[57] = updateDate[1];
        b[58] = startCluster[0];
        b[59] = startCluster[1];
        for (int i = 0; i < 4; i++)
            b[60 + i] = fileLength[i];
        return b;
    }

    //获取文件名
    public String getFileName() {
        String fileNameV = new String(fileName);
        return fileNameV;
    }

    //设置文件名
    public void setFileName(String fileNameV) {
        try {
            byte[] b = fileNameV.getBytes("ascii");
            if (b.length > 48) {
                System.out.println("FileHeader setFileName:invalid name");
                return;
            }
            this.fileName = b;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //设置为文件夹
    public boolean setFolder(boolean b) {
        if (b == true)
            attributes[0] = (byte) (attributes[0] | 0x01);//最后一位置1
        else
            attributes[0] = (byte) (attributes[0] & 0xfe);//最后一位清0；
        return true;
    }

    //获取是否是文件夹
    public boolean ifFolder() {
        if ((attributes[0] & 0x01) == 1)
            return true;
        return false;
    }

    //获取起始簇号
    public int getStartCluster() {
        return (int) startCluster[0] * 256 + (int) startCluster[1];
    }

    //设置起始簇号
    public void setStartCluster() {
        FAT fat = new FAT();
        int[] number = fat.getEmptyItem(1);
        startCluster[0] = (byte) ((number[0] >> 8) & 0xff);
        startCluster[1] = (byte) (number[0] & 0xff);
    }

    public int getFileLength() {
        return (int) (((fileLength[0] >> 24) & 0xff) * 256 * 256 * 256 + ((fileLength[0] >> 16) & 0xff) * 256 * 256 + ((fileLength[0] >> 8) & 0xff) * 2568 + fileLength[0] & 0xff);
    }

    public void setFileLength(int length) {
        fileLength[0] = (byte) ((length >> 24) & 0xff);
        fileLength[1] = (byte) ((length >> 16) & 0xff);
        fileLength[2] = (byte) ((length >> 8) & 0xff);
        fileLength[3] = (byte) (length & 0xff);
    }

//待补充
//    public void setCreateTimeAndDate(Date date) {
//
//    }
//
//    public Date getCreateTimeAndDate() {
//        return new Date();
//    }
//
//    public void setUpdateTimeAndDate(Date date) {
//
//    }
//
//    public Date getUpdateTimeAndDate() {
//        return new Date();
//    }
//
//    private byte[] getSeconds() {
//        return seconds;
//    }
//
//    private void setSeconds(byte[] seconds) {
//        this.seconds = seconds;
//    }
//
//    private byte[] getCreateTime() {
//        return createTime;
//    }
//
//    private void setCreateTime(byte[] createTime) {
//        this.createTime = createTime;
//    }
//
//    private byte[] getCreateDate() {
//        return createDate;
//    }
//
//    private void setCreateDate(byte[] createDate) {
//        this.createDate = createDate;
//    }
//
//    private byte[] getUpdateTime() {
//        return updateTime;
//    }
//
//    private void setUpdateTime(byte[] updateTime) {
//        this.updateTime = updateTime;
//    }
//
//    private byte[] getUpdateDate() {
//        return updateDate;
//    }
//
//    private void setUpdateDate(byte[] updateDate) {
//        this.updateDate = updateDate;
//    }


    @Override
    public String toString() {
        return "FileHeader{" +
                "fileName=" + getFileName() +
                ", ifFolder=" + ifFolder() +
                ", startCluster=" + getStartCluster() +
                ", fileLength=" + getFileLength() +
                '}';
    }
}
