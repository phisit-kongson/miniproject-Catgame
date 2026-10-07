import javax.swing.*;
import java.awt.*;

public class PreGame extends JPanel {
    
    private Image logo ;
    private Image preBackground ;
    private JLabel title1, title2;
    private JButton loginBtn, signupBtn;

    public PreGame() {
        setPreferredSize(new Dimension(400, 600));
        setLayout(null);

        title1 = new JLabel("My Little");
        title1.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        title1.setBounds(100, 200, 400, 100);
        title1.setForeground(Color.BLACK);
        title2 = new JLabel("Cat");
        title2.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        title2.setBounds(160, 275, 400, 100);
        title2.setForeground(Color.BLACK);
        
        signupBtn = new JButton("SIGN UP");
        signupBtn.setBounds(85, 490, 220, 50);
        signupBtn.setFocusable(false);
        signupBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
        signupBtn.setForeground(Color.black);
        signupBtn.addActionListener(e -> {
        JFrame preFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new SignupFrame(preFrame);
        if (preFrame != null) {
        preFrame.setVisible(false);
        }
        });
        
        loginBtn = new JButton("LOG IN");
        loginBtn.setBounds(85, 400, 220, 50);
        loginBtn.setFocusable(false);
        loginBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
        loginBtn.setForeground(Color.black);
        loginBtn.addActionListener(e -> {
        JFrame preFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new LoginFrame(preFrame);
        if (preFrame != null) {
        preFrame.setVisible(false);
        }
        });

        add(signupBtn); add(loginBtn); add(title1); add(title2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        logo = new ImageIcon(getClass().getResource("/pic/background/catcenter.png")).getImage();
        preBackground = new ImageIcon(getClass().getResource("/pic/background/login_bg1.png")).getImage();
        super.paintComponent(g);
        g.drawImage(preBackground,0,0,getWidth(),getHeight(),this);
        g.drawImage(logo, 75, 15, 250, 250, this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("My Little Cat");
        Image icon = new ImageIcon(PreGame.class.getResource("/pic/background/caticon.jpg")).getImage();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(new PreGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setIconImage(icon);
        frame.setVisible(true);
    }

}   