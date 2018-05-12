package disk;

import java.util.List;

public interface DiskHandler {
    //格式化磁盘，需要格式化FAT表并对数据区和fat表写入初始值
    public void formate();

    //给出文件头和文件内容的byte表示，将文件内容写入磁盘，返回是否update成功
    public boolean createFile(byte[] fileHeader, int parentCluster);

    //给出文件起始簇号文件头和文件内容的byte表示，将文件内容写入磁盘，返回是否update成功
    public boolean writeFile(int startCluster, byte[] fileHeader, byte[] contents, int parentCluster);

    //给出起始簇号，返回以此簇开始的文件内容(返回值是一个ArrayList<byte[]>，有两个byte[]元素，第一个是fileHeader的byte表示，第二个是文件内容的byte表示)
    public List<byte[]> readFile(int startCluster);

    //给出文件起始簇号，删除文件(主义是文件，不能删除文件夹)
    public boolean deleteFile(int startCluster,int parentCluster);

}
