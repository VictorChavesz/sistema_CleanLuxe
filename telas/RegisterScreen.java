package telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterScreen extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JTextField enderecoField;
    private JTextField telefoneField;
    private JButton registerButton, backButton;
    private JComboBox<String> estabelecimentoComboBox;

    public RegisterScreen() {
        setTitle("CLEANLUXE - Cadastro de Usuário");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Definir cores da paleta
        Color backgroundColor = Color.decode("#D1E3DD");
        Color buttonColor = Color.decode("#5762D5");
        Color textColor = Color.decode("#6E7DAB");

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel, backgroundColor, buttonColor, textColor);

        setVisible(true);
    }

    private void placeComponents(JPanel panel, Color backgroundColor, Color buttonColor, Color textColor) {
        panel.setLayout(null);
        panel.setBackground(backgroundColor);

        // Label e campo para Usuário
        JLabel userLabel = new JLabel("Usuário:");
        userLabel.setBounds(10, 20, 120, 25);
        userLabel.setForeground(textColor);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(140, 20, 200, 25);
        panel.add(usernameField);

        // Label e campo para Senha
        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setBounds(10, 60, 120, 25);
        passwordLabel.setForeground(textColor);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(140, 60, 200, 25);
        panel.add(passwordField);

        // Label e campo para Confirmar Senha
        JLabel confirmPasswordLabel = new JLabel("Confirmar Senha:");
        confirmPasswordLabel.setBounds(10, 100, 120, 25);
        confirmPasswordLabel.setForeground(textColor);
        panel.add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setBounds(140, 100, 200, 25);
        panel.add(confirmPasswordField);

        // Label e campo para Endereço
        JLabel enderecoLabel = new JLabel("Endereço:");
        enderecoLabel.setBounds(10, 140, 120, 25);
        enderecoLabel.setForeground(textColor);
        panel.add(enderecoLabel);

        enderecoField = new JTextField(50);
        enderecoField.setBounds(140, 140, 200, 25);
        panel.add(enderecoField);

        // Label e campo para Telefone
        JLabel telefoneLabel = new JLabel("Telefone (DDD + 9 + Número):");
        telefoneLabel.setBounds(10, 180, 200, 25);
        telefoneLabel.setForeground(textColor);
        panel.add(telefoneLabel);

        telefoneField = new JTextField(11);
        telefoneField.setBounds(220, 180, 120, 25);
        panel.add(telefoneField);

        // Tipo estabelecimento
        JLabel estabelecimentoLabel = new JLabel("Tipo de Estabelecimento:");
        estabelecimentoLabel.setBounds(10, 220, 200, 25);
        estabelecimentoLabel.setForeground(textColor);
        panel.add(estabelecimentoLabel);

        String[] tiposEstabelecimento = {"hotel", "restaurante", "loja", "escritório", "spa", "galeria",
                                         "concessionária", "joalheria", "clínica", "teatro", "prédio"};
        estabelecimentoComboBox = new JComboBox<>(tiposEstabelecimento);
        estabelecimentoComboBox.setBounds(220, 220, 120, 25);
        panel.add(estabelecimentoComboBox);

        // Botão de Cadastro
        registerButton = new JButton("Cadastrar");
        registerButton.setBounds(10, 260, 100, 25);
        registerButton.setBackground(buttonColor);
        registerButton.setForeground(Color.WHITE);
        panel.add(registerButton);

        // Botão Voltar
        backButton = new JButton("Voltar");
        backButton.setBounds(280, 260, 100, 25);
        backButton.setBackground(buttonColor);
        backButton.setForeground(Color.WHITE);
        panel.add(backButton);

        // Listener para o botão de cadastro
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String endereco = enderecoField.getText().trim();
                String telefone = telefoneField.getText().trim();
                String estabelecimento = (String) estabelecimentoComboBox.getSelectedItem();

                // Validações de entrada
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                        endereco.isEmpty() || telefone.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "As senhas não correspondem.");
                    return;
                }

                // Salvar o usuário no arquivo CSV
                salvarUsuario(username, password, endereco, telefone, estabelecimento);
                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
                dispose();
                new LoginScreen();  // Retorna para a tela de login
            }
        });

        // Listener para o botão de voltar
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Fecha a tela de registro
                new LoginScreen();  // Abre a tela de login
            }
        });
    }

    // Função para salvar o usuário no arquivo CSV
    private void salvarUsuario(String username, String password, String endereco, String telefone, String estabelecimento) {
        String filePath = "C:\\Users\\paulo.victor\\Documents\\sistema_CleanLuxe\\usuarios.csv";
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.append(username);
            writer.append(",");
            writer.append(password);
            writer.append(",");
            writer.append(endereco);
            writer.append(",");
            writer.append(telefone);
            writer.append(",");
            writer.append(estabelecimento);
            writer.append("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
