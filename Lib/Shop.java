package Lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Shop extends JFrame implements ActionListener {

    private Container cp;
    private JButton backBtn;
    private JFrame parentFrame;
    private JLabel coinLabel;
    private final Runnable coinRefresher = this::updateCoinDisplay;
    
    private final GameState state = GameState.get();
    private Image shopBg; 

    public Shop(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        setTitle("Cat Shop");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        try {
            shopBg = new ImageIcon(getClass().getResource("/pic/background/login_bg1.png")).getImage();
        } catch (Exception e) {
            shopBg = null;
        }

        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (shopBg != null) {
                    g.drawImage(shopBg, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(180, 180, 180));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        bgPanel.setLayout(null);
        bgPanel.setPreferredSize(new Dimension(400, 600));
        setContentPane(bgPanel);
        cp = bgPanel;
 
        JLabel titleLabel = new JLabel("Cat Shop", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 22)); 
        titleLabel.setBounds(30, 20, 170, 40);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        cp.add(titleLabel);

        coinLabel = new JLabel("Coin: " + state.getCoin(), SwingConstants.CENTER);
        coinLabel.setFont(new Font("Monospaced", Font.BOLD, 17));
        coinLabel.setBounds(230, 20, 140, 40); // พิกัดขวาบน พอดีกับขอบสินค้า
        coinLabel.setOpaque(true);
        coinLabel.setBackground(Color.WHITE);
        coinLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        cp.add(coinLabel);

        state.addListener(coinRefresher);
        //  รายการสินค้าแต่ละแถว 
        createItemRow(80, "Fish", "6 Coins", "fish", 6, "/pic/icon/fish.png", 20);
        createItemRow(190, "Cat Milk", "3 Coins", "milk", 3, "/pic/icon/milk.png", 10);
        createItemRow(300, "Kibble", "8 Coins", "kibble", 8, "/pic/icon/kibble.png", 30);
        createItemRow(410, "Snack cat", "4 Coins", "snack cat", 4, "/pic/icon/snackcat.png", 15);

        //  ปุ่มย้อนกลับ (Back)
        backBtn = new JButton("back");
        backBtn.setFont(new Font("Monospaced", Font.BOLD, 18));
        backBtn.setBounds(110, 525, 170, 40);
        backBtn.setBackground(Color.WHITE);
        backBtn.setFocusable(false);
        backBtn.addActionListener(this);
        cp.add(backBtn);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void updateCoinDisplay() {
        if (coinLabel != null) {
            coinLabel.setText("Coin: " + state.getCoin());
        }
    }

    // ฟังก์ชันสร้างแถว UI สินค้า ระบบกดซื้อ
    private void createItemRow(int yPos, String itemName, String priceText, String itemKey, int price, String imagePath, int hungerGain) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(null);
        rowPanel.setBounds(30, yPos, 340, 85);
        rowPanel.setBackground(new Color(255, 255, 255, 235)); // พื้นหลังขาวโปร่งแสงเล็กน้อยเพื่อให้เห็นลายพื้นหลังร้าน
        rowPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        // ชื่อสินค้า
        JLabel nameLabel = new JLabel(itemName);
        nameLabel.setFont(new Font("Monospaced", Font.BOLD, 17));
        nameLabel.setBounds(75, 8, 150, 25);
        rowPanel.add(nameLabel);

        // รูปภาพอาหาร
        JLabel imgLabel = new JLabel("", SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imgLabel.setText(""); // ไอคอนสำรองกรณีหาไฟล์ภาพไม่เจอ
        }
        imgLabel.setBounds(15, 27, 45, 45);
        rowPanel.add(imgLabel);

        JLabel hungerLabel = new JLabel("+ " + hungerGain + " Hunger");
        hungerLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
        hungerLabel.setForeground(new Color(230, 100, 30)); 
        hungerLabel.setBounds(75, 32, 150, 20);
        rowPanel.add(hungerLabel);

        // ราคา
        JLabel priceLabel = new JLabel(" " + priceText);
        priceLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
        priceLabel.setBounds(75, 54, 150, 25);
        rowPanel.add(priceLabel);

        // ปุ่ม Buy 
        JButton buyBtn = new JButton("Buy");
        buyBtn.setFont(new Font("Monospaced", Font.BOLD, 16));
        buyBtn.setForeground(Color.WHITE);
        buyBtn.setBackground(new Color(51, 122, 183));
        buyBtn.setBounds(230, 22, 90, 50);
        buyBtn.setFocusable(false);
        buyBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        // เมื่อกดซื้อสินค้า
        buyBtn.addActionListener(e -> {
            if (state.trySpend(price)) {
                state.addItem(itemKey, 1); // เพิ่มไอเทมเข้ากระเป๋า
                GameState.get().recordShopBuy();//ตัวนับเควส ว่ามีของเข้ากะเป๋าไหม
            } else {
                JOptionPane.showMessageDialog(this, "Not enough coins!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        rowPanel.add(buyBtn);
        cp.add(rowPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            state.removeListener(coinRefresher);
            if (parentFrame != null) {
                parentFrame.setVisible(true); 
            }
            this.dispose();
        }
    }
}