package telas;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Inicia a tela de login
        SwingUtilities.invokeLater(() -> new LoginScreen());
    }
}
