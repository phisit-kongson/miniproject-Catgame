package Lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Shop extends JFrame implements ActionListener {

    private Container cp;
    private JButton backBtn;
    private JFrame parentFrame; 
    
    public Shop(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        setTitle("Cat Shop");
        setSize(400, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        cp = getContentPane();
        cp.setLayout(null);
        cp.setBackground(Color.GRAY);

        JLabel titleLabel = new JLabel("Cat Shop", SwingConstants.CENTER);
        titleLabel.setBounds(50, 30, 300, 40);
        cp.add(titleLabel);

        backBtn = new JButton("return");
        backBtn.setBounds(130, 500, 140, 40);
        backBtn.addActionListener(this);
        cp.add(backBtn);
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