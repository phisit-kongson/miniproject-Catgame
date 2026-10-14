import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class SignupFrame extends JFrame implements ActionListener{
    
    private JLabel text1 ;
    private JLabel text2 ;
    private JLabel title ;
    private JLabel userHint;
    private JLabel passHint;
    private JTextField username ;
    private JPasswordField password ;
    private JButton signupBtn ;
    private JButton backBtn ;
    private JFrame preFrame;
    private Image background ;

    public SignupFrame(JFrame preFrame){
        this.preFrame = preFrame;
        setTitle("Sign up");
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,400);

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

        title = new JLabel("Sign up");
        title.setBounds(150,30,140,40);
        title.setFont(new Font("Tahoma", Font.BOLD, 30));
        add(title);

        text1 = new JLabel("username : ");
        text1.setBounds(47, 90, 100, 30);
        text1.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(text1);

        username = new JTextField();
        username.setBounds(160, 90, 180, 30);
        add(username);

        userHint = new JLabel("*4-12 characters, no spaces and no comma(,)");
        userHint.setFont(new Font("Tahoma", Font.PLAIN, 11));
        userHint.setForeground(Color.GRAY);
        userHint.setBounds(160, 122, 300, 18);
        add(userHint);

        text2 = new JLabel("password : ");
        text2.setBounds(50, 155, 100, 30);
        text2.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(text2);

        password = new JPasswordField();
        password.setBounds(160, 155, 180, 30);
        add(password);

        passHint = new JLabel("*8-12 characters, no spaces and no comma(,)");
        passHint.setFont(new Font("Tahoma", Font.PLAIN, 11));
        passHint.setForeground(Color.GRAY);
        passHint.setBounds(160, 187, 300, 18);
        add(passHint);

        signupBtn = new JButton("Sign up");
        signupBtn.setBounds(135, 245, 130, 40);
        signupBtn.setFocusable(false);
        add(signupBtn);
        signupBtn.addActionListener(this);

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
    if (usernameIn.contains(",") || passwordIn.contains(",")) {
        showPopup("Username and password cannot contain commas (,)", "Warning");
        return;
    }
    if (usernameIn.length() < 4 || usernameIn.length() > 12) {
        showPopup("Username must be between 4 and 12 characters.", "Warning");
        return;
    }
    if (usernameIn.contains(" ") || passwordIn.contains(" ")) {
        showPopup("Username and password cannot contain spaces.", "Warning");
        return;
    }
    if (passwordIn.length() < 8 || passwordIn.length() > 12) {
        showPopup("Password must be at least 8-12 characters.", "Warning");
        return;
    }

        String filePath = "./File/users.csv";
        File file = new File(filePath);

        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        int nextId = 1;

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine(); 

                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] arr = line.split(",");

                    if (arr.length > 1 && arr[1].trim().equals(usernameIn)) {
                        showPopup("This username is already taken!", "Warning");
                        return;
                    }

                    if (arr.length > 0) {
                        try {
                            int currentId = Integer.parseInt(arr[0].trim());
                            if (currentId >= nextId) {
                                nextId = currentId + 1;
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                }
            } catch (IOException ex) {
                nextId = 1;
            }
        }

        boolean needNewLine = false;
        if (file.exists() && file.length() > 0) {
            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                raf.seek(file.length() - 1);
                int lastByte = raf.read();
                if (lastByte != '\n' && lastByte != '\r') {
                    needNewLine = true; 
                }
            } catch (Exception ignored) {}
        }

        String newUserRecord = nextId + "," + usernameIn + "," + passwordIn ;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            if (needNewLine) bw.newLine();
            bw.write(newUserRecord);
            bw.newLine();
            showPopup("Sign up successful!", "Success");
            if (preFrame != null) preFrame.setVisible(true);
            dispose();
        } catch (IOException ex) {
            showPopup("Cannot write to users.csv: " + ex.getMessage(), "Error");
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