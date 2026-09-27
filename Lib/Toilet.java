package Lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Toilet extends JFrame implements ActionListener {

    private Container cp;
    private JButton backBtn;
    private JFrame parentFrame; 

    public Toilet(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        setTitle("Feed Room");
        setSize(400, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cp = getContentPane();
        cp.setLayout(null);

        
        backBtn = new JButton("back");
        backBtn.setBounds(130, 500, 140, 40);
        backBtn.setFocusable(false);
        backBtn.addActionListener(this);
        cp.add(backBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            if (parentFrame != null) {
                parentFrame.setVisible(true); // เปิดหน้า GameFrame กลับมา
            }
            this.dispose();
        }
    }
}