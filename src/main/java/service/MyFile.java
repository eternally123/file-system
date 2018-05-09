package service;

import disk.FileHeader;

/**
 * @author: Li Xueyang
 * @time: 2018/5/8 17 01
 * @description:
 */
public class MyFile {
    private FileHeader fileHeader;
    private byte[] fileContent;

    public MyFile() {
        this.fileHeader = new FileHeader();
    }

    public FileHeader getFileHeader() {
        return fileHeader;
    }

    public void setFileHeader(FileHeader fileHeader) {
        this.fileHeader = fileHeader;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}
