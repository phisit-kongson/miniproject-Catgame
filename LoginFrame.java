import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import Lib.GameState;

public class LoginFrame extends JFrame implements ActionListener {
    
    private JLabel text1;
    private JLabel text2;
    private JLabel title;
    private JTextField username;
    private JPasswordField password;
    private JButton login1Btn;
    private JButton backBtn;
    private JFrame preFrame;
    private Image background;
    

    public LoginFrame(JFrame preFrame) {
        this.preFrame = preFrame;
        setTitle("Log in");
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);

        try {
            background = new ImageIcon(getClass().getResource("/pic/background/login_bg2.png")).getImage();
        } catch (Exception e) {
            background = null;
        }

        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        bgPanel.setLayout(null);
        setContentPane(bgPanel);

        title = new JLabel("Login");
        title.setBounds(150, 30, 140, 40);
        title.setFont(new Font("Tahoma", Font.BOLD, 30));
        add(title);

        text1 = new JLabel("username : ");
        text1.setBounds(47, 100, 100, 30);
        text1.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(text1);

        username = new JTextField();
        username.setBounds(160, 100, 180, 30);
        add(username);

        text2 = new JLabel("password : ");
        text2.setBounds(50, 175, 100, 30);
        text2.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(text2);

        password = new JPasswordField();
        password.setBounds(160, 175, 180, 30);
        add(password);

        login1Btn = new JButton("Login");
        login1Btn.setBounds(135, 245, 130, 40);
        login1Btn.setFocusable(false);
        add(login1Btn);
        login1Btn.addActionListener(this);

        backBtn = new JButton("back");
        backBtn.setBounds(135, 300, 130, 40);
        backBtn.setFocusable(false);
        add(backBtn);
        backBtn.addActionListener(e -> {
            if (preFrame != null) {
                preFrame.setVisible(true);   
            }
            dispose();                   
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String usernameIn = username.getText().trim();
        String passwordIn = new String(password.getPassword()).trim();

        if (usernameIn.isEmpty() || passwordIn.isEmpty()) {
            showPopup("Please fill in all fields.", "Warning");
            return;
        }

        File file = new File("./File/users.csv");
        if (!file.exists()) {
            showPopup("No user data found. Please sign up first.", "Warning");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); 
            boolean complete = false;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] arr = line.split(",");

                if (arr.length > 2 && usernameIn.equals(arr[1].trim()) && passwordIn.equals(arr[2].trim())) {
                    complete = true;
                    GameState.get().loadUser(usernameIn);
                    showPopup("Login successful!", "Success");
                    dispose();                    
                    GameFrame game = new GameFrame();
                    game.setVisible(true);
                    if (preFrame != null) {
                        preFrame.dispose();
                    }
                    break;
                }
            }

            if (!complete) {
                showPopup("Invalid username or password!", "Warning");
            }
        } catch (IOException ex) {
            showPopup("Cannot read users.csv: " + ex.getMessage(), "Error");
        }
    }

    private void showPopup(String message, String title) {
        JDialog dialog = new JDialog(this, title, true); 
        dialog.setLayout(new BorderLayout());

        JLabel msg = new JLabel(message, SwingConstants.CENTER);
        msg.setFont(new Font("Tahoma", Font.PLAIN, 14));
        msg.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));

        JButton okBtn = new JButton("OK");
        okBtn.setFocusable(false);
        okBtn.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.add(okBtn);

        dialog.add(msg, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.getRootPane().setDefaultButton(okBtn); 

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(280, 130));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}