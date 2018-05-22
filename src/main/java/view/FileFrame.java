package view;

import service.FileService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

/**
 * @author: Li Xueyang
 * @time: 2018/5/6 20 08
 * @description: 用户界面窗体组件
 */
public class FileFrame extends JFrame{
    private FileService fileService;
    FileFrame(){
        this.fileService=new FileService();
        setSize(800, 400);//设置界面长宽
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)d.getWidth()/4, (int)d.getHeight()/10);
        setTitle("FileSystem");//设置标题

        CommandPanel filePanel = new CommandPanel(fileService);
        Container contentPane = getContentPane();//设置一个容器
        contentPane.add(filePanel);//将命令行面板组件实例添加到容器中

        ////////////////////////////配置环境///////////////////////////
        addWindowListener(new WindowAdapter() {
            //关闭窗口
            public void windowClosing(WindowEvent arg0) {
                System.out.println("windowClosing");
            }
            //打开窗口
            public void windowOpened(WindowEvent arg0) {
            }
        });

    }

}
