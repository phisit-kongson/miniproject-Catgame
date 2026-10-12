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
        backBtn.setBounds(130, 515, 140, 40);
        backBtn.setFocusable(false);
        backBtn.addActionListener(this);
        cp.add(backBtn);

        cp.add(new StatusPanel());  

        cat = new CatSprite("cat", 6, 192, 200);
        cat.setLocation(104, 240);
        cp.add(cat);
        cat.setSleepAnimation("cat-sleep", 4, 600);       
        energyTimer = new Timer(500, e -> {           
            state.addEnergy(3);
            if (state.getEnergy() >= GameState.MAX) {  //เต็มแล้วแมวตื่นเอง
                wakeCat();
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
        energyTimer.start();
    }

    private void wakeCat() {                           
        energyTimer.stop();
        cat.wake();
        cat.setLocation(104, 240);
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