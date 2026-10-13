package Lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sleep extends JFrame implements ActionListener {

    private Container cp;
    private JButton backBtn;
    private JFrame parentFrame;
    private CatSprite cat;
    private Image bedroom;
    private JLabel hintLabel;

    private final GameState state = GameState.get();   
    private Timer energyTimer;                         

    public Sleep(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        setTitle("Bedroom");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        bedroom = new ImageIcon(getClass().getResource("/pic/room/bedroom2.png")).getImage();
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bedroom != null) {
                    g.drawImage(bedroom, 0, 0, getWidth(), getHeight(), this);
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
        cp.add(backBtn);

        cp.add(new StatusPanel());

        hintLabel = new JLabel("Click on the cat to sleep.", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        hintLabel.setBounds(60, 480, 280, 32);
        hintLabel.setOpaque(true);
        hintLabel.setBackground(Color.WHITE);
        hintLabel.setForeground(Color.BLACK);
        hintLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        cp.add(hintLabel);

        cat = new CatSprite("cat", 6, 192, 200);
        cat.setLocation(104, 240);
        cp.add(cat);
        cat.setSleepAnimation("cat-sleep", 4, 600);       
        energyTimer = new Timer(500, e -> {           
            state.addEnergy(3);
            if (state.getEnergy() >= GameState.MAX) {  
                wakeCat();
                boolean gotMoney = state.tryClaimReward("sleep", 20);
                if (gotMoney) {
                   hintLabel.setText("The cat is awake! +20 coin (cd 30s)");
                }else {
                    hintLabel.setText("The cat is awake!");
                }
            }
        });

        cat.addMouseListener(new MouseAdapter() {      // กดแมวแล้วนอน
            @Override
            public void mouseClicked(MouseEvent e) {
                if (cat.isSleeping()) wakeCat();
                else sleepCat();
            }
        });
        
        addWindowListener(new WindowAdapter() {        // ปิดห้องหยุด Timer
            @Override
            public void windowClosed(WindowEvent e) {
                energyTimer.stop();
            }
        });
        
        cat.start();
        pack();
        setLocationRelativeTo(null);
    }

    private void sleepCat() {                          
        cat.sleep();
        cat.setLocation(104, 240 + 40);
        hintLabel.setText("The cat is sleeping.");
        energyTimer.start();
    }

    private void wakeCat() {                           
        energyTimer.stop();
        cat.wake();
        cat.setLocation(104, 240);
        hintLabel.setText("The cat is awake!");
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