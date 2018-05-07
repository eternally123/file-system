package service;

import java.util.ArrayList;

/**
 * @author: Li Xueyang
 * @time: 2018/5/6 20 27
 * @description:向用户视图层提供文件系统的各种服务api
 */
public class FileService {

    private ArrayList directoryStack;   //记录当前目录信息的栈
    private ArrayList path;             //记录当前的路径栈
    public String[] handleLsCommand(){
        String[] directories=new String[0];
        return directories;
    }
    public String handleCdCommand(String directoryName){
        String currentPath = new String();
        return currentPath;
    }
    public boolean creatFile(String fileName){ return true; }
    public boolean removeFile(String fileName){ return true; }
    public boolean creatDirectory(String directoryName){return true;}
    public boolean removeDirectory(String directoryName){return true;}
    public boolean format(){return true;}
    public boolean readFile(String fileName,StringBuffer content){
        return true;
    }
    public boolean writeFile(String fileName, String content) {
        return true;
    }
}
