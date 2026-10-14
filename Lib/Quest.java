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

    private Timer reQuestTimer;
    private final java.util.List<Runnable> reTime = new java.util.ArrayList<>();
    private final java.util.List<JPanel> rows = new java.util.ArrayList<>();

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

        state.refreshQuests();     
        buildQuestBoxes();

        reQuestTimer = new Timer(1000, e -> {
            if (state.refreshQuests()) {
                buildQuestBoxes();
            } else {
                for (Runnable r : new java.util.ArrayList<>(reTime)) r.run();
            }
        });
        reQuestTimer.start();

        backBtn = new JButton("Back");
        backBtn.setFont(new Font("Monospaced", Font.BOLD, 18));
        backBtn.setBounds(100, 525, 200, 45);
        backBtn.setBackground(Color.WHITE);
        backBtn.setFocusable(false);
        backBtn.addActionListener(this);
        cp.add(backBtn);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildQuestBoxes() {
        for (JPanel p : rows) cp.remove(p);
        rows.clear();
        reTime.clear();

        boolean feedDone = state.getFeedCount() >= 3;
        boolean playDone = state.getPlayCount() >= 1;
        boolean shopDone = state.getShopBuyCount() >= 2;

        createQuestBox(80, "feed", "Feed the Cat",
            "Feed cat (" + state.getFeedCount() + "/3)", 15, feedDone, state.doFeedClaimed(), () -> {
                state.addCoin(15);
                state.setFeedClaimed(true);
            });

        createQuestBox(190, "play", "Play with Cat",
            "Click on cat (" + state.getPlayCount() + "/1)", 10, playDone, state.doPlayClaimed(), () -> {
                state.addCoin(10);
                state.setPlayClaimed(true);
            });

        createQuestBox(300, "shop", "Shop Food",
            "Buy from shop (" + state.getShopBuyCount() + "/2)", 20, shopDone, state.doShopClaimed(), () -> {
                state.addCoin(20);
                state.setShopClaimed(true);
            });

        cp.revalidate();
        cp.repaint();
    }

    private static String formatTime(long sec) {
        long h = sec / 3600;
        long m = (sec % 3600) / 60;
        long s = sec % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    private void createQuestBox(int y, String questKey, String questName, String desc,
                                int rewardCoins, boolean isCompleted, boolean isClaimed, Runnable onClaim) {
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
        claimBtn.setBounds(230, 18, 90, 45);
        claimBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        if (isClaimed) {
            claimBtn.setText("Claimed");
            claimBtn.setBackground(Color.GRAY);
            claimBtn.setForeground(Color.WHITE);
            claimBtn.setEnabled(false);

            JLabel cdLabel = new JLabel(formatTime(state.getQuestCDRemaining(questKey)), SwingConstants.CENTER);
            cdLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
            cdLabel.setForeground(new Color(200, 0, 0));
            cdLabel.setBounds(230, 66, 90, 18);
            rowPanel.add(cdLabel);

            reTime.add(() -> cdLabel.setText(formatTime(state.getQuestCDRemaining(questKey))));
        } else if (isCompleted) {
            claimBtn.setText("Claim");
            claimBtn.setBackground(Color.GREEN);
            claimBtn.setForeground(Color.WHITE);
            claimBtn.addActionListener(e -> {
                onClaim.run();
                JOptionPane.showMessageDialog(this, "Claimed +" + rewardCoins + " Coins!", "Success", JOptionPane.INFORMATION_MESSAGE);
                buildQuestBoxes();   
            });
        } else {
            claimBtn.setText("Doing");
            claimBtn.setBackground(Color.RED);
            claimBtn.setForeground(Color.WHITE);
            claimBtn.setEnabled(false);
        }

        rowPanel.add(claimBtn);
        cp.add(rowPanel);
        rows.add(rowPanel);
    }

    @Override
    public void dispose() {
        if (reQuestTimer != null) reQuestTimer.stop();   
        super.dispose();
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