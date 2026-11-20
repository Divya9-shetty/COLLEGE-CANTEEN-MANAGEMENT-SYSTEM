package canteen;

import javax.swing.SwingUtilities;

// Main: Entry point of the Canteen System application
public class Main {
    public static void main(String[] args) {
        // Launch LoginFrame on the Swing Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
