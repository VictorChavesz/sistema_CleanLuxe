package telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommentScreen extends JFrame {
    private String username; // Cliente que está logado
    private JTable budgetTable;
    private String[][] tableData;
    private JTextField commentField;

    public CommentScreen(String username) {
        this.username = username;

        setTitle("CLEANLUXE - Comentários sobre Orçamento");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Definir cores da paleta
        Color backgroundColor = Color.decode("#D1E3DD");
        Color buttonColor = Color.decode("#5762D5");

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(backgroundColor);

        // Tabela para exibir orçamentos
        loadBudgets();
        String[] columnNames = {"Cliente", "Serviço", "Data Inicial", "Data Final", "Área (m²)", "Valor Final", "Status"};
        budgetTable = new JTable(tableData, columnNames);
        JScrollPane scrollPane = new JScrollPane(budgetTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Campo para comentário
        commentField = new JTextField();
        panel.add(commentField, BorderLayout.SOUTH);

        // Botão para salvar comentário
        JButton saveCommentButton = new JButton("Salvar Comentário");
        saveCommentButton.setBackground(buttonColor);
        saveCommentButton.setForeground(Color.WHITE);
        saveCommentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveComment();
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(commentField, BorderLayout.CENTER);
        bottomPanel.add(saveCommentButton, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private void loadBudgets() {
        List<String[]> budgets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("orcamentos.csv"))) {
            String line;
            br.readLine(); // Ignorar cabeçalho
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[0].trim().equals(username)) {
                    String[] budget = new String[7];
                    System.arraycopy(data, 0, budget, 0, 6);
                    budget[6] = data[6]; // Status
                    budgets.add(budget);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        tableData = new String[budgets.size()][7];
        budgets.toArray(tableData);
    }

    private void saveComment() {
        int selectedRow = budgetTable.getSelectedRow();
        if (selectedRow >= 0) {
            String comment = commentField.getText().trim();
            String service = tableData[selectedRow][1]; // Pega o serviço selecionado
            String area = tableData[selectedRow][4]; // Pega a área (m²) selecionada
            
            // Salva o comentário no arquivo comentarios.csv
            try (FileWriter writer = new FileWriter("comentarios.csv", true)) {
                String line = String.format("%s,%s,%s,%s\n", username, service, area, comment);
                writer.append(line);
                writer.flush();
                JOptionPane.showMessageDialog(this, "Comentário salvo com sucesso!");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao salvar comentário.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um orçamento para comentar.");
        }
    }

    public static void main(String[] args) {
        new CommentScreen("teste"); // Substitua pelo cliente logado
    }
}
