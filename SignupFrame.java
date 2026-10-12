import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class SignupFrame extends JFrame implements ActionListener{
    
    private JLabel text1 ;
    private JLabel text2 ;
    private JLabel title ;
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
        setSize(400,350);

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
        text1.setBounds(47, 110, 100, 30);
        text1.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(text1);

        username = new JTextField();
        username.setBounds(160, 110, 180, 30);
        add(username);

        text2 = new JLabel("password : ");
        text2.setBounds(50, 160, 100, 30);
        text2.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(text2);

        password = new JPasswordField();
        password.setBounds(160, 160, 180, 30);
        add(password);

        signupBtn = new JButton("Sign up");
        signupBtn.setBounds(135, 220, 130, 40);
        signupBtn.setFocusable(false);
        add(signupBtn);
        signupBtn.addActionListener(this);

        backBtn = new JButton("back");
        //backBtn.setBounds(135,260,130,40);
        backBtn.setBounds(0,0,40,40);
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
            JOptionPane.showMessageDialog(this, "Please fill in all fields."
            , "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (usernameIn.contains(",") || passwordIn.contains(",")) {
            JOptionPane.showMessageDialog(this, "Username and password cannot contain commas (,))"
            , "Warning", JOptionPane.WARNING_MESSAGE);
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
                String line = br.readLine(); // ข้าม Header แถวแรก

                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] arr = line.split(",");

                    if (arr.length > 1 && arr[1].trim().equals(usernameIn)) {
                        JOptionPane.showMessageDialog(this, "This username is already taken!"
                        , "Error", JOptionPane.ERROR_MESSAGE);
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
            if (needNewLine) {
                bw.newLine();
            }
            bw.write(newUserRecord);
            bw.newLine(); 

            JOptionPane.showMessageDialog(this, "Sign up successful!", "Success"
            , JOptionPane.INFORMATION_MESSAGE);

            if (preFrame != null) {
                preFrame.setVisible(true);
            }
            dispose();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Cannot write to users.csv: " 
            + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}