package ui.swing;

import javax.swing.SwingUtilities;
import ui.swing.frames.LoginFrame;

public class MainSwingLauncher {
    public static void main(String[] args) {
        // Launch Swing UI on EDT
        SwingUtilities.invokeLater(() -> {
            LoginFrame lf = new LoginFrame();
            lf.setVisible(true);
        });
    }
}