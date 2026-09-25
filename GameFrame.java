import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Lib.*;

public class GameFrame extends JFrame implements ActionListener{

    Container cp;
    JButton shopbtn, feedbtn, sleepbtn, toiletbtn, lobbybtn;
    Image background;

    public GameFrame() {
        
        Initial();
        setComponent();
        Finally();
            
    }

    public void Initial(){
       
        try {
            background = new ImageIcon(getClass().getResource("pic/room.jpg")).getImage();
        } catch (Exception e) {
            background = null;
        }

        
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (background != null) {
                
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        bgPanel.setLayout(null);

        this.setContentPane(bgPanel);
        cp = this.getContentPane();
    }

    public void setComponent(){
        int wbtn = 70;
        int hbtn = 50;
        
        ImageIcon shopIcon = null;
        Image imgshop = new ImageIcon(getClass().getResource("/pic/shop.png")).getImage();
        Image scaledImg = imgshop.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        shopIcon = new ImageIcon(scaledImg);
        shopbtn = new JButton();
        shopbtn.setIcon(shopIcon);
        shopbtn.setBounds(20, 500, wbtn, hbtn);
        shopbtn.setFocusable(false);
        shopbtn.addActionListener(this);
        cp.add(shopbtn);

        feedbtn = new JButton("");
        feedbtn.setBounds(120, 500, wbtn, hbtn);
        feedbtn.addActionListener(this);
        cp.add(feedbtn);

        sleepbtn = new JButton("");
        sleepbtn.setBounds(220, 500, wbtn, hbtn);
        sleepbtn.addActionListener(this);
        cp.add(sleepbtn);

        toiletbtn = new JButton("");
        toiletbtn.setBounds(320, 500, wbtn, hbtn);
        toiletbtn.addActionListener(this);
        cp.add(toiletbtn);
        
    }

    public void Finally(){
        setTitle("My Little Cat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(200, 230, 255));
        getContentPane().setPreferredSize(new Dimension(400, 600));
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == shopbtn) {
            
            Shop shopWindow = new Shop(this);
            shopWindow.setVisible(true);
            this.setVisible(false);
            
    }
    }
}