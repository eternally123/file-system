package service;
import disk.DiskHandler;
import disk.DiskHandlerImpl;
import disk.FileHeader;
import disk.FolderContent;

import java.util.ArrayList;

import static common.ConstVar.rootItemNumber;

/**
 * @author: Li Xueyang
 * @time: 2018/5/6 20 27
 * @description: 向用户视图层提供文件系统的各种服务api
 */
public class FileService {

    private DiskHandler diskHandler=new DiskHandlerImpl();
    private MyFile myfile;
    private MyFolder myFolder;
    private ArrayList<Integer> currentDirectoryStack;   //记录当前目录信息的栈
    private ArrayList<String> pathStack;             //记录当前的路径栈
    public FileService(){
        currentDirectoryStack=new ArrayList<>(1);
        currentDirectoryStack.add(0);   //存入根目录
        pathStack = new ArrayList<>(1);
        pathStack.add("root");
    }
    public String getCurrentPath(){
        StringBuilder currentPath=new StringBuilder();
        for (String path:pathStack){
            currentPath.append("/").append(path);
        }
        currentPath.deleteCharAt(0);
        return currentPath.toString();
    }

    /**
     * 列出当前目录下的所有文件及其信息（未测试）
     * @return
     */
    public StringBuffer listCurrentDirectory(){
        StringBuffer filesAndDirectories=new StringBuffer();
        byte[] content=diskHandler.readFile(currentDirectoryStack.get(currentDirectoryStack.size()-1)).get(1);
        FolderContent folderContent=new FolderContent(content);
        for (FileHeader fileHeader:folderContent.getAllFolderFileHeader()){
            filesAndDirectories.append(fileHeader.getFileName()).append(" folder ").append(fileHeader.getFileLength()).append(" \n");
        }
        for (FileHeader fileHeader:folderContent.getAllFileFileHeader()){
            filesAndDirectories.append(fileHeader.getFileName()).append(" file   ").append(fileHeader.getFileLength()).append(" \n");
        }
        return filesAndDirectories;
    }

    /**
     * 改变当前目录（未测试），只提供cd.或cd..或cd/功能
     * @param directoryName
     * @return
     */
    public String changeDirectory(String directoryName){

        if (directoryName.compareTo("/")==0){
            while(currentDirectoryStack.size()>1){
                currentDirectoryStack.remove(currentDirectoryStack.size()-1);
                pathStack.remove(pathStack.size()-1);
            }
        }
        if (directoryName.compareTo("..")==0) {
            currentDirectoryStack.remove(currentDirectoryStack.size() - 1);   //返回上级目录
            pathStack.remove(pathStack.size() - 1);
        }
        return getCurrentPath();
    }

    /**
     * 列出磁盘所有文件（）
     * @return
     */
    public StringBuffer listAllDirectory(){
        StringBuffer tree=listDeep("",rootItemNumber);
        return tree;
    }

    /**
     * 列出给定指定文件夹下的所有文件，并加上前缀输出
     * @param before
     * @param parentCluster
     * @return
     */
    public StringBuffer listDeep(String before,int parentCluster){
        StringBuffer result=new StringBuffer();
        FolderContent folderContent=new FolderContent(diskHandler.readFile(parentCluster).get(1));
        for (FileHeader fileHeader:folderContent.getAllFileFileHeader()){
            result.append(before).append(fileHeader.getFileName()).append("\n");
        }
        for (FileHeader folderHeader:folderContent.getAllFolderFileHeader()){
            String newBefore=before+folderHeader.getFileName()+"/";
            result.append(listDeep(newBefore,folderHeader.getStartCluster()));
        }
        return result;
    }


    /**
     * 判断文件是否存在（未测试）
     * @param fileName
     * @return
     */
    public boolean fileExist(String fileName){
        if (fileName.indexOf('/')==-1){     //文件名参数仅含文件名时
            if (compareFileOnce(fileName,currentDirectoryStack.get(currentDirectoryStack.size()-1))<0){
                return false;       //文件不存在当前文件夹下
            }else{
                return true;        //文件存在于当前文件夹下
            }
        }else {     //文件名参数含有绝对路径时
            String path = divideIntoParentPathAndName(fileName)[0];//路径
            String name = divideIntoParentPathAndName(fileName)[1];//文件名
            if (pathExist(path) < 0) {     //如果路径不存在
                return false;
            } else {
                if (compareFileOnce(name, pathExist(path)) < 0) { //路径存在文件不存在
                    return false;
                } else {       //路径存在文件存在
                    return true;
                }
            }
        }
    }

    /**
     * 创建文件（未测试）,此前已经判断文件是否存在
     * @param fileName
     * @return
     */
    public boolean creatFile(String fileName){  //暂且用null代替内容，需要交流
        int parentCluster;
        myfile=new MyFile();
        myfile.getFileHeader().setFolder(false);
        myfile.getFileHeader().setFileLength(0);
        myfile.getFileHeader().setStartCluster();
        if (fileName.indexOf('/')==-1){     //文件名参数不含绝对路径
            myfile.getFileHeader().setFileName(fileName);
            parentCluster=currentDirectoryStack.get(currentDirectoryStack.size()-1);
        }else{  //文件名参数含绝对路径
            String path=divideIntoParentPathAndName(fileName)[0];
            String name=divideIntoParentPathAndName(fileName)[1];
            myfile.getFileHeader().setFileName(name);
            parentCluster=pathExist(path);
        }
        return diskHandler.createFile(myfile.getFileHeader().getBytes(),parentCluster);
    }

    /**
     * 删除文件（未测试），删除前已判断文件是否存在
     * @param fileName
     * @return
     */
    public boolean deleteFile(String fileName){
        int parentDirectoryCluster;  //父目录的起始簇号
        int startClusterOfFile;    //文件的起始簇号
        if (fileName.indexOf('/')==-1){     //文件名参数不含绝对路径
            parentDirectoryCluster=currentDirectoryStack.get(currentDirectoryStack.size()-1);
            startClusterOfFile=compareFileOnce(fileName,parentDirectoryCluster);
        }else{      //文件名参数不含绝对路径
            String path=divideIntoParentPathAndName(fileName)[0];
            String name=divideIntoParentPathAndName(fileName)[1];
            parentDirectoryCluster=pathExist(path);
            startClusterOfFile=compareFileOnce(name,parentDirectoryCluster);
        }
        return diskHandler.deleteFile(startClusterOfFile,parentDirectoryCluster);
    }


    /**
     * 判断目录是否存在（未测试）
     * @param directoryName
     * @return
     */
    public boolean directoryExist(String directoryName){
        if (directoryName.indexOf('/')==-1){    //目录名参数仅含有目录名时
            if (compareDirectoryOnce(directoryName,currentDirectoryStack.get(currentDirectoryStack.size()-1))<0){
                //目录不存在当前文件夹下
                return false;
            }else { //目录存在当前文件夹下
                return true;
            }
        }else {     //目录名参数为带绝对路径时
            String path = divideIntoParentPathAndName(directoryName)[0];//目录的父路径
            String directory = divideIntoParentPathAndName(directoryName)[1];//目录名
            if (pathExist(path) < 0) {     //如果路径不存在
                return false;
            } else {
                if (compareDirectoryOnce(directory, pathExist(path)) < 0) { //路径存在目录不存在
                    return false;
                } else {       //路径存在目录存在
                    return true;
                }
            }
        }
    }

    /**
     * 创建目录（未测试）
     * @param directoryName
     * @return
     */
    public boolean creatDirectory(String directoryName){
        int parentCluster;
        myFolder = new MyFolder();
        myFolder.getFolderHeader().setFolder(true);
        myFolder.getFolderHeader().setFileLength(0);
        myFolder.getFolderHeader().setStartCluster();
        if (directoryName.indexOf('/')==-1){    //目录名参数不含绝对路径
            myFolder.getFolderHeader().setFileName(directoryName);
            parentCluster=currentDirectoryStack.get(currentDirectoryStack.size()-1);
        }else {      //目录名参数不含绝对路径
            String path=divideIntoParentPathAndName(directoryName)[0];
            String name=divideIntoParentPathAndName(directoryName)[1];
            myFolder.getFolderHeader().setFileName(name);
            parentCluster=pathExist(path);
        }
        return (diskHandler.createFile(myFolder.getFolderHeader().getBytes(),parentCluster));
    }

    /**
     * 删除目录（未测试）
     * @param directoryName
     * @return
     */
    public boolean deleteDirectory(String directoryName){
        int parentCluster;
        int directoryCluster;
        if (directoryName.indexOf('/')==-1){    //目录名参数不含绝对路径
            parentCluster=currentDirectoryStack.get(currentDirectoryStack.size()-1);
            directoryCluster=compareDirectoryOnce(directoryName,parentCluster);
        }else{  //目录名参数含绝对路径
            String path=divideIntoParentPathAndName(directoryName)[0];
            String name=divideIntoParentPathAndName(directoryName)[1];
            parentCluster=pathExist(path);
            directoryCluster=compareDirectoryOnce(name,parentCluster);
        }
        return (diskHandler.deleteFile(directoryCluster,parentCluster));
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
     * 读磁盘文件，并加载到缓存区（未测试），加载前已判断该文件是否存在
     * @param fileName
     * @return
     */
    public String readFile(String fileName){
        int parentCluster;
        int fileCluster;
        if (fileName.indexOf('/')!=-1){ //文件名含绝对路径
            String path=divideIntoParentPathAndName(fileName)[0];
            String name=divideIntoParentPathAndName(fileName)[1];
            parentCluster=pathExist(path);
            fileCluster=compareFileOnce(name,parentCluster);
        }
        else{   //文件名不含绝对路径
            parentCluster=currentDirectoryStack.get(currentDirectoryStack.size()-1);
            fileCluster=compareFileOnce(fileName,parentCluster);
        }
        String  content=new String(diskHandler.readFile(fileCluster).get(1));
        return  content;
    }

    /**
     * 写缓存区文件，写到磁盘（未完成）
     * @param fileName
     * @param content
     * @return
     */
    public boolean writeFile(String fileName, String content) {
        int clusterOfParent=0;
        int clusterOfFile=0;
        byte[] fileHeader;
        if (fileName.indexOf('/')==-1) { //文件名参数不含绝对路径
            clusterOfParent = currentDirectoryStack.get(currentDirectoryStack.size() - 1);
            clusterOfFile = compareFileOnce(fileName, clusterOfParent);
        }
        if (fileName.indexOf('/')!=-1){  //文件名参数含有绝对路径
            String parentPath =divideIntoParentPathAndName(fileName)[0];
            String name=divideIntoParentPathAndName(fileName)[1];
            clusterOfParent=pathExist(parentPath);
            clusterOfFile=compareFileOnce(name,clusterOfParent);
        }
        fileHeader=diskHandler.readFile(clusterOfFile).get(0);
        return diskHandler.writeFile(clusterOfFile,fileHeader,content.getBytes(),clusterOfParent);
    }

    /**
     * 将文件名参数（绝对路径）分离为父路径和文件名或目录名（完成）
     * @param fileName
     * @return
     */

    public String[] divideIntoParentPathAndName(String fileName){
        String[] parentPathAndName= new String[2];
        int pos=fileName.lastIndexOf("/");
        parentPathAndName[0]=fileName.substring(0,pos);//父路径
        parentPathAndName[1]=fileName.substring(pos+1);//文件名或目录名
        return parentPathAndName;
    }
    /**
     * 判断路径是否存在,若存在，返回起始簇值，若不存在，返回-1（未测试）
     * @param path
     * @return
     */
    public int pathExist(String path) {
        int parentCluster=rootItemNumber;
        if (path.compareTo("/") == 0) {
            return rootItemNumber;//如果路径为根目录，返回根目录的起始簇值
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
        for (FileHeader foldHeader :myFolder.getFolderContent().getAllFolderFileHeader()){       //遍历父目录的所有子目录
            if (directoryName.compareTo(foldHeader.getFileName())==0){                     //找到目录
                resultCluster=foldHeader.getStartCluster();
                break;
            }
        }
        return resultCluster;
    }

    /**
     * 在一个父目录下寻找一个文件，只查询一级，查到返回文件起始簇号，否则返回-1（未测试）
     * @param fileName
     * @param clusterOfParentDirectory
     * @return
     */
    public int compareFileOnce(String fileName,int clusterOfParentDirectory){
        int resultCluster=-1;
        myFolder=new MyFolder();
        myFolder.setFolderContent(new FolderContent(diskHandler.readFile(clusterOfParentDirectory).get(1)));//获取父目录的内容
        for (FileHeader fileHeader :myFolder.getFolderContent().getAllFileFileHeader()){       //遍历父目录的所有文件
            if (fileName.compareTo(fileHeader.getFileName())==0){                     //找到文件
                resultCluster=fileHeader.getStartCluster();
                break;
            }
        }
        return resultCluster;
    }

    //测试
    public static void main(String[] args){

    }
}
