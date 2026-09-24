import javax.swing.*;

import Lib.GameFrame;

import java.awt.*;

public class GameUi extends JPanel{
    Image bg = new ImageIcon("pic/cat_bg.jpg").getImage();

    GameUi() {
        setPreferredSize(new Dimension(400, 600));
        setLayout(null);

        JLabel l1 = new JLabel("My Little");
        JLabel l2 = new JLabel("Cat");
        l1.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        l1.setBounds(100, 150, 400, 100);
        l2.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        l2.setBounds(150, 250, 400, 100);
        l1.setForeground(Color.GREEN);
        l2.setForeground(Color.GREEN);
        
        
        JButton b = new JButton("PLAY");
        b.setBounds(125, 400, 150, 50);
        b.addActionListener(e -> {
        new GameFrame().setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    });
        add(b); add(l1); add(l2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Cat Game");
        

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setContentPane(new GameUi());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}