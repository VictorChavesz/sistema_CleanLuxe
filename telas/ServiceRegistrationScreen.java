package telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class ServiceRegistrationScreen extends JFrame {

    private JTextField serviceTypeField;
    private JTextField priceField;
    private JTextField timeField;

    public ServiceRegistrationScreen() {
        setTitle("CLEANLUXE - Cadastro de Serviço");
        setSize(400, 280); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Definir cores da paleta
        Color backgroundColor = Color.decode("#D1E3DD");
        Color buttonColor = Color.decode("#5762D5");
        Color textColor = Color.decode("#6E7DAB");

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(backgroundColor);  // Aplicar cor de fundo

        // Label e campo para Tipo de Serviço
        JLabel serviceTypeLabel = new JLabel("Tipo de Serviço:");
        serviceTypeLabel.setBounds(10, 20, 150, 25);
        serviceTypeLabel.setForeground(textColor);  // Aplicar cor ao texto
        panel.add(serviceTypeLabel);

        serviceTypeField = new JTextField(20);
        serviceTypeField.setBounds(150, 20, 200, 25);
        panel.add(serviceTypeField);

        // Label e campo para Preço por Metro Quadrado
        JLabel priceLabel = new JLabel("Preço por m²:");
        priceLabel.setBounds(10, 60, 150, 25);
        priceLabel.setForeground(textColor);
        panel.add(priceLabel);

        priceField = new JTextField(20);
        priceField.setBounds(150, 60, 200, 25);
        panel.add(priceField);

        // Label e campo para Tempo Necessário (em dias)
        JLabel timeLabel = new JLabel("Tempo necessário (dias)");
        timeLabel.setBounds(10, 100, 150, 25);
        timeLabel.setForeground(textColor);
        panel.add(timeLabel);

        timeField = new JTextField(20);
        timeField.setBounds(150, 100, 200, 25);
        panel.add(timeField);

        // Botão para salvar serviço
        JButton saveButton = new JButton("Salvar Serviço");
        saveButton.setBounds(150, 150, 150, 25);
        saveButton.setBackground(buttonColor);  // Cor do botão
        saveButton.setForeground(Color.WHITE);  // Cor do texto do botão
        panel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String serviceType = serviceTypeField.getText().trim();
                String price = priceField.getText().trim();
                String time = timeField.getText().trim();

                if (serviceType.isEmpty() || price.isEmpty() || time.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.");
                    return;
                }

                // Salvar o serviço no arquivo CSV
                salvarServico(serviceType, price, time);
                JOptionPane.showMessageDialog(null, "Serviço cadastrado com sucesso!");
                dispose();  // Fecha a tela de cadastro
            }
        });

        add(panel);
        setVisible(true);
    }

    // Função para salvar o serviço no arquivo CSV
    private void salvarServico(String serviceType, String price, String time) {
        String filePath = "C:\\Users\\paulo.victor\\Documents\\sistema_CleanLuxe\\servicos.csv";
        try (FileWriter writer = new FileWriter(filePath, true)) {  
            writer.append(serviceType);
            writer.append(",");
            writer.append(price);
            writer.append(",");
            writer.append(time);
            writer.append("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
