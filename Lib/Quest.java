package Lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Quest extends JFrame implements ActionListener {

    private Container cp;
    private JButton backBtn;
    private JFrame parentFrame;
    private GameState state = GameState.get();

    private Image questBg;

    public Quest(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        setTitle("Quests");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            questBg = new ImageIcon(getClass().getResource("/pic/background/login_bg1.png")).getImage();
        } catch (Exception e) {
            questBg = null;
        }

        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (questBg != null) {
                    g.drawImage(questBg, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        bgPanel.setLayout(null);
        bgPanel.setPreferredSize(new Dimension(400, 600));
        setContentPane(bgPanel);
        cp = bgPanel;

        JLabel titleLabel = new JLabel("Daily Quests", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 26));
        titleLabel.setBounds(60, 20, 280, 50);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        cp.add(titleLabel);

        // เช็กเงื่อนไขความสำเร็จ
        boolean feedDone = state.getFeedCount() >= 3;
        boolean playDone = state.getPlayCount() >= 1;
        boolean shopDone = state.getShopBuyCount() >= 2;

        // 1. เควส Feed
        createQuestBox(80, "Feed the Cat", 
            "Feed cat (" + state.getFeedCount() + "/3)", 15, feedDone, state.doFeedClaimed(), () -> {
                state.addCoin(15);
                state.setFeedClaimed(true);
            });

        // 2. เควส Play
        createQuestBox(190, "Play with Cat", 
            "Click on cat (" + state.getPlayCount() + "/1)", 10, playDone, state.doPlayClaimed(), () -> {
                state.addCoin(10);
                state.setPlayClaimed(true);
            });

        //เควส Shop
        createQuestBox(300, "Shop Food", 
            "Buy from shop (" + state.getShopBuyCount() + "/2)", 20, shopDone, state.doShopClaimed(), () -> {
                state.addCoin(20);
                state.setShopClaimed(true);
            });

        backBtn = new JButton("< Back");
        backBtn.setFont(new Font("Monospaced", Font.BOLD, 18));
        backBtn.setBounds(100, 525, 200, 45);
        backBtn.setBackground(new Color(240, 240, 240));
        backBtn.setFocusable(false);
        backBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        backBtn.addActionListener(this);
        cp.add(backBtn);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // เพิ่มพารามิเตอร์ isClaimed เข้ามาเช็กสถานะ
    private void createQuestBox(int y, String questName, String desc, int rewardCoins, boolean isCompleted, boolean isClaimed, Runnable onClaim) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(null);
        rowPanel.setBounds(30, y, 340, 95);
        rowPanel.setBackground(new Color(255, 255, 255, 235));
        rowPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        JLabel nameLabel = new JLabel(questName);
        nameLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        nameLabel.setBounds(15, 10, 200, 22);
        rowPanel.add(nameLabel);

        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        descLabel.setBounds(15, 32, 210, 20);
        rowPanel.add(descLabel);

        JLabel rewardLabel = new JLabel("Reward: +" + rewardCoins + " Coins");
        rewardLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
        rewardLabel.setForeground(new Color(180, 100, 0));
        rewardLabel.setBounds(15, 57, 200, 22);
        rowPanel.add(rewardLabel);

        JButton claimBtn = new JButton();
        claimBtn.setFont(new Font("Monospaced", Font.BOLD, 14));
        claimBtn.setFocusable(false);
        claimBtn.setBounds(230, 22, 90, 50);
        claimBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // ตรวจสอบลำดับสถานะ: ถ้าเคลมแล้ว -> ถ้าทำเสร็จแล้วแต่ยังไม่เคลม -> ถ้ายังไม่เสร็จ
        if (isClaimed) {
            claimBtn.setText("Claimed");
            claimBtn.setBackground(Color.GRAY);
            claimBtn.setForeground(Color.WHITE);
            claimBtn.setEnabled(false);
        } else if (isCompleted) {
            claimBtn.setText("Claim");
            claimBtn.setBackground(Color.GREEN);
            claimBtn.setForeground(Color.WHITE);
            claimBtn.addActionListener(e -> {
                onClaim.run(); // เรียกฟังก์ชันบวกเหรียญและตั้งค่า set...Claimed(true)
                JOptionPane.showMessageDialog(this, "Claimed +" + rewardCoins + " Coins!", "Success", JOptionPane.INFORMATION_MESSAGE);
                claimBtn.setText("Claimed");
                claimBtn.setEnabled(false);
                claimBtn.setBackground(Color.GRAY);
            });
        } else {
            claimBtn.setText("Doing");
            claimBtn.setBackground(Color.RED);
            claimBtn.setForeground(Color.WHITE);
            claimBtn.setEnabled(false);
        }

        rowPanel.add(claimBtn);
        cp.add(rowPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            this.dispose();
        }
    }
}