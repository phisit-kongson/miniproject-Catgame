import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Lib.*;

public class GameFrame extends JFrame implements ActionListener{

    Container cp;
    JButton shopbtn, feedbtn, sleepbtn, toiletbtn, lobbybtn;
    Image background;
    private int coin = 10; 
    private JLabel coinBox;

    public GameFrame() {
        
        Initial();
        setComponent();
        Finally();
            
    }

    public void Initial(){
       
        try {
            background = new ImageIcon(getClass().getResource("/pic/room2.png")).getImage();
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
        
        //ปุ่ม shop
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

        //ปุ่ม ให้อาหาร
        ImageIcon feedIcon = null;
        Image imgfeed = new ImageIcon(getClass().getResource("/pic/feed.png")).getImage();
        Image feedImg = imgfeed.getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        feedIcon = new ImageIcon(feedImg);
        feedbtn = new JButton();
        feedbtn.setIcon(feedIcon);
        feedbtn.setBounds(120, 500, wbtn, hbtn);
        feedbtn.setFocusable(false);
        feedbtn.addActionListener(this);
        cp.add(feedbtn);

        //ปุ่ม นอน
        ImageIcon sleepIcon = null;
        Image imgsleep = new ImageIcon(getClass().getResource("/pic/moons.png")).getImage();
        Image sleepImg = imgsleep.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        sleepIcon = new ImageIcon(sleepImg);
        sleepbtn = new JButton();
        sleepbtn.setIcon(sleepIcon);
        sleepbtn.setBounds(220, 500, wbtn, hbtn);
        sleepbtn.addActionListener(this);
        cp.add(sleepbtn);

        //ปุ่ม ห้องน้ำ
        ImageIcon toiletIcon = null;
        Image imgtoilet = new ImageIcon(getClass().getResource("/pic/toilet.png")).getImage();
        Image toiletImg = imgtoilet.getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        toiletIcon = new ImageIcon(toiletImg);
        toiletbtn = new JButton();
        toiletbtn.setIcon(toiletIcon);
        toiletbtn.setBounds(320, 500, wbtn, hbtn);
        toiletbtn.addActionListener(this);
        cp.add(toiletbtn);

        //ปุ่ม กลับล็อบบี้
        ImageIcon lobbyIcon = null;
        Image imglobby = new ImageIcon(getClass().getResource("/pic/home.png")).getImage();
        Image lobbyImg = imglobby.getScaledInstance(66, 65, Image.SCALE_SMOOTH);
        lobbyIcon = new ImageIcon(lobbyImg);
        lobbybtn = new JButton();
        lobbybtn.setIcon(lobbyIcon);
        lobbybtn.setBounds(320, 5, wbtn, hbtn);
        lobbybtn.addActionListener(this);
        cp.add(lobbybtn);

        //บล็อคแสดงcoin
        coinBox = new JLabel("Coin: " + coin, SwingConstants.CENTER);
        coinBox.setFont(new Font("", Font.BOLD, 16));
        coinBox.setBounds(15, 15, 100, 35); 
        coinBox.setOpaque(true);                                
        coinBox.setBackground(Color.WHITE);                     
        coinBox.setForeground(Color.BLACK);                     
        coinBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); 
        cp.add(coinBox);
        
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