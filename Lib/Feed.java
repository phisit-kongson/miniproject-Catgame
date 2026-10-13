package Lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Feed extends JFrame implements ActionListener {

    private Container cp;
    private JButton backBtn;
    private JButton milkBtn, fishBtn, kibbleBtn, snackBtn;
    private JFrame parentFrame;
    private Image backGround;
    private CatSprite cat;

    private final GameState state = GameState.get();
    private final Runnable refresher = this::updateCounts;
    private static final int FULL_LIMIT    = 100;   
    private static final int VOMIT_HUNGER  = 20;   
    
    public Feed(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        setTitle("Feed");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        backGround = new ImageIcon(getClass().getResource("/pic/room/room_morning.png")).getImage();
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backGround != null) {
                    g.drawImage(backGround, 0, 0, getWidth(), getHeight(), this);
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

        milkBtn = new JButton("Milk");
        milkBtn.setBounds(22, 450, 70, 56);
        milkBtn.setFocusable(false);
        milkBtn.setMargin(new Insets(0, 0, 0, 0));
        milkBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        milkBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        milkBtn.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/pic/icon/milk.png"))
        .getImage().getScaledInstance(32, 32, Image.SCALE_REPLICATE)));
        milkBtn.addActionListener(this);
        cp.add(milkBtn);

        fishBtn = new JButton("Fish");
        fishBtn.setBounds(122, 450, 70, 56);
        fishBtn.setFocusable(false);
        fishBtn.setMargin(new Insets(0, 0, 0, 0));
        fishBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        fishBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        fishBtn.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/pic/icon/fish.png"))
        .getImage().getScaledInstance(32, 32, Image.SCALE_REPLICATE)));
        fishBtn.addActionListener(this);
        cp.add(fishBtn); 

        kibbleBtn = new JButton("Kibble");
        kibbleBtn.setBounds(222, 450, 70, 56);
        kibbleBtn.setFocusable(false);
        kibbleBtn.setMargin(new Insets(0, 0, 0, 0));
        kibbleBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        kibbleBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        kibbleBtn.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/pic/icon/kibble.png"))
        .getImage().getScaledInstance(32, 32, Image.SCALE_REPLICATE)));
        kibbleBtn.addActionListener(this);
        cp.add(kibbleBtn);

        snackBtn = new JButton("Snack");
        snackBtn.setBounds(322, 450, 70, 56);
        snackBtn.setFocusable(false);
        snackBtn.setMargin(new Insets(0, 0, 0, 0));
        snackBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        snackBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        snackBtn.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/pic/icon/snackcat.png"))
        .getImage().getScaledInstance(32, 32, Image.SCALE_REPLICATE)));
        snackBtn.addActionListener(this);
        cp.add(snackBtn);
        
        cp.add(new StatusPanel());
        cat = new CatSprite("cat", 6, 192, 200);
        cat.setLocation(104,210);
        cat.setEatAnimation("cat-eat", 4, 600);
        cat.setVomitAnimation("cat-vomit", 3, 600);
        updateCounts();
        state.addListener(refresher);
        addWindowListener(new WindowAdapter() {
    @Override
    public void windowClosed(WindowEvent e) {
        state.removeListener(refresher);
    }
    });
        cp.add(cat);
        cat.start();
        GameState.get().startDecay();
        pack();
        setLocationRelativeTo(null);
    }
    private void updateCounts() {
        setCount(milkBtn, "milk");
        setCount(fishBtn, "fish");
        setCount(kibbleBtn, "kibble");
        setCount(snackBtn, "snack cat");
    }
    private void setCount(JButton b, String itemKey) {
        int n = state.getItemCount(itemKey);
        b.setText("x" + n);
        b.setEnabled(n > 0);      
    }
    private void feed(String itemKey, int hungerGain) {
        if (cat.isBusy()) return;                 
        if (!state.useItem(itemKey)) return;      

        if (state.getHunger() >= FULL_LIMIT) {    // อิ่มแล้วยังป้อน -> อ้วก
            cat.playVomit(() -> {
                state.addHunger(-VOMIT_HUNGER);
            });
        } else {                                  // ยังหิวอยู่ -> กินปกติ
            cat.playEat(() -> state.addHunger(hungerGain));
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            if (parentFrame != null) {
                parentFrame.setVisible(true); 
            }
            this.dispose();
        } else if (e.getSource() == milkBtn) {
            feed("milk", 10);
        } else if (e.getSource() == fishBtn) {
            feed("fish", 25);
        } else if (e.getSource() == kibbleBtn) {
            feed("kibble", 35);
        } else if (e.getSource() == snackBtn) {
            feed("snack cat", 15);
        }
    }
}