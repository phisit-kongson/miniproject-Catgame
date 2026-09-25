package Lib;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("My Little Cat");
        setSize(400, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // โหลด Panel หลักของเกม
        setContentPane(new MainGamePanel());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameFrame().setVisible(true));
    }
}

class MainGamePanel extends JPanel {
    
    private Image bg;
    private Image catImage;

    
    private int coins = 999999;
    private int happyPercent = 50;   
    private int foodPercent = 50;    
    private int showerPercent = 50;  
    private int sleepPercent = 50;   

    public MainGamePanel() {
        setLayout(null);
        setPreferredSize(new Dimension(400, 650));

        
        try {
    bg = new ImageIcon("pic/room.jpg").getImage();
    catImage = new ImageIcon("pic/caticon.jpg").getImage();
    } catch (Exception e) {
    e.printStackTrace();
    }


        initUI();
    }

    private void initUI() {

        // กล่องแสดงจำนวนเหรียญ 
JPanel coinPanel = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // สี่เหลี่ยมตรงเงิน 
        g2.setColor(new Color(240, 240, 240, 230));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
        g2.setColor(new Color(210, 210, 210));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);

        // ไอคอนเหรียญสีทอง 
        int coinSize = 20;
        int coinX = getWidth() - coinSize - 4;
        int coinY = (getHeight() - coinSize) / 2;

        g2.setColor(new Color(255, 193, 7));
        g2.fillOval(coinX, coinY, coinSize, coinSize);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("$", coinX + 6, coinY + 15);

        g2.dispose();
    }
};

    coinPanel.setOpaque(false);

    
    coinPanel.setBounds(15, 15, 140, 38); 
    coinPanel.setLayout(new BorderLayout());    

    JLabel coinText = new JLabel(String.valueOf(coins), SwingConstants.LEFT);
    
    coinText.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
    coinText.setFont(new Font("SansSerif", Font.BOLD, 16));

    coinPanel.add(coinText, BorderLayout.CENTER);
    add(coinPanel);



        
        // ปุ่มสถานะด้านล่าง 4 ปุ่ม 
        
        JButton happyBtn = createStatusButton("", happyPercent, new Color(255, 215, 0));
        happyBtn.setBounds(20, 535, 75, 60);

        JButton foodBtn = createStatusButton("", foodPercent, new Color(139, 195, 74));
        foodBtn.setBounds(110, 535, 75, 60);

        JButton showerBtn = createStatusButton("", showerPercent, new Color(33, 150, 243));
        showerBtn.setBounds(195, 535, 75, 60);

        JButton sleepBtn = createStatusButton("", sleepPercent, new Color(156, 39, 176));
        sleepBtn.setBounds(280, 535, 75, 60);

        add(happyBtn);
        add(foodBtn);
        add(showerBtn);
        add(sleepBtn);
    }


    //  สร้างปุ่มเมนูด้านล่างพร้อมหลอดเติมสีสถานะตาม %
    private JButton createStatusButton(String iconText, int percentage, Color fillColor) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // พื้นหลังขาว
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, w, h, 25, 25);

                // เติมสีสถานะจากล่างขึ้นบนตาม %
                int fillHeight = (int) (h * (percentage / 100.0));
                g2.setColor(fillColor);
                g2.setClip(new RoundRectangle2D.Float(0, 0, w, h, 25, 25));
                g2.fillRect(0, h - fillHeight, w, fillHeight);
                g2.setClip(null);

                // ขอบปุ่ม
                g2.setColor(Color.DARK_GRAY);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 25, 25);

            }
        };
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }

   @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 1. วาดรูปพื้นหลัง 
        if (bg != null) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
        
        // 2. วาดรูปแมวไว้ตรงกลางหน้าจอ
        if (catImage != null) {
            g.drawImage(catImage, 100, 180, 200, 200, this);
        }
    }
    }