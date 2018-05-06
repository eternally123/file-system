package disk;

import java.util.Arrays;

/*
重写get、set方法
 */
public class FileHeader {
    char[] fileName=new char[48];//文件名
    char[] attributes=new char[1];//属性
    char[] seconds=new char[1];//秒进位
    char[] createTime=new char[2];//创建时间
    char[] createDate=new char[2];//创建日期
    char[] updateTime=new char[2];//update时间
    char[] updateDate=new char[2];//update日期
    char[] startCluster=new char[2];//起始簇号
    char[] fileLength=new char[4];//文件长度---占用簇大小*4

    public FileHeader() {
    }

    public FileHeader(char[] fileName, char[] attributes, char[] seconds, char[] createTime, char[] createDate, char[] updateTime, char[] updateDate, char[] startCluster, char[] fileLength) {
        this.fileName = fileName;
        this.attributes = attributes;
        this.seconds = seconds;
        this.createTime = createTime;
        this.createDate = createDate;
        this.updateTime = updateTime;
        this.updateDate = updateDate;
        this.startCluster = startCluster;
        this.fileLength = fileLength;
    }

    public char[] getFileName() {
        return fileName;
    }

    public void setFileName(char[] fileName) {
        this.fileName = fileName;
    }

    public char[] getAttributes() {
        return attributes;
    }

    public void setAttributes(char[] attributes) {
        this.attributes = attributes;
    }

    public char[] getSeconds() {
        return seconds;
    }

    public void setSeconds(char[] seconds) {
        this.seconds = seconds;
    }

    public char[] getCreateTime() {
        return createTime;
    }

    public void setCreateTime(char[] createTime) {
        this.createTime = createTime;
    }

    public char[] getCreateDate() {
        return createDate;
    }

    public void setCreateDate(char[] createDate) {
        this.createDate = createDate;
    }

    public char[] getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(char[] updateTime) {
        this.updateTime = updateTime;
    }

    public char[] getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(char[] updateDate) {
        this.updateDate = updateDate;
    }

    public char[] getStartCluster() {
        return startCluster;
    }

    public void setStartCluster(char[] startCluster) {
        this.startCluster = startCluster;
    }

    public char[] getFileLength() {
        return fileLength;
    }

    public void setFileLength(char[] fileLength) {
        this.fileLength = fileLength;
    }

    @Override
    public String toString() {
        return "FileHeader{" +
                "fileName=" + Arrays.toString(fileName) +
                ", attributes=" + Arrays.toString(attributes) +
                ", seconds=" + Arrays.toString(seconds) +
                ", createTime=" + Arrays.toString(createTime) +
                ", createDate=" + Arrays.toString(createDate) +
                ", updateTime=" + Arrays.toString(updateTime) +
                ", updateDate=" + Arrays.toString(updateDate) +
                ", startCluster=" + Arrays.toString(startCluster) +
                ", fileLength=" + Arrays.toString(fileLength) +
                '}';
    }
}
