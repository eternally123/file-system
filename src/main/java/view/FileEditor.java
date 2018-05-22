package view;

import service.FileService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author: Li Xueyang
 * @time: 2018/5/6 20 24
 * @description: 文件编辑器
 */
public class FileEditor extends JDialog {
    private JTextArea textArea = new JTextArea();
    private JButton save = new JButton("Save");//保存按钮
    private JButton cancel = new JButton("Canel");//取消按钮
    private String fileName;     //文件名
    private FileService fileService;    //文件服务

    FileEditor(JFrame frame, String fileName,FileService fileService) {
        super(frame, fileName, true);
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)d.getWidth()/2, (int)d.getHeight()/2);
        setLocation((int)d.getWidth()/4, (int)d.getHeight()/4);
        setResizable(false);
        this.fileName = fileName;
        this.fileService=fileService;

        Border brd = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK);
        textArea.setBorder(brd);
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.black);
        textArea.setFont(new Font("宋体", Font.PLAIN, 15));
        textArea.setLineWrap(true);
        ButtonListener listener = new ButtonListener();
        save.addActionListener(listener);//保存按钮设置监听器
        cancel.addActionListener(listener);//取消按钮设置监听器

        //加载文件
        textArea.setText(fileService.readFile(this.fileName));

        //设置滚动面板并添加进容器
        JScrollPane spEdit = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel btPanel = new JPanel();
        btPanel.add(save);
        btPanel.add(cancel);
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(btPanel, BorderLayout.SOUTH);
        container.add(spEdit);
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //保存文件
            if ((JButton) e.getSource() == save) {
                String content = textArea.getText();
                if (fileService.writeFile(fileName,content)) {
                    dispose();
                }
            } else {//取消编辑
                dispose();
            }
        }
    }
}
