package service;
import disk.DiskHandler;
import disk.DiskHandlerImpl;
import disk.FileHeader;
import disk.FolderContent;

import java.util.ArrayList;

import static common.ConstVar.fatStart;

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
     * 创建文件（未完成,等待FileHeader的修改）
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
     * 删除文件（未完成，待提供接口）
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
        String path=divideIntoParentPathAndName(fileName)[0];//路径
        String name=divideIntoParentPathAndName(fileName)[1];//文件名
        if (pathExist(path)<0){     //如果路径不存在
            return false;
        }else{
            if (compareFileOnce(name,pathExist(path))<0){ //路径存在文件不存在
                return false;
            }
            else{       //路径存在文件存在
                return true;
            }
        }
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
     * 删除目录（未完成，待提供接口）
     * @param directoryName
     * @return
     */
    public boolean deleteDirectory(String directoryName){
        return true;
    }

    /**
     * 判断目录是否存在（未测试）
     * @param directoryName
     * @return
     */
    public boolean directoryExist(String directoryName){
        String path=divideIntoParentPathAndName(directoryName)[0];//目录的父路径
        String directory=divideIntoParentPathAndName(directoryName)[1];
        if (pathExist(path)<0){     //如果路径不存在
            return false;
        }else{
            if (compareDirectoryOnce(directory,pathExist(path))<0){ //路径存在目录不存在
                return false;
            }
            else{       //路径存在目录存在
                return true;
            }
        }
    }

    /**
     * 格式化磁盘（未测试）
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
     * 将文件名参数分离为父路径和文件名或目录名（）
     * @param fileName
     * @return
     */
    public String[] divideIntoParentPathAndName(String fileName){   //没考虑只有名字而没有路径的情况
        String[] parentPathAndName= new String[2];
        int pos=fileName.lastIndexOf("/");
        if (fileName.charAt(0)!='/'){
            parentPathAndName[0]="/"+fileName.substring(0,pos);//父路径
        }else{
            parentPathAndName[0]=fileName.substring(0,pos);//父路径
        }
        parentPathAndName[1]=fileName.substring(pos+1);//文件名或目录名
        return parentPathAndName;
    }

    /**
     * 判断路径是否存在,若存在，返回起始簇值，若不存在，返回-1（未测试）
     * @param path
     * @return
     */
    public int pathExist(String path) {
        int parentCluster=fatStart;
        if (path.compareTo("/") == 0) {
            return fatStart;//如果路径为根目录，返回根目录的起始簇值
        } else{ //否则进行查找并得出查找结果
            return compareDirectoryMany(path,parentCluster);
        }
    }

    /**
     * 在一个父目录下寻找一个目录，查询多级，查到返回目录起始簇号，否则返回-1（未测试）
     * @param directoryPath
     * @param clusterOfParentDirectory
     * @return
     */
    public int compareDirectoryMany(String directoryPath,int clusterOfParentDirectory){
        String[] directories = directoryPath.split("/");
        int clusterOfPath=compareDirectoryOnce(directories[0],clusterOfParentDirectory);
        if (clusterOfPath<0){
            return -1;
        }
        else{
            if (directories.length==1){
                return clusterOfPath;
            }
            else {
                StringBuilder last = new StringBuilder();
                for (int j = 1; j < directories.length; j++) {
                    last.append(directories[j]);
                }
                return compareDirectoryMany(last.toString(), clusterOfPath);
            }
        }
    }
    /**
     * 在一个父目录下寻找一个目录，只查询一级，查到返回目录起始簇号，否则返回-1（未测试）
     * @param directoryName
     * @param clusterOfParentDirectory
     * @return
     */
    public int compareDirectoryOnce(String directoryName,int clusterOfParentDirectory){
        int resultCluster=-1;
        myFolder=new MyFolder();
        myFolder.setFolderContent(new FolderContent(diskHandler.readFile(clusterOfParentDirectory).get(1)));//获取父目录的内容
        for (int cluster :myFolder.getFolderContent().getAllFolderCluster()){       //遍历父目录的所有子目录
            FileHeader temp= new FileHeader(diskHandler.readFile(cluster).get(0));  //父目录的每个子目录的目录头
            if (directoryName.compareTo(temp.getFileName())==0){                     //找到目录
                resultCluster=temp.getStartCluster();
                break;
            }
        }
        return resultCluster;
    }

    public int compareFileOnce(String fileName,int clusterOfParentDirectory){
        int resultCluster=-1;
        myFolder=new MyFolder();
        myFolder.setFolderContent(new FolderContent(diskHandler.readFile(clusterOfParentDirectory).get(1)));//获取父目录的内容
        for (int cluster :myFolder.getFolderContent().getAllFileCluster()){       //遍历父目录的所有文件
            FileHeader temp= new FileHeader(diskHandler.readFile(cluster).get(0));  //父目录的每个文件的文件头
            if (fileName.compareTo(temp.getFileName())==0){                     //找到文件
                resultCluster=temp.getStartCluster();
                break;
            }
        }
        return resultCluster;
    }

    //测试
    public static void main(String[] args){
        FileService fileService=new FileService();
    }
}
