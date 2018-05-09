package service;
import disk.DiskHandler;
import disk.DiskHandlerImpl;

import java.util.ArrayList;

/**
 * @author: Li Xueyang
 * @time: 2018/5/6 20 27
 * @description:向用户视图层提供文件系统的各种服务api
 */
public class FileService {

    private DiskHandler diskHandler=new DiskHandlerImpl();
    private MyFile myfile;
    private MyFolder myFolder;
    private ArrayList<Integer> directoryStack;   //记录当前目录信息的栈
    private ArrayList<String> path;             //记录当前的路径栈

    /**
     * 列出当前目录下的所有文件及其信息（未完成）
     * @return
     */
    public StringBuffer listCurrentDirectory(){
        StringBuffer filesAndDirectories=new StringBuffer();
        return filesAndDirectories;
    }

    /**
     * 改变当前目录（未完成）
     * @param directoryName
     * @return
     */
    public String changeDirectory(String directoryName){
        String currentPath = new String();
        return currentPath;
    }

    /**
     * 列出磁盘所有文件（未完成）
     * @return
     */
    public StringBuffer listAllDirectory(){
        StringBuffer tree=new StringBuffer();
        return tree;
    }

    /**
     * 创建文件（未完成）
     * @param fileName
     * @return
     */
    public boolean creatFile(String fileName){
        //尚未处理带路径的文件名参数
        myfile=new MyFile();
        myfile.getFileHeader().setFileName(fileName);
        myfile.getFileHeader().setFolder(false);
        myfile.getFileHeader().setFileLength(0);
        return true;
    }

    /**
     * 删除文件（未完成）
     * @param fileName
     * @return
     */
    public boolean deleteFile(String fileName){
        return true;
    }

    /**
     * 判断文件是否存在（未完成）
     * @param fileName
     * @return
     */
    public boolean fileExist(String fileName){
        return true;
    }

    /**
     * 创建目录（未完成）
     * @param directoryName
     * @return
     */
    public boolean creatDirectory(String directoryName){
        return true;
    }

    /**
     * 删除目录（未完成）
     * @param directoryName
     * @return
     */
    public boolean deleteDirectory(String directoryName){
        return true;
    }

    /**
     * 判断目录是否存在（未完成）
     * @param directoryName
     * @return
     */
    public boolean directoryExist(String directoryName){
        return true;
    }

    /**
     * 格式化磁盘
     * @return
     */
    public boolean format(){
        diskHandler.formate();
        return true;
    }

    /**
     * 读磁盘文件，并加载到缓存区（未完成）
     * @param fileName
     * @param content
     * @return
     */
    public boolean readFile(String fileName,StringBuffer content){
        return true;
    }

    /**
     * 写缓存区文件，写到磁盘（未完成）
     * @param fileName
     * @param content
     * @return
     */
    public boolean writeFile(String fileName, String content) {
        return true;
    }

    /**
     * 将文件名参数分离为父路径和文件名或目录名（未完成）
     * @param fileNmae
     * @return
     */
    public String[] divideIntoParentPathAndName(String fileNmae){
        String[] parentPathAndName= new String[2];
        return parentPathAndName;
    }

    /**
     * 判断路径是否存在,若存在，返回起始簇至，若不存在，返回-1（未完成）
     * @param path
     * @return
     */
    public int pathExist(String path){
        int clusterOfPath=-1;//路径不存在时返回值
        return clusterOfPath;
    }
}
