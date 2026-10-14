import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Lib.*;

public class GameFrame extends JFrame implements ActionListener {
    
    private Container cp;
    private JButton shopbtn, feedbtn, sleepbtn, toiletbtn, lobbybtn;
    private Image background;
    private JLabel hintLabel;
    private CatSprite cat;
    private final Runnable statusListener = this::updateHintByStatus;
    
    public GameFrame() {
        initBackground();
        setComponent();
        Finally();
    }
    
    public void initBackground() {
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

    public void setComponent() {
        int wbtn = 70;
        int hbtn = 50;
        
        // ปุ่ม shop
        Image imgshop = new ImageIcon(getClass().getResource("/pic/icon/shop.png")).getImage();
        Image scaledImg = imgshop.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        shopbtn = new JButton(new ImageIcon(scaledImg));
        shopbtn.setBounds(20, 500, wbtn, hbtn);
        shopbtn.setFocusable(false);
        shopbtn.setBackground(Color.WHITE);
        shopbtn.addActionListener(this);
        cp.add(shopbtn);

        // ปุ่ม ให้อาหาร
        Image imgfeed = new ImageIcon(getClass().getResource("/pic/icon/feed.png")).getImage();
        Image feedImg = imgfeed.getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        feedbtn = new JButton(new ImageIcon(feedImg));
        feedbtn.setBounds(120, 500, wbtn, hbtn);
        feedbtn.setFocusable(false);
        feedbtn.setBackground(Color.WHITE);
        feedbtn.addActionListener(this);
        cp.add(feedbtn);

        // ปุ่ม นอน
        Image imgsleep = new ImageIcon(getClass().getResource("/pic/icon/moons.png")).getImage();
        Image sleepImg = imgsleep.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        sleepbtn = new JButton(new ImageIcon(sleepImg));
        sleepbtn.setBounds(220, 500, wbtn, hbtn);
        sleepbtn.setFocusable(false);
        sleepbtn.setBackground(Color.WHITE);
        sleepbtn.addActionListener(this);
        cp.add(sleepbtn);

        // ปุ่ม ห้องน้ำ
        Image imgtoilet = new ImageIcon(getClass().getResource("/pic/icon/toilet.png")).getImage();
        Image toiletImg = imgtoilet.getScaledInstance(65, 65, Image.SCALE_SMOOTH);
        toiletbtn = new JButton(new ImageIcon(toiletImg));
        toiletbtn.setBounds(320, 500, wbtn, hbtn);
        toiletbtn.setFocusable(false);
        toiletbtn.setBackground(Color.WHITE);
        toiletbtn.addActionListener(this);
        cp.add(toiletbtn);

        // ปุ่ม กลับล็อบบี้
        Image imglobby = new ImageIcon(getClass().getResource("/pic/icon/home.png")).getImage();
        Image lobbyImg = imglobby.getScaledInstance(66, 65, Image.SCALE_SMOOTH);
        lobbybtn = new JButton(new ImageIcon(lobbyImg));
        lobbybtn.setBounds(320, 5, wbtn, hbtn);
        lobbybtn.setFocusable(false);
        lobbybtn.setBackground(Color.WHITE);
        lobbybtn.addActionListener(this);
        cp.add(lobbybtn);

        hintLabel = new JLabel("Meow Meow", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        hintLabel.setBounds(60, 450, 280, 32);
        hintLabel.setOpaque(true);
        hintLabel.setBackground(Color.WHITE);
        hintLabel.setForeground(Color.BLACK);
        hintLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        cp.add(hintLabel);

        cp.add(new StatusPanel());

        cat = new CatSprite("cat", 6, 192, 200);
        cat.setHappyAnimation("cat-happy", 3, 425);
        cat.setAnnoyedAnimation("cat-annoy", 4, 400);
        cat.setLocation(104, 210);
        cat.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (cat.isBusy()) return;

                cat.onClicked();
                boolean gotMoney = GameState.get().tryClaimReward("pet", 10);

                hintLabel.setForeground(Color.BLACK);
                if(cat.isAnnoyedNow()) {
                    hintLabel.setText("Annoyed!!!!");
                }else if (gotMoney) {
                    hintLabel.setText("Happy~ +10 Coins(cd 30s)");
                } else {
                    hintLabel.setText("Happy! Meow~");
                }

                // รอให้อนิเมชันเล่นจบ (ประมาณ 1.3 วินาที) แล้วเปลี่ยนกลับเป็นข้อความตามสถานะ
                Timer resetHintTimer = new Timer(1300, evt -> updateHintByStatus());
                resetHintTimer.setRepeats(false);
                resetHintTimer.start();
            }
        });

        cp.add(cat);
        cat.start();
        GameState.get().startDecay();

        // ผูก Listener คอยเช็กสถานะตลอดเวลา
        GameState.get().addListener(statusListener);
        updateHintByStatus();
    }

    private void updateHintByStatus() {
        // ถ้าแมวกำลังเล่นท่าตอนถูกคลิกอยู่ ให้โชว์ข้อความท่านั้นไปก่อนจนจบ
        if (hintLabel == null || (cat != null && cat.isBusy())) return;

        GameState state = GameState.get();
        if (state.getHunger() < 20) {
            hintLabel.setText("Very Hungry! Feed me please!");
        } else if (state.getEnergy() < 20) {
            hintLabel.setText("Very Sleepy... Need some rest!");
        } else if (state.getHygiene() < 20) {
            hintLabel.setText("So Dirty! Take me to the bath!");
        } else {
            hintLabel.setText("Meow Meow");
        }
    }

    public void Finally() {
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
            
        } else if (e.getSource() == feedbtn) {
            Feed feedroom = new Feed(this);
            feedroom.setVisible(true);
            this.setVisible(false);

        } else if (e.getSource() == sleepbtn) {
            Sleep bedroom = new Sleep(this);
            bedroom.setVisible(true);
            this.setVisible(false);

        } else if (e.getSource() == toiletbtn) {
            Toilet toiletroom = new Toilet(this);
            toiletroom.setVisible(true);
            this.setVisible(false);

        } else if (e.getSource() == lobbybtn) {
            GameState.get().removeListener(statusListener);
            GameState.get().stopDecay();
            if (cat != null) {
                cat.stop();
            }
            PreGame.main(null);
            this.dispose();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameFrame();
        });
    }
}