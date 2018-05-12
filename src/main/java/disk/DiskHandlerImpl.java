package disk;

import common.ConstVar;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;


/*
设计存在问题！！！会对用户看到的结果造成一定影响
问题在于文件长度与fileHeader之间的关联关系


----------------------------------------------讨论：fileHeader不用上层设置起始簇--------------------------------------------
 */
public class DiskHandlerImpl implements DiskHandler {
    private RandomAccessFile randomAccessFile;

    private void initRandomAccessFile() {
        try {
            randomAccessFile = new RandomAccessFile(ConstVar.diskName, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    //格式化磁盘
    //格式化fat表
    //格式化dataArea
    public void formate() {
        //格式化FAT表
        FAT fat = new FAT();
        fat.formate();
        DataArea dataArea = new DataArea();
        dataArea.formate();
    }

    /*
    1.将此文件写入磁盘(两部分 fileHeader和contents)   2.通过ParentCluster找到父文件夹，修改父文件夹的contents，然后将父文件夹写回
     */
    @Override
    public boolean createFile(byte[] fileHeader, byte[] contents, int parentCluster) {
        //1.将文件写入磁盘
        boolean result = createFileWithoutUpdateParentFolder(fileHeader, contents);//将文件写入磁盘
        if (result == false) {
            System.out.println("DiskHandlerImpl writeFileWithoutUpdateParentFolder: something wrong...");
            return false;
        }
        //2.修改父目录文件夹
        List<byte[]> parentFolder = readFile(parentCluster);//读出父目录内容
        FolderContent parentFolderContent = new FolderContent(parentFolder.get(1));//父目录中的folderContent存入parentFolderContent
        FileHeader newFileHeader = new FileHeader(fileHeader);
        parentFolderContent.addFileHeader(newFileHeader);
        byte[] updatedFolderContent = parentFolderContent.getByte();//将父目录的folderContent修改

        result = writeFileWithoutUpdateParentFolder(parentCluster, parentFolder.get(0), updatedFolderContent);//将父文件夹修改后的内容写回
        if (result == false) {
            System.out.println("DiskHandlerImpl writeFileWithoutUpdateParentFolder: something wrong...");
            return false;
        }
        return true;
    }

    /*
    根据文件长度获取所需簇的个数n，并获取n个空fat表项。
    然后为文件头设置属性：起始簇号，文件长度。
    最后分别在fat表和数据区写入响应内容
     */
    private boolean createFileWithoutUpdateParentFolder(byte[] fileHeader, byte[] contents) {
        FAT fat = new FAT();
        int clusterNumber = (contents.length + 64) / ConstVar.cluster + 1;
        int[] emptyClusterNumber = fat.getEmptyItem(clusterNumber);//获取所需要的簇号存放在emptyClusterNumber数组中

        FileHeader newFileHeader = new FileHeader(fileHeader);//创建fileHeader对象方便为fileHeader部分属性赋值
        newFileHeader.setStartCluster(emptyClusterNumber[0]);
        fileHeader = newFileHeader.getBytes();//设置好文件头信息的fileHeader

        //------写fat表
        fat.writeMany(emptyClusterNumber);
        //------写dataArea
        DataArea dataArea = new DataArea();
        byte[] fileHeaderAndContent = new byte[64 + contents.length];//将所有内容封入fileHeaderAndContent
        for (int i = 0; i < 64; i++)
            fileHeaderAndContent[i] = fileHeader[i];
        for (int i = 64; i < fileHeaderAndContent.length; i++) {
            fileHeaderAndContent[i] = contents[i - 64];
        }
        dataArea.writeMany(emptyClusterNumber, fileHeaderAndContent);
        return true;
    }

    /*
    更新一个文件的(内容和头文件)，会影响父文件夹的内容，要把父文件夹中记录此文件的一条记录连带更新
     */
    @Override
    public boolean writeFile(int startCluster, byte[] fileHeader, byte[] contents, int parentCluster) {
        boolean result = writeFileWithoutUpdateParentFolder(startCluster, fileHeader, contents);
        if (result == false) {
            System.out.println("DiskHandlerImpl updateFile: something wrong...");
            return false;
        }

        List<byte[]> parentFolder = readFile(parentCluster);//读出父目录fileHeader和content，更改父目录内容
        FolderContent parentFolderContent = new FolderContent(parentFolder.get(1));
        for (int i = 0; i < parentFolderContent.fileHeaderList.size(); i++) {
            if (parentFolderContent.fileHeaderList.get(i).getStartCluster() == startCluster)
                parentFolderContent.fileHeaderList.remove(i);
        }
        parentFolderContent.addFileHeader(new FileHeader(fileHeader));//加入修改后的fileHeader

        byte[] byteFolderContent = parentFolderContent.getByte();//将父目录的folderContent修改

        result = writeFileWithoutUpdateParentFolder(parentCluster, parentFolder.get(0), byteFolderContent);//将父文件夹修改后的内容写回
        if (result == false) {
            System.out.println("DiskHandlerImpl updateFile: something wrong...");
            return false;
        }

        return false;
    }

    /*
    更新一个文件的内容，由于不会影响此文件的文件头，故不会对父文件夹造成影响。
     */
    private boolean writeFileWithoutUpdateParentFolder(int startCluster, byte[] fileHeader, byte[] contents) {
        //clusterNumber记录update时文件所需簇个数
        //diskClusterNumber[] 是update之前文件再磁盘上的存储位置
        //emptyClusterNumber 是update所需要的簇的位置
        FAT fat = new FAT();
        int clusterNumber = (contents.length + 64) / ConstVar.cluster + 1;
        int[] diskClusterNumber = fat.readFileNumbers(startCluster);
        int[] emptyClusterNumber;

        if (diskClusterNumber.length == clusterNumber) {//1-----------如果所需簇个数相同，不做更改，直接重写
            emptyClusterNumber = diskClusterNumber;
            FileHeader newFileHeader = new FileHeader(fileHeader);//创建fileHeader对象方便为fileHeader部分属性赋值
            newFileHeader.setStartCluster(emptyClusterNumber[0]);
            newFileHeader.setFileLength(contents.length + 64);
            fileHeader = newFileHeader.getBytes();//设置好文件头信息的fileHeader

            fat.writeMany(emptyClusterNumber);//1------------写fat表
            DataArea dataArea = new DataArea();
            byte[] fileHeaderAndContent = new byte[64 + contents.length];//将所有内容封入fileHeaderAndContent
            for (int i = 0; i < 64; i++)
                fileHeaderAndContent[i] = fileHeader[i];
            for (int i = 64; i < fileHeaderAndContent.length; i++) {
                fileHeaderAndContent[i] = contents[i - 64];
            }
            dataArea.writeMany(emptyClusterNumber, fileHeaderAndContent);//2-----------写dataArea
        } else if (diskClusterNumber.length > clusterNumber) {//2-------------所需簇个数小于原来的，需要更改fat表，数据区直接重写
            emptyClusterNumber = new int[clusterNumber];
            for (int i = 0; i < clusterNumber; i++)
                emptyClusterNumber[i] = diskClusterNumber[i];

            fat.write(diskClusterNumber[clusterNumber - 1], ConstVar.fatItemEndValue);//1----------fat表写入终止值
            for (int i = clusterNumber; i < diskClusterNumber.length; i++) {//1---------------fat表终止值后的清空，表示可用
                fat.write(diskClusterNumber[i], ConstVar.fatItemEmptyValue);
            }

            DataArea dataArea = new DataArea();
            byte[] fileHeaderAndContent = new byte[64 + contents.length];//将所有内容封入fileHeaderAndContent
            for (int i = 0; i < 64; i++)
                fileHeaderAndContent[i] = fileHeader[i];
            for (int i = 64; i < fileHeaderAndContent.length; i++) {
                fileHeaderAndContent[i] = contents[i - 64];
            }
            dataArea.writeMany(emptyClusterNumber, fileHeaderAndContent);//2-----------写dataArea
        } else {//3-----------如果簇个数大于原来的，需更改fat表，数据区重写
            emptyClusterNumber = new int[clusterNumber];
            int[] extraCluster = fat.getEmptyItem(clusterNumber - diskClusterNumber.length);
            for (int i = 0; i < diskClusterNumber.length; i++)
                emptyClusterNumber[i] = diskClusterNumber[i];
            for (int i = diskClusterNumber.length; i < clusterNumber; i++)
                emptyClusterNumber[i] = extraCluster[i - diskClusterNumber.length];

            fat.write(diskClusterNumber[diskClusterNumber.length - 1], extraCluster[0]);//1--------update之前最后一个fat表项赋值为新增加的fat表项的第一个的值
            fat.writeMany(extraCluster);//新填的fat表项写入磁盘

            DataArea dataArea = new DataArea();
            byte[] fileHeaderAndContent = new byte[64 + contents.length];//将所有内容封入fileHeaderAndContent
            for (int i = 0; i < 64; i++)
                fileHeaderAndContent[i] = fileHeader[i];
            for (int i = 64; i < fileHeaderAndContent.length; i++) {
                fileHeaderAndContent[i] = contents[i - 64];
            }
            dataArea.writeMany(emptyClusterNumber, fileHeaderAndContent);//2-----------写dataArea
        }
        return true;
    }


    @Override
    public List<byte[]> readFile(int startCluster) {
        FAT fat = new FAT();
        DataArea dataArea = new DataArea();
        int[] number = fat.readFileNumbers(startCluster);//number存储要进行读取的簇号
        byte[] fileAndContent = dataArea.readMany(number);//content存储读出的内容(是簇长度的整数倍，需要根据文件长度取舍)
        byte[] fileHeader = new byte[64];//1--------------读出fileHeader
        for (int i = 0; i < 64; i++)
            fileHeader[i] = fileAndContent[i];
        FileHeader fileHeader1 = new FileHeader(fileHeader);

        int fileLength = fileHeader1.getFileLength();//得到文件长度
        byte[] content = new byte[fileLength - 64];//2---------------读出content
        for (int i = 64; i < fileLength; i++)
            content[i - 64] = fileAndContent[i];

        List<byte[]> result = new ArrayList<>();
        result.add(fileHeader);
        result.add(content);
        return result;
    }

    /*
    删除文件需要将相应的fat表项置空
    同时将父文件夹内容修改
     */
    @Override
    public boolean deleteFile(int startCluster, int parentCluster) {
        FAT fat = new FAT();
        DataArea dataArea = new DataArea();
        int[] fileNumber = fat.readFileNumbers(startCluster);
        for (int i = 0; i < fileNumber.length; i++)
            fat.write(fileNumber[i], ConstVar.fatItemEmptyValue);//1--------------要删除的文件的fat表项置空

        //修改父文件夹内容
        int[] parentFileNumber=fat.readFileNumbers(parentCluster);
        byte[] fileHeaderAndContent = dataArea.readMany(parentFileNumber);//父文件夹所有内容

        byte[] fileHeader = new byte[64];//父文件夹文件头
        for (int i = 0; i < 64; i++)
            fileHeader[i] = fileHeaderAndContent[i];
        FileHeader parentFileHeader = new FileHeader(fileHeader);

        int fileLength = parentFileHeader.getFileLength();
        parentFileHeader.setFileLength(fileLength-64);//1--------------修改父文件夹文件头

        byte[] folderContent=new byte[64];//2-----------------修改父文件夹folderContent
        FolderContent parentFolderContent=new FolderContent();//父文件夹folderContent
        for (int i=0;i<(fileLength-64)/64;i++){//每次循环读出一个fileHeader
            for (int j=0;j<64;j++){
                folderContent[j]=fileHeaderAndContent[64+i*64+j];
            }
            FileHeader parentFolderContentItem=new FileHeader(folderContent);
            if (parentFolderContentItem.getStartCluster()!=startCluster)
                parentFolderContent.addFileHeader(parentFolderContentItem);
        }
        byte[] newFolderContent= parentFolderContent.getByte();

        createFileWithoutUpdateParentFolder(fileHeader,newFolderContent);
        return true;
    }
}
