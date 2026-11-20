package canteen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class OrderFrame extends JFrame {

    private static final double TAX_RATE = 0.05;

    private static final Color BG_TOP    = new Color(26, 35, 126);
    private static final Color BG_BOTTOM = new Color(63, 81, 181);
    private static final Color CARD_BG   = new Color(255, 255, 255);
    private static final Color BTN_COLOR = new Color(255, 111, 0);
    private static final Color HDR_BG    = new Color(232, 234, 255);
    private static final Color TEXT_DARK = new Color(26, 35, 126);
    private static final Color ROW_ALT   = new Color(245, 246, 255);

    private String[]    itemNames;
    private double[]    itemPrices;
    private JCheckBox[] checkBoxes;
    private JSpinner[]  spinners;
    private JLabel      lblRunningTotal;
    private JTextField  txtStudentID;

    public OrderFrame() {
        setTitle("Canteen Order System");
        setSize(600, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        loadMenuItems();
        buildUI();
        setVisible(true);
    }

    private void loadMenuItems() {
        List<String> names  = new ArrayList<>();
        List<Double> prices = new ArrayList<>();
        try {
            Connection con = DatabaseHelper.getConnection();
            ResultSet rs = con.createStatement()
                             .executeQuery("SELECT item_name, price FROM inventory");
            while (rs.next()) {
                names.add(rs.getString("item_name"));
                prices.add(rs.getDouble("price"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not load menu: " + e.getMessage());
        }
        itemNames  = names.toArray(new String[0]);
        itemPrices = new double[prices.size()];
        for (int i = 0; i < prices.size(); i++) itemPrices[i] = prices.get(i);
        checkBoxes = new JCheckBox[itemNames.length];
        spinners   = new JSpinner[itemNames.length];
    }

    private void buildUI() {
        JPanel bg = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setBorder(new EmptyBorder(16, 16, 16, 16));
        setContentPane(bg);

        JLabel title = new JLabel("Canteen Order System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));
        bg.add(title, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel header = new JPanel(new GridLayout(1, 3, 8, 0));
        header.setBackground(HDR_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 255), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        for (String col : new String[]{"Food Item", "Price", "Quantity"}) {
            JLabel lbl = new JLabel(col, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setForeground(TEXT_DARK);
            header.add(lbl);
        }
        card.add(header, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridLayout(itemNames.length, 3, 4, 4));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(8, 0, 0, 0));

        for (int i = 0; i < itemNames.length; i++) {
            final int idx = i;
            Color rowColor = (i % 2 == 0) ? Color.WHITE : ROW_ALT;

            checkBoxes[i] = new JCheckBox(itemNames[i]);
            checkBoxes[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            checkBoxes[i].setBackground(rowColor);
            checkBoxes[i].setForeground(new Color(40, 40, 80));
            checkBoxes[i].setBorder(new EmptyBorder(6, 10, 6, 6));

            JLabel priceLabel = new JLabel("Rs. " + String.format("%.2f", itemPrices[i]), SwingConstants.CENTER);
            priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            priceLabel.setForeground(new Color(0, 100, 60));
            priceLabel.setBackground(rowColor);
            priceLabel.setOpaque(true);

            spinners[i] = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
            spinners[i].setEnabled(false);
            spinners[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            spinners[i].setBackground(rowColor);

            checkBoxes[i].addActionListener(e -> {
                spinners[idx].setEnabled(checkBoxes[idx].isSelected());
                updateTotal();
            });
            spinners[i].addChangeListener(e -> updateTotal());

            menuPanel.add(checkBoxes[i]);
            menuPanel.add(priceLabel);
            menuPanel.add(spinners[i]);
        }

        JScrollPane scroll = new JScrollPane(menuPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        card.add(scroll, BorderLayout.CENTER);
        bg.add(card, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(10, 10)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel idRow = new JPanel(new BorderLayout(10, 0));
        idRow.setOpaque(false);
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLabel.setForeground(TEXT_DARK);
        txtStudentID = new JTextField();
        txtStudentID.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtStudentID.setBackground(new Color(240, 242, 255));
        txtStudentID.setForeground(TEXT_DARK);
        txtStudentID.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 255), 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        idRow.add(idLabel, BorderLayout.WEST);
        idRow.add(txtStudentID, BorderLayout.CENTER);

        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        JLabel totalLabel = new JLabel("Running Total:");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalLabel.setForeground(TEXT_DARK);
        lblRunningTotal = new JLabel("Rs. 0.00");
        lblRunningTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblRunningTotal.setForeground(new Color(0, 150, 80));
        lblRunningTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        totalRow.add(totalLabel, BorderLayout.WEST);
        totalRow.add(lblRunningTotal, BorderLayout.EAST);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        infoPanel.setOpaque(false);
        infoPanel.add(idRow);
        infoPanel.add(totalRow);
        bottom.add(infoPanel, BorderLayout.CENTER);

        JButton btnOrder = new JButton("PLACE ORDER") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? BTN_COLOR.darker() : BTN_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        btnOrder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnOrder.setForeground(Color.WHITE);
        btnOrder.setOpaque(false);
        btnOrder.setContentAreaFilled(false);
        btnOrder.setBorderPainted(false);
        btnOrder.setFocusPainted(false);
        btnOrder.setPreferredSize(new Dimension(160, 44));
        btnOrder.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOrder.addActionListener(e -> placeOrder());
        bottom.add(btnOrder, BorderLayout.EAST);

        JPanel bottomWrapper = new JPanel(new BorderLayout());
        bottomWrapper.setOpaque(false);
        bottomWrapper.setBorder(new EmptyBorder(12, 0, 0, 0));
        bottomWrapper.add(bottom);
        bg.add(bottomWrapper, BorderLayout.SOUTH);
    }

    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < itemNames.length; i++) {
            if (checkBoxes[i].isSelected())
                total += itemPrices[i] * (int) spinners[i].getValue();
        }
        lblRunningTotal.setText("Rs. " + String.format("%.2f", total));
    }

    private void placeOrder() {
        String studentID = txtStudentID.getText().trim();
        if (studentID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID.");
            return;
        }
        List<Integer> selected = new ArrayList<>();
        for (int i = 0; i < itemNames.length; i++) {
            if (checkBoxes[i].isSelected()) selected.add(i);
        }
        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one item.");
            return;
        }

        double subtotal = 0;
        StringBuilder receipt = new StringBuilder();
        receipt.append("====== CANTEEN RECEIPT ======\n\n");
        receipt.append(String.format("%-22s %4s %9s%n", "Item", "Qty", "Amount"));
        receipt.append("-------------------------------------\n");

        for (int i : selected) {
            int    qty    = (int) spinners[i].getValue();
            double amount = itemPrices[i] * qty;
            subtotal     += amount;
            receipt.append(String.format("%-22s %4d %9.2f%n", itemNames[i], qty, amount));
        }

        double tax   = subtotal * TAX_RATE;
        double total = subtotal + tax;

        receipt.append("-------------------------------------\n");
        receipt.append(String.format("%-22s %14.2f%n", "Subtotal (Rs.)", subtotal));
        receipt.append(String.format("%-22s %14.2f%n", "Tax 5%  (Rs.)",  tax));
        receipt.append(String.format("%-22s %14.2f%n", "TOTAL   (Rs.)",  total));
        receipt.append("\nStudent ID : ").append(studentID);

        JTextArea ta = new JTextArea(receipt.toString());
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setEditable(false);
        ta.setBackground(new Color(245, 246, 255));

        int confirm = JOptionPane.showConfirmDialog(this,
            new JScrollPane(ta), "Confirm Order", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            saveTransaction(studentID, total, selected);
        }
    }

    private void saveTransaction(String studentID, double total, List<Integer> selected) {
        try {
            Connection con = DatabaseHelper.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO transactions (student_id, total_amount) VALUES (?, ?)");
            ps.setString(1, studentID);
            ps.setDouble(2, total);
            ps.executeUpdate();

            PreparedStatement pu = con.prepareStatement(
                "UPDATE inventory SET stock = stock - ? WHERE item_name = ?");
            for (int i : selected) {
                pu.setInt(1, (int) spinners[i].getValue());
                pu.setString(2, itemNames[i]);
                pu.executeUpdate();
            }
            con.commit();

            JOptionPane.showMessageDialog(this,
                "Order placed successfully!\nTotal: Rs. " + String.format("%.2f", total));

            for (int i = 0; i < itemNames.length; i++) {
                checkBoxes[i].setSelected(false);
                spinners[i].setValue(1);
                spinners[i].setEnabled(false);
            }
            txtStudentID.setText("");
            lblRunningTotal.setText("Rs. 0.00");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
