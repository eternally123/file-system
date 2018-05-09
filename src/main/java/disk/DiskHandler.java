package disk;

import java.util.List;

public interface DiskHandler {
    //格式化磁盘，需要格式化FAT表并对数据区和fat表写入初始值
    public void formate();

    //给出文件头和文件内容的byte表示，将文件内容写入磁盘
    public int writeFile(byte[] fileHeader, byte[] contents, int ParentCluster);

    //给出文件起始簇号文件头和文件内容的byte表示，将文件内容写入磁盘，并返回文件起始簇号
    public int updateFile(int startCluster, byte[] fileHeader, byte[] contents, int ParentCluster);

    //给出起始簇号，返回以此簇开始的文件内容(返回值是一个ArrayList<byte[]>，有两个byte[]元素，第一个是fileHeader的byte表示，第二个是文件内容的byte表示)
    public List<byte[]> readFile(int startCluster);

}
