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
        setTitle("FileSystem");//设置标题

        CommandPanel filePanel = new CommandPanel(fileService);
        Container contentPane = getContentPane();//设置一个容器
        contentPane.add(filePanel);//将命令行面板组件实例添加到容器中

        ////////////////////////////配置环境///////////////////////////
        addWindowListener(new WindowAdapter() {
            //关闭窗口
            public void windowClosing(WindowEvent arg0) {
                /*try {
                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("deploy.ini")));
                    for (int i = 0; i < Utility.NUM_OF_ROOTFILE; i++) {
                        for (int j = 0; j < Utility.SIZE_OF_FILEINFO; j++) {
                            out.writeChar(fileManager.rootTable[i][j]);
                        }

                    }
                    for (int i = 0; i < Utility.NUM_OF_DATASECTOR; i++) {
                        out.writeChar(fileManager.fatTable[i]);
                    }

                    for (int i = 0; i < Utility.NUM_OF_DATASECTOR; i++) {
                        for (int j = 0; j < Utility.SIZE_OF_SECTOR; j++) {
                            out.writeChar(fileManager.dataArea[i][j]);
                        }
                    }
                    out.close();

                } catch (Exception e) {
                    System.out.println(e);
                }*/
                System.out.println("windowClosing");
            }
            //打开窗口
            public void windowOpened(WindowEvent arg0) {
                /*try {
                    DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream("deploy.ini")));
                    for (int i = 0; i < Utility.NUM_OF_ROOTFILE; i++) {
                        for (int j = 0; j < Utility.SIZE_OF_FILEINFO; j++) {
                            fileManager.rootTable[i][j] = in.readChar();
                        }
                    }
                    for (int i = 0; i < Utility.NUM_OF_DATASECTOR; i++) {
                        fileManager.fatTable[i] = in.readChar();
                    }

                    for (int j = 0; j < Utility.NUM_OF_DATASECTOR; j++) {
                        for (int i = 0; i < Utility.SIZE_OF_SECTOR; i++) {
                            fileManager.dataArea[j][i] = in.readChar();
                        }
                    }
                    in.close();

                } catch (Exception e) {
                    System.out.println(e);
                }*/
            }
        });

    }

}
