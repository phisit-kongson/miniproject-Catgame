import javax.swing.*;
import java.awt.*;

public class PreGame extends JPanel{
    
    Image bg1 = new ImageIcon(getClass().getResource("pic/catback.jpg")).getImage();
    Image bg2 = new ImageIcon(getClass().getResource("pic/catcenter.png")).getImage();
    Image bg3 = new ImageIcon(getClass().getResource("pic/caticon.jpg")).getImage();
    Image bg4 = new ImageIcon(getClass().getResource("pic/ya.png")).getImage();
    Image bg5 = new ImageIcon(getClass().getResource("pic/mek.png")).getImage();
    Image bg6 = new ImageIcon(getClass().getResource("pic/mek.png")).getImage();

    public PreGame() {
        setPreferredSize(new Dimension(400, 600));
        setLayout(null);

        JLabel l1 = new JLabel("My Little");
        JLabel l2 = new JLabel("Cat");
        l1.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        l1.setBounds(100, 200, 400, 100);
        l2.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        l2.setBounds(160, 275, 400, 100);
        l1.setForeground(Color.GREEN);
        l2.setForeground(Color.GREEN);
        
        
        JButton b = new JButton("PLAY");
        //JButton b = new JButton(new ImageIcon(getClass().getResource("pic/play.jpg")));
        b.setBounds(85, 490, 220, 50);
        b.setFont(new Font("Comic Sans MS", Font.BOLD, 45));
        b.setForeground(Color.black);
        b.addActionListener(e -> {
        new GameFrame().setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
        
        
        
    });
        add(b); add(l1); add(l2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg1, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(bg2, 75, 15, 250, 250, this);
        g.drawImage(bg4, 0, 430, 450, 250, this);
        g.drawImage(bg5, 0, 20, 100, 100, this);
        g.drawImage(bg6, 300, 20, 100, 100, this);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("My Little Cat");
        

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setContentPane(new PreGame());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
        Image t = new ImageIcon(PreGame.class.getResource("pic/caticon.jpg")).getImage();
        f.setIconImage(t);
    }
    
}   