package disk;

import java.util.ArrayList;
import java.util.List;

public class FolderContent {

    List<FileHeader> fileHeaderList = new ArrayList<>();//当前目录与父目录不会记录在fileHeaderList集合中

    public FolderContent() {

    }

    public FolderContent(byte[] folderContent) {
        if (folderContent.length % 64 != 0) {
            System.out.println("FolderContent constructor error");
            return;
        }
        int itemSize = folderContent.length / 64;
        if (itemSize == 0)
            return;
        for (int i = 0; i < itemSize; i++) {
            byte[] c = new byte[64];
            for (int j = 0; j < 64; j++)
                c[j] = folderContent[i * 64 + j];
            FileHeader fileHeader = new FileHeader(c);
            fileHeaderList.add(fileHeader);
        }
    }

    public byte[] getByte() {
        int number = fileHeaderList.size();
        if (number == 0)
            return null;
        byte[] b = new byte[number * 64];
        byte[] tmp;
        for (int i = 0; i < fileHeaderList.size(); i++) {
            tmp = fileHeaderList.get(i).getBytes();
            for (int j = 0; j < 64; j++)
                b[i * 64 + j] = tmp[j];
        }
        return b;
    }

    public List<FileHeader> getAllFileHeader(){
        return fileHeaderList;
    }

    public List<FileHeader> getAllFolderFileHeader() {
        List<FileHeader> folderFileHeaderList = new ArrayList<>();
        if (fileHeaderList.size() == 0)
            return folderFileHeaderList;
        for (int i = 0; i < fileHeaderList.size(); i++) {
            if (fileHeaderList.get(i).ifFolder())
                folderFileHeaderList.add(fileHeaderList.get(i));
        }
        return folderFileHeaderList;
    }

    public List<FileHeader> getAllFileFileHeader() {
        List<FileHeader> fileFileHeaderList = new ArrayList<>();
        if (fileHeaderList.size() == 0)
            return fileFileHeaderList;
        for (int i = 0; i < fileHeaderList.size(); i++) {
            if (fileHeaderList.get(i).ifFolder() == false)
                fileFileHeaderList.add(fileHeaderList.get(i));
        }
        return fileFileHeaderList;
    }

    public boolean addFileHeader(FileHeader fileHeader){
        fileHeaderList.add(fileHeader);
        return true;
    }

    public boolean removeFileHeader(int startCluster){
        for (int i=0;i<fileHeaderList.size();i++){
            if (fileHeaderList.get(i).getStartCluster()==startCluster)
                fileHeaderList.remove(i);
        }
        return true;
    }
}
