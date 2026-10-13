package Lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Toilet extends JFrame implements ActionListener {

    private Container cp;
    private JButton backBtn;
    private JFrame parentFrame;
    private Image toilet_bg;
    private CatSprite cat;
    private JLabel hintLabel;

    private static final int BATH_LEVELS = 4;   // จำนวนคลิกถูสบู่ ตามเฟรม
    private static final int SOAP_GAIN   = 5;   // ค่าความสะอาดที่ได้จากการคลิก
    private static final int RINSE_GAIN  = 30;  // ค่าที่ได้ตอนคลิกจบ

    private enum Bath { IDLE, SOAPING, FOAM_FULL, RINSING }
    private Bath bath = Bath.IDLE;
    private int soapLevel = 0;                  // 0 = ยังไม่มีฟอง

    public Toilet(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        setTitle("Toilet");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        toilet_bg = new ImageIcon(getClass().getResource("/pic/room/toilet_bg.png")).getImage();
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (toilet_bg != null) {
                    g.drawImage(toilet_bg, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        bgPanel.setLayout(null);
        bgPanel.setPreferredSize(new Dimension(400, 600));
        setContentPane(bgPanel);
        cp = getContentPane();          

        backBtn = new JButton("back");
        backBtn.setFont(new Font("Monospaced", Font.BOLD, 18));
        backBtn.setBounds(110, 525, 170, 40);
        backBtn.setBackground(Color.WHITE);
        backBtn.setFocusable(false);
        backBtn.addActionListener(this);
        cp.add(backBtn);;

        cp.add(new StatusPanel());
        
        // ข้อความ
        hintLabel = new JLabel("Click on the cat to give it a bath.", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        hintLabel.setBounds(60, 480, 280, 32);
        hintLabel.setOpaque(true);
        hintLabel.setBackground(Color.WHITE);
        hintLabel.setForeground(Color.BLACK);
        hintLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        cp.add(hintLabel);
        
        cat = new CatSprite("cat", 6, 192, 200);
        cat.setHappyAnimation("cat-happy", 3, 375);
        cat.setAnnoyedAnimation("cat-annoy", 4, 400);
        cat.setBathFrames("cat-bath", BATH_LEVELS);
        cat.setRinseAnimation("cat-rinse", 3, 400);
        cat.setLocation(104,210);
        cat.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onCatClicked();
            }
        });
        cp.add(cat);
        cat.start();

        pack();
        setLocationRelativeTo(null);
    }

    // คลิกแมว: ถูสบู่ทีละระดับ -> ฟองเต็มแล้วคลิกอีกครั้งเพื่อล้างน้ำ -> แมวดีใจ
    private void onCatClicked() {
        if (cat.isBusy() || bath == Bath.RINSING) return;   // กำลังเล่นอนิเมชันอยู่ ไม่รับคลิกซ้อน

        GameState state = GameState.get();

        if (bath == Bath.IDLE && state.getHygiene() >= GameState.MAX) {
            cat.onClicked();                                // สะอาดเต็มแล้ว ไม่ต้องอาบ (แมวทำท่าตามปกติ)
            hintLabel.setText("The cat is already clean.");
            return;
        }

        if (bath == Bath.IDLE || bath == Bath.SOAPING) {    // ถูสบู่
            soapLevel++;
            cat.showBathLevel(soapLevel - 1);
            state.addHygiene(SOAP_GAIN);
            if (soapLevel >= BATH_LEVELS) {
                bath = Bath.FOAM_FULL;
                hintLabel.setText("Full of bubbles! Click to rinse.");
            } else {
                bath = Bath.SOAPING;
                hintLabel.setText("Click (" + soapLevel + "/" + BATH_LEVELS + ")");
            }

        }  else if (bath == Bath.FOAM_FULL) {                // ล้างน้ำ
            bath = Bath.RINSING;
            hintLabel.setText("Rinsing...");
            cat.playRinse(() -> {
                state.addHygiene(RINSE_GAIN);
                cat.playHappy();
                soapLevel = 0;
                bath = Bath.IDLE;

                if (state.getHygiene() >= GameState.MAX) {
                    boolean gotReward = state.tryClaimReward("toilet", 20);
                    if (gotReward) {
                        hintLabel.setText("Clean & fresh! +20 Coins ");
                    } else {
                        hintLabel.setText("The kitty is cleaner now!");
                    }
                } else {
                    hintLabel.setText("The kitty is cleaner now!");
                }
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            cat.stop();
            this.dispose();
        }
    }
    
}