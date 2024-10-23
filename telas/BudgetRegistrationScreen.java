package telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BudgetRegistrationScreen extends JFrame {

    private JTextField startDateField;
    private JTextField areaField;
    private JTextField endDateField;
    private JTextField finalValueField;
    private JComboBox<String> serviceComboBox;
    private String username; // Cliente que está logado

    public BudgetRegistrationScreen(String username) {
        this.username = username;

        setTitle("CLEANLUXE - Cadastro de Orçamento");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Definir cores da paleta
        Color backgroundColor = Color.decode("#D1E3DD");
        Color buttonColor = Color.decode("#5762D5");
        Color textColor = Color.decode("#6E7DAB");

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(backgroundColor);

        // Label e campo para Tipo de Serviço
        JLabel serviceLabel = new JLabel("Tipo de Serviço:");
        serviceLabel.setBounds(10, 20, 150, 25);
        serviceLabel.setForeground(textColor);
        panel.add(serviceLabel);

        serviceComboBox = new JComboBox<>();
        loadServices(); // Carregar serviços do CSV
        serviceComboBox.setBounds(150, 20, 200, 25);
        panel.add(serviceComboBox);

        // Label e campo para Data Inicial
        JLabel startDateLabel = new JLabel("Data Inicial (AAAA-MM-DD):");
        startDateLabel.setBounds(10, 60, 200, 25);
        startDateLabel.setForeground(textColor);
        panel.add(startDateLabel);

        startDateField = new JTextField();
        startDateField.setBounds(200, 60, 150, 25);
        panel.add(startDateField);

        // Label e campo para Data Final
        JLabel endDateLabel = new JLabel("Data Final:");
        endDateLabel.setBounds(10, 100, 150, 25);
        endDateLabel.setForeground(textColor);
        panel.add(endDateLabel);

        endDateField = new JTextField();
        endDateField.setBounds(150, 100, 200, 25);
        endDateField.setEditable(false); // Não editável
        panel.add(endDateField);

        // Label e campo para Área em m²
        JLabel areaLabel = new JLabel("Total m²:");
        areaLabel.setBounds(10, 140, 150, 25);
        areaLabel.setForeground(textColor);
        panel.add(areaLabel);

        areaField = new JTextField();
        areaField.setBounds(150, 140, 200, 25);
        panel.add(areaField);

        // Label e campo para Valor Final
        JLabel finalValueLabel = new JLabel("Valor Final:");
        finalValueLabel.setBounds(10, 180, 150, 25);
        finalValueLabel.setForeground(textColor);
        panel.add(finalValueLabel);

        finalValueField = new JTextField();
        finalValueField.setBounds(150, 180, 200, 25);
        finalValueField.setEditable(false); // Não editável
        panel.add(finalValueField);

        // Ação ao perder o foco no campo de Data Inicial
        startDateField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                updateEndDateAndFinalValue();
            }
        });

        // Ação ao perder o foco no campo de Área
        areaField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEndDateAndFinalValue();
            }
        });

        // Botão para salvar orçamento
        JButton saveButton = new JButton("Salvar Orçamento");
        saveButton.setBounds(150, 220, 150, 25);
        saveButton.setBackground(buttonColor);
        saveButton.setForeground(Color.WHITE);
        panel.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBudget();
            }
        });

        add(panel);
        setVisible(true);
    }

    private void loadServices() {
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\paulo.victor\\Documents\\sistema_CleanLuxe\\servicos.csv"))) {
            String line;
            br.readLine(); // Ignorar cabeçalho
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    serviceComboBox.addItem(data[0]); // Adiciona tipo de serviço ao combo box
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateEndDateAndFinalValue() {
        try {
            String selectedService = (String) serviceComboBox.getSelectedItem();
            String areaText = areaField.getText().trim();
            String startDateText = startDateField.getText().trim();

            if (!startDateText.isEmpty() && !areaText.isEmpty()) {
                LocalDate startDate = LocalDate.parse(startDateText, DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Certifique-se de usar o formato correto
                int area = Integer.parseInt(areaText);

                // Ler o tempo necessário para o serviço
                BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\paulo.victor\\Documents\\sistema_CleanLuxe\\servicos.csv"));
                String line;
                int timeRequired = 0;
                boolean serviceFound = false;

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 3 && data[0].equals(selectedService)) {
                        timeRequired = Integer.parseInt(data[2].trim()); // Tempo necessário em dias
                        serviceFound = true;
                        break;
                    }
                }
                br.close();

                if (!serviceFound) {
                    JOptionPane.showMessageDialog(this, "Serviço não encontrado: " + selectedService);
                    return; // Saída se o serviço não for encontrado
                }

                LocalDate endDate = startDate.plusDays(timeRequired);
                endDateField.setText(endDate.toString());

                // Calcular valor final
                double pricePerSquareMeter = 0;
                // Obter preço por metro quadrado
                BufferedReader brPrice = new BufferedReader(new FileReader("C:\\Users\\paulo.victor\\Documents\\sistema_CleanLuxe\\servicos.csv"));
                while ((line = brPrice.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 3 && data[0].equals(selectedService)) {
                        pricePerSquareMeter = Double.parseDouble(data[1].trim());
                        break;
                    }
                }
                brPrice.close();

                double finalValue = area * pricePerSquareMeter;
                finalValueField.setText(String.valueOf(finalValue));
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato AAAA-MM-DD.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao calcular dados do orçamento.");
        }
    }

    private void saveBudget() {
        String serviceType = (String) serviceComboBox.getSelectedItem();
        String startDate = startDateField.getText().trim();
        String endDate = endDateField.getText().trim();
        String area = areaField.getText().trim();
        String finalValue = finalValueField.getText().trim();
        String status = "Cadastrado"; // Status definido como "Cadastrado"

        if (serviceType.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || area.isEmpty() || finalValue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
            return;
        }

        try (FileWriter writer = new FileWriter("C:\\Users\\paulo.victor\\Documents\\sistema_CleanLuxe\\orcamentos.csv", true)) {
            writer.append(username);
            writer.append(",");
            writer.append(serviceType);
            writer.append(",");
            writer.append(startDate);
            writer.append(",");
            writer.append(endDate);
            writer.append(",");
            writer.append(area);
            writer.append(",");
            writer.append(finalValue);
            writer.append(",");
            writer.append(status); // Adicionando o status ao CSV
            writer.append("\n");
            JOptionPane.showMessageDialog(this, "Orçamento salvo com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar o orçamento.");
        }
    }

    public static void main(String[] args) {
        new BudgetRegistrationScreen("Cliente1");
    }
}
