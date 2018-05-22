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
 * @description: 用户界面的面板组件
 */
public class CommandPanel extends JPanel {
    private FileEditor fileEditor;
    private JTextField commandInput;
    private JTextArea commandOutput;
    private FileService fileService;
    private String currentPath;

    public CommandPanel(FileService fileService) {
        this.fileService = fileService;
    this.currentPath=fileService.getCurrentPath()+">";
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
        commandInput.setBackground(Color.white);
        commandInput.setForeground(Color.blue);
        KeyHandler KeyListener = new KeyHandler();
        commandInput.addKeyListener(KeyListener);   //为输入命令的单行文本添加监听器
        commandInput.setFont(new Font("宋体 ", Font.PLAIN, 18));   //设置当前字体
        commandInput.setFocusable(true);    //获取焦点
        /*设置左侧提示标签*/
        JLabel label = new JLabel("INPUT:");
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
        commandOutput.setBackground(Color.darkGray);
        commandOutput.setForeground(Color.white);
        commandOutput.setFont(new Font("宋体", Font.BOLD, 15));
        commandOutput.append("Welcome to the file system! You can input help to gain help!\n");
        commandOutput.append(currentPath);
        JScrollPane spOutput = new JScrollPane(commandOutput, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(spOutput);
    }

    /**
     * 取出并处理从命令输入面板获取到的命令
     * @param command
     */
    private void handleInputCommand(String command){
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
        //System.out.println("head: "+commandHead+" parameter: "+parameter);
        /*处理指令*/
        if (commandHead.compareTo("cd") == 0 && !parameter.isEmpty()){
            System.out.println("flag");
            if(parameter.compareTo("/") == 0 || parameter.compareTo("..") == 0 || fileService.directoryExist(parameter)) {
                commandOutput.append(commandHead + " " + parameter + "\n");
                fileService.changeDirectory(parameter);
            }else{
                commandOutput.append("The directory dosen't exist!\n");
            }
        }
        else if (commandHead.compareTo("tree")==0){
            commandOutput.append(commandHead+" "+parameter+"\n");
            commandOutput.append((fileService.listAllDirectory()).toString());
        }
        else if (commandHead.compareTo("ls") == 0 && (parameter.isEmpty() || parameter.compareTo("-l")==0)) {
            commandOutput.append(commandHead + " " + parameter + "\n");
            if (parameter.isEmpty()) {
                commandOutput.append((fileService.listCurrentDirectory(false)).toString());
            }else{
                commandOutput.append((fileService.listCurrentDirectory(true)).toString());
            }
        }
        else if (commandHead.compareTo("help") == 0) {
            commandOutput.append(commandHead+" "+parameter);
            handleHelpCommand();
        }
        else if (commandHead.compareTo("md") == 0 && !parameter.isEmpty()) {
            commandOutput.append(commandHead+" "+parameter+"\n");
            handleMdCommand(parameter);

        }
        else if (commandHead.compareTo("rd") == 0 && !parameter.isEmpty()) {
            commandOutput.append(commandHead+" "+parameter+"\n");
            handleRdCommand(parameter);

        }
        else if (commandHead.compareTo("mf") == 0 && !parameter.isEmpty()) {
            commandOutput.append(commandHead+" "+parameter+"\n");
            handleMfCommand(parameter);
        }
        else if (commandHead.compareTo("rf") == 0 && !parameter.isEmpty()) {
            commandOutput.append(commandHead+" "+parameter+"\n");
            handleRfCommand(parameter);
        }
        else if (commandHead.compareTo("edit") == 0 && !parameter.isEmpty()) {
            commandOutput.append(commandHead+" "+parameter+"\n");
            handleEditCommand(parameter);
        }
        else if (commandHead.compareTo("format") == 0) {
            commandOutput.append("commandHead: "+commandHead+" parameter: "+parameter);
            if (fileService.format()){
                commandOutput.append("Format successfully!\n");
            }
        } else {
            commandOutput.append(command + "' is not a valid command !\nPlease input \'help\' to gain valid command ^_^ ");
        }
        commandOutput.append("\n\n");
        commandOutput.setCaretPosition(commandOutput.getText().length());
    }
    public void handleHelpCommand(){
        commandOutput.append("\nWelcome to help!\n");
        commandOutput.append("command  parameter       -->usage are those:\n");
        commandOutput.append("help                     -->to gain help about the command information.\n");
        commandOutput.append("ls       -l|             -->to list the files and directories of current directory.\n");
        commandOutput.append("cd       .|..|/          -->to change current directory.\n");
        commandOutput.append("tree                     --> to list all files of the file system.\n");
        commandOutput.append("md       directoryName   -->to make a directory.\n");
        commandOutput.append("rd       directoryName   -->to remove a directory.\n");
        commandOutput.append("mf       fileName        -->to make a file.\n");
        commandOutput.append("rf       fileName        -->to remove a file.\n");
        commandOutput.append("edit     fileName        -->to edit a file.\n");
        commandOutput.append("format                   -->to format the disk.\n");
    }
    public void handleCdCommand(String parameter){
    }
    public void handleMdCommand(String parameter){
        if (fileService.directoryExist(parameter)){
            commandOutput.append("The directory already exists!\n");
        }else {
            if (fileService.creatDirectory(parameter)) {
                commandOutput.append("The directory has been created successfully!\n");
            }else{
                commandOutput.append("Failed to create directory ,unknown error happened!\n");
            }
        }
    }
    public void handleRdCommand(String parameter){
        if (fileService.directoryExist(parameter)){
            if (fileService.deleteDirectory(parameter)){
                commandOutput.append("Directory deleted successfully!\n");
            }
        }else{
            commandOutput.append("The directory dosen't exist!\n");
        }
    }
    public void handleMfCommand(String parameter){
        if (fileService.fileExist(parameter)){
            System.out.println("flag1");
            commandOutput.append("The file already exists!\n");
        }else {
            System.out.println("flag2");

            if (fileService.creatFile(parameter)) {
                commandOutput.append("The file has been created successfully!\n");
            }else{
                commandOutput.append("Failed to create the file,unknown error happened!\n");
            }
        }
    }
    public void handleRfCommand(String parameter){
        if (fileService.fileExist(parameter)){
            if (fileService.deleteFile(parameter)){
                commandOutput.append("The file deleted successfully!\n");
            }
        }else {
            commandOutput.append("The file dosen't exist!\n");
        }
    }
    public void handleEditCommand(String parameter){
        if (!fileService.fileExist(parameter)) {
            fileService.creatFile(parameter);
        }
        //如果要编辑的文件不存在，先创建该文件
        fileEditor = new FileEditor(null, parameter, fileService);
        fileEditor.show();
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
                commandOutput.append(fileService.getCurrentPath()+">");
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
