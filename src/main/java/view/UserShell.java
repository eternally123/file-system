package view;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * @author: Li Xueyang
 * @time: 2018/5/6 18 59
 * @description:用户的shell界面
 */
public class UserShell {
    public static void main(String[] args){
        FileFrame fileFrame=new FileFrame();
        fileFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        fileFrame.show();

    }
}
