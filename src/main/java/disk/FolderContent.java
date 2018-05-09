package disk;

import java.util.ArrayList;
import java.util.List;

public class FolderContent {

    List<FileHeader> fileHeaderList = new ArrayList<>();


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

//    public List<Integer> getAllCluster() {
//        return null;
//    }
//
//    public List<Integer> getAll
//
//
//
//
//
//
//
// FolderCluster() {
//        return null;
//    }
//
//    public List<Integer> getAllFileCluster() {
//        return null;
//    }
//
//    public int getCurrentCluster() {
//        return 0;
//    }
//
//    public int getParentCluster() {
//        return 0;
//    }
}
