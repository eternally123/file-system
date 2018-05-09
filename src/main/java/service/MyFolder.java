package service;

import disk.FileHeader;
import disk.FolderContent;

/**
 * @author: Li Xueyang
 * @time: 2018/5/8 17 02
 * @description:
 */
public class MyFolder {
    private FileHeader folderHeader=new FileHeader();

    public MyFolder() {
        this.folderHeader = new FileHeader();
        this.folderContent = new FolderContent();
    }

    public FileHeader getFolderHeader() {
        return folderHeader;
    }

    public void setFolderHeader(FileHeader folderHeader) {
        this.folderHeader = folderHeader;
    }

    public FolderContent getFolderContent() {
        return folderContent;
    }

    public void setFolderContent(FolderContent folderContent) {
        this.folderContent = folderContent;
    }

    private FolderContent folderContent=new FolderContent();
}
