package view;

import service.FileService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author: Li Xueyang
 * @time: 2018/5/6 20 10
 * @description:用户界面的面板组件
 */
public class CommandPanel extends JPanel {
    private FileEditor fileEditor;
    private JTextField commandInput;
    private JTextArea commandOutput;
    private FileService fileService;
    private String currentPath = "root:\\>";

    public CommandPanel(FileService fileService) {
        this.fileService = fileService;
        init();
    }

    /**
     * 初始化用户界面命令行面板组件
     */
    private void init(){
        /**
         * 设置总命令面板参数
         */
        setLayout(new BorderLayout());//设置布局管理
        Border brd = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK);


        /**
         * 设置命令输入面板区域
         */
        commandInput = new JTextField();//创建单行文本，用于输入命令
        commandInput.setBorder(brd);
        commandInput.setBackground(Color.YELLOW);
        //textInput.setForeground(Color.WHITE);
        KeyHandler KeyListener = new KeyHandler();
        commandInput.addKeyListener(KeyListener);   //为输入命令的单行文本添加监听器
        commandInput.setFont(new Font("Verdana", Font.BOLD, 18));   //设置当前字体
        commandInput.setFocusable(true);    //获取焦点
        /*设置左侧提示标签*/
        JLabel label = new JLabel("[INPUT]");
        label.setFont(new Font("Times New Roman", Font.BOLD, 15));
        label.setBorder(brd);
        label.setForeground(Color.black);
        /*设置命令输入面板区域位于下方*/
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.WEST);
        panel.add(commandInput);
        this.add(panel, BorderLayout.SOUTH);
        /**
         * 设置命令输出面板区域
         */
        commandOutput= new JTextArea();
        commandOutput.setBorder(brd);
        commandOutput.setLineWrap(true);
        commandOutput.setWrapStyleWord(true);
        commandOutput.setFocusable(false);
        commandOutput.setBackground(Color.DARK_GRAY);
        commandOutput.setForeground(Color.GREEN);
        commandOutput.setFont(new Font("Verdana", Font.BOLD, 15));
        commandOutput.append(currentPath);
        JScrollPane spOutput = new JScrollPane(commandOutput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(spOutput);
    }

    /**
     * 取出并处理从命令输入面板获取到的命令
     * @param command
     */
    private void handleInputCommand(String command){
        System.out.println(command);
        String commandHead,parameter;
        /*取指令*/
        int pos = command.indexOf(" ");
        if (pos == -1) {
            commandHead =command;
            parameter = "";
        } else {
            commandHead = command.substring(0, pos);
            parameter = command.substring(pos).trim();
        }
        commandHead = commandHead.toLowerCase();
        parameter = parameter.toLowerCase();
        System.out.println("head: "+commandHead+" parameter: "+parameter);
        /*处理指令*/
        if (commandHead.compareTo("cd") == 0 &&
                (parameter.compareTo("\\") == 0 || parameter.compareTo("..") == 0 || parameter.compareTo(".") == 0)) {
            commandOutput.append("commandHead: "+commandHead+" parameter: "+parameter);
        }
        else if (commandHead.compareTo("ls") == 0 && parameter.compareTo("") == 0) {
            commandOutput.append("commandHead: "+commandHead+" parameter: "+parameter);
        }
        else if (commandHead.compareTo("help") == 0) {
            commandOutput.append("commandHead: "+commandHead+" parameter: "+parameter);
        }
        else if (commandHead.compareTo("md") == 0) {
            commandOutput.append("commandHead: "+commandHead+" parameter: "+parameter);
        }
        else if (commandHead.compareTo("rd") == 0) {
            commandOutput.append("commandHead: "+commandHead+" parameter: "+parameter);
        }
        else if (commandHead.compareTo("mf") == 0) {
            commandOutput.append("commandHead: "+commandHead+" parameter: "+parameter);
        }
        else if (commandHead.compareTo("rf") == 0) {
            commandOutput.append("commandHead: "+commandHead+" parameter: "+parameter);
        }
        else if (commandHead.compareTo("edit") == 0) {
            commandOutput.append("commandHead: "+commandHead+" parameter: "+parameter);
            fileEditor=new FileEditor(null,parameter,fileService);
            fileEditor.show();
        }
        else if (commandHead.compareTo("format") == 0) {
            commandOutput.append("commandHead: "+commandHead+" parameter: "+parameter);
        } else {
            commandOutput.append(command + "' is not a valid command !\nPlease input \'help\' to gain valid command ^_^ ");
        }
        commandOutput.append("\n\n");
        commandOutput.setCaretPosition(commandOutput.getText().length());
    }

    /**
     * 指令输入监听器
     */
    private class KeyHandler implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_ENTER
                    && commandInput.getText().compareTo("") != 0) {
                handleInputCommand(commandInput.getText());
                commandInput.setText("");
                commandOutput.append(currentPath);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
