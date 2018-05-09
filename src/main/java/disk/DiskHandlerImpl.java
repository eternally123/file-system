package disk;

import java.util.List;

public class DiskHandlerImpl implements DiskHandler {
    @Override
    public void formate() {

    }

    @Override
    public boolean writeFile(byte[] fileHeader, byte[] contents, int ParentCluster) {
        //将此文件写入磁盘---两部分 fileHeader和contents
        //通过ParentCluster找到父文件，修改父文件的contents
        return false;
    }

    @Override
    public boolean updateFile(int startCluster, byte[] fileHeader, byte[] contents, int ParentCluster) {
        return false;
    }

    @Override
    public List<byte[]> readFile(int startCluster) {
        return null;
    }

    @Override
    public boolean deleteFile(int startCluster) {
        return false;
    }
}
