package disk;

import java.util.List;

public class DiskHandlerImpl implements DiskHandler {
    @Override
    public void formate() {

    }

    @Override
    public int writeFile(byte[] fileHeader, byte[] contents, int ParentCluster) {
        return 0;
    }

    @Override
    public int updateFile(int startCluster, byte[] fileHeader, byte[] contents, int ParentCluster) {
        return 0;
    }

    @Override
    public List<byte[]> readFile(int startCluster) {
        return null;
    }
}
