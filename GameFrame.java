import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Lib.*;


public class GameFrame extends JFrame implements ActionListener{
    
    private Container cp;
    private JButton shopbtn, feedbtn, sleepbtn, toiletbtn, lobbybtn;
    private Image background;
    private int coin ; 
    private JLabel coinBox;
    private CatSprite cat;
    private JProgressBar hungerBar, energyBar, hygieneBar;
    private Timer decayTimer;
    
    public GameFrame() {
        
        initBackground();
        setComponent();
        Finally();
           
    }
    
    public void initBackground(){
       
        try {
            background = new ImageIcon(getClass().getResource("/pic/room/room_morning.png")).getImage();
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
        Image imgshop = new ImageIcon(getClass().getResource("/pic/icon/shop.png")).getImage();
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
        Image imgfeed = new ImageIcon(getClass().getResource("/pic/icon/feed.png")).getImage();
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
        Image imgsleep = new ImageIcon(getClass().getResource("/pic/icon/moons.png")).getImage();
        Image sleepImg = imgsleep.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        sleepIcon = new ImageIcon(sleepImg);
        sleepbtn = new JButton();
        sleepbtn.setIcon(sleepIcon);
        sleepbtn.setBounds(220, 500, wbtn, hbtn);
        sleepbtn.setFocusable(false);
        sleepbtn.addActionListener(this);
        cp.add(sleepbtn);

        //ปุ่ม ห้องน้ำ
        ImageIcon toiletIcon = null;
        Image imgtoilet = new ImageIcon(getClass().getResource("/pic/icon/toilet.png")).getImage();
        Image toiletImg = imgtoilet.getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        toiletIcon = new ImageIcon(toiletImg);
        toiletbtn = new JButton();
        toiletbtn.setIcon(toiletIcon);
        toiletbtn.setBounds(320, 500, wbtn, hbtn);
        toiletbtn.setFocusable(false);
        toiletbtn.addActionListener(this);
        cp.add(toiletbtn);

        //ปุ่ม กลับล็อบบี้
        ImageIcon lobbyIcon = null;
        Image imglobby = new ImageIcon(getClass().getResource("/pic/icon/home.png")).getImage();
        Image lobbyImg = imglobby.getScaledInstance(66, 65, Image.SCALE_SMOOTH);
        lobbyIcon = new ImageIcon(lobbyImg);
        lobbybtn = new JButton();
        lobbybtn.setIcon(lobbyIcon);
        lobbybtn.setBounds(320, 5, wbtn, hbtn);
        lobbybtn.setFocusable(false);
        lobbybtn.addActionListener(this);
        cp.add(lobbybtn);

        //บล็อคแสดงcoin
        coin = 100;
        coinBox = new JLabel("coin : " + coin, SwingConstants.CENTER);
        coinBox.setFont(new Font("", Font.BOLD, 16));
        coinBox.setBounds(15, 15, 100, 35); 
        coinBox.setOpaque(true);                                
        coinBox.setBackground(Color.WHITE);                     
        coinBox.setForeground(Color.BLACK);                     
        coinBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); 
        cp.add(coinBox);

        UIManager.put("ProgressBar.selectionBackground", Color.BLACK);
        UIManager.put("ProgressBar.selectionForeground", Color.BLACK);

        //หลอดค่าพลังงาน
        energyBar = new JProgressBar(0, 100);
        energyBar.setValue(80); //กำหนดค่าเริ่มต้น
        energyBar.setStringPainted(true); //เเสดงเปอร์เซ็น
        energyBar.setFont(new Font("Tahoma",Font.CENTER_BASELINE,14));
        energyBar.setForeground(new Color(36, 149, 255));
        energyBar.setBounds(20, 65, 100, 15);
        add(energyBar);
        
        //หลอดค่าความหิว
        hungerBar = new JProgressBar(0, 100); 
        hungerBar.setValue(50);               
        hungerBar.setStringPainted(true);     
        hungerBar.setFont(new Font("Tahoma",Font.CENTER_BASELINE,14));
        hungerBar.setForeground(new Color(240, 129, 60)); 
        hungerBar.setBounds(20, 90, 100, 15); 
        add(hungerBar);

        //หลอดความสะอาด
        hygieneBar = new JProgressBar(0,100);
        hygieneBar.setValue(100);
        hygieneBar.setStringPainted(true);
        hygieneBar.setFont(new Font("Tahoma",Font.CENTER_BASELINE,14));
        hygieneBar.setForeground(new Color(51, 222, 205));
        hygieneBar.setBounds(20,115,100,15);
        add(hygieneBar);

        cat = new CatSprite("cat", 6, 192, 200);
        cat.setHappyAnimation("cat-happy", 3, 375); 
        cat.setLocation((400 - 192) / 2, (600 - 192) / 2);
        cat.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
        cat.playHappy();             
        }
        });

        cp.add(cat);
        cat.start();
        startStatusDecay();
    }

    public void startStatusDecay() {
        decayTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                energyBar.setValue(Math.max(0, energyBar.getValue() - 1));
                hungerBar.setValue(Math.max(0, hungerBar.getValue() - 2));
                hygieneBar.setValue(Math.max(0, hygieneBar.getValue() - 1));
            }
        });
        decayTimer.start();
    }

    public void Finally(){
        setTitle("My Little Cat");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setPreferredSize(new Dimension(400, 600));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == shopbtn) {
            Shop shopWindow = new Shop(this);
            shopWindow.setVisible(true);
            this.setVisible(false);
            
        }else if (e.getSource() == feedbtn) {
            Feed feedroom = new Feed(this);
            feedroom.setVisible(true);
            this.setVisible(false);

        }else if (e.getSource() == sleepbtn) {
            Sleep bedroom = new Sleep(this);
            bedroom.setVisible(true);
            this.setVisible(false);


        }else if (e.getSource() == toiletbtn) {
            Toilet toiletroom = new Toilet(this);
            toiletroom.setVisible(true);
            this.setVisible(false);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameFrame();
        });
    }

}