package canteen;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginFrame extends JFrame {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JLabel         lblStatus;

    private static final Color BG_TOP    = new Color(26, 35, 126);
    private static final Color BG_BOTTOM = new Color(63, 81, 181);
    private static final Color CARD_BG   = new Color(255, 255, 255);
    private static final Color BTN_COLOR = new Color(255, 111, 0);
    private static final Color FIELD_BG  = new Color(240, 242, 255);
    private static final Color TEXT_DARK = new Color(26, 35, 126);

    public LoginFrame() {
        setTitle("Canteen Login");
        setSize(420, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Gradient background panel
        JPanel bg = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        // Card panel
        JPanel card = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(340, 380));
        card.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 0, 8, 0);
        gc.fill   = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 2;

        // Icon label
        JLabel icon = new JLabel("\uD83C\uDF7D", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        gc.gridx = 0; gc.gridy = 0;
        card.add(icon, gc);

        // Title
        JLabel title = new JLabel("Canteen System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        gc.gridy = 1;
        card.add(title, gc);

        // Subtitle
        JLabel sub = new JLabel("Staff Login Portal", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(100, 100, 150));
        gc.gridy = 2;
        card.add(sub, gc);

        // Username label
        gc.gridwidth = 1; gc.gridx = 0; gc.gridy = 3;
        gc.insets = new Insets(12, 0, 2, 0);
        JLabel uLabel = new JLabel("Username");
        uLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        uLabel.setForeground(TEXT_DARK);
        card.add(uLabel, gc);

        // Username field
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBackground(FIELD_BG);
        txtUsername.setForeground(TEXT_DARK);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 255), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtUsername.setPreferredSize(new Dimension(260, 40));
        gc.gridwidth = 2; gc.gridx = 0; gc.gridy = 4;
        gc.insets = new Insets(0, 0, 8, 0);
        card.add(txtUsername, gc);

        // Password label
        gc.gridwidth = 1; gc.gridx = 0; gc.gridy = 5;
        gc.insets = new Insets(4, 0, 2, 0);
        JLabel pLabel = new JLabel("Password");
        pLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pLabel.setForeground(TEXT_DARK);
        card.add(pLabel, gc);

        // Password field
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBackground(FIELD_BG);
        txtPassword.setForeground(TEXT_DARK);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 255), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtPassword.setPreferredSize(new Dimension(260, 40));
        gc.gridwidth = 2; gc.gridx = 0; gc.gridy = 6;
        gc.insets = new Insets(0, 0, 8, 0);
        card.add(txtPassword, gc);

        // Status
        lblStatus = new JLabel(" ", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(200, 50, 50));
        gc.gridy = 7;
        card.add(lblStatus, gc);

        // Login button
        JButton btnLogin = new JButton("LOGIN") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? BTN_COLOR.darker() : BTN_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setOpaque(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(260, 45));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gc.gridy = 8;
        gc.insets = new Insets(4, 0, 0, 0);
        card.add(btnLogin, gc);

        btnLogin.addActionListener(e -> attemptLogin());
        txtPassword.addActionListener(e -> attemptLogin());

        GridBagConstraints bgc = new GridBagConstraints();
        bg.add(card, bgc);

        setVisible(true);
    }

    private void attemptLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            lblStatus.setText("Please fill in all fields.");
            return;
        }

        try (Connection con = DatabaseHelper.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dispose();
                new OrderFrame();
            } else {
                lblStatus.setText("Invalid username or password.");
            }
        } catch (Exception ex) {
            lblStatus.setText("DB error: " + ex.getMessage());
        }
    }
}
