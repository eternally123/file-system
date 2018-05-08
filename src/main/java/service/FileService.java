package service;

import disk.DiskHandler;

import java.util.ArrayList;

/**
 * @author: Li Xueyang
 * @time: 2018/5/6 20 27
 * @description:向用户视图层提供文件系统的各种服务api
 */
public class FileService {

    private DiskHandler diskHandler;
    private ArrayList<Integer> directoryStack;   //记录当前目录信息的栈
    private ArrayList<String> path;             //记录当前的路径栈

    public StringBuffer listCurrentDirectory(){
        StringBuffer filesAndDirectories=new StringBuffer();
        return filesAndDirectories;
    }
    public String changeDirectory(String directoryName){
        String currentPath = new String();
        return currentPath;
    }
    public StringBuffer listAllDirectory(){
        StringBuffer tree=new StringBuffer();
        return tree;
    }
    public boolean creatFile(String fileName){
        return true;
    }
    public boolean deleteFile(String fileName){
        return true;
    }
    public boolean fileExist(String fileName){
        return true;
    }
    public boolean creatDirectory(String directoryName){
        return true;
    }
    public boolean deleteDirectory(String directoryName){
        return true;
    }
    public boolean directoryExist(String directoryName){
        return true;
    }
    public boolean format(){
        return true;
    }
    public boolean readFile(String fileName,StringBuffer content){
        return true;
    }
    public boolean writeFile(String fileName, String content) {
        return true;
    }
}
