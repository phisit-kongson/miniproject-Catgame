import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

public class LoginFrame extends JFrame implements ActionListener{
    
    private JLabel text1 ;
    private JLabel text2 ;
    private JLabel title ;
    private JTextField username ;
    private JPasswordField password ;
    private JButton login1Btn ;
    private JButton backBtn ;
    private JFrame preFrame;

    public LoginFrame(JFrame preFrame){
        this.preFrame = preFrame;
        setTitle("Log in");
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,350);

        title = new JLabel("Log In");
        title.setBounds(150,30,100,40);
        title.setFont(new Font("Tahoma", Font.BOLD, 30));
        add(title);

        text1 = new JLabel("username : ");
        text1.setBounds(47, 110, 100, 30);
        text1.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 16));
        add(text1);

        username = new JTextField();
        username.setBounds(160, 110, 180, 30);
        add(username);

        text2 = new JLabel("password : ");
        text2.setBounds(50, 160, 100, 30);
        text2.setFont(new Font("Tahoma", Font.CENTER_BASELINE, 16));
        add(text2);

        password = new JPasswordField();
        password.setBounds(160, 160, 180, 30);
        add(password);

        login1Btn = new JButton("Login");
        login1Btn.setBounds(135, 220, 130, 40);
        login1Btn.setFocusable(false);
        add(login1Btn);
        login1Btn.addActionListener(this);

        backBtn = new JButton("back");
        //backBtn.setBounds(135,260,130,40);
        backBtn.setBounds(0,0,40,40);
        backBtn.setFocusable(false);
        add(backBtn);
        backBtn.addActionListener(e -> {
        preFrame.setVisible(true);   
        dispose();                   
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String usernamein = username.getText();
        String passwordin = new String (password.getPassword());

        if(usernamein.equals("") || passwordin.equals("")){
            System.out.println(" 111");
        }else{
            try (BufferedReader br = new BufferedReader(new FileReader("./File/users.csv"))){
                String s ;
                br.readLine();
                boolean complete = false;
                while((s = br.readLine()) != null) {
                    String arr[] = s.split(",");

                    if(usernamein.equals(arr[1])&&passwordin.equals(arr[2])){
                        dispose();                    
                        GameFrame game = new GameFrame();
                        game.setVisible(true);
                        preFrame.dispose();
                        complete = true;
                        break;
                    }
                }
                if(complete==false) System.out.println("111");
            }catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "can not read user.csv ");
                return;
            }
        }
    } 
}   