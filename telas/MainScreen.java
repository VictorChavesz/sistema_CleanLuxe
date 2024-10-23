package telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends JFrame {
    private JPanel budgetPanel;  // Painel para os orçamentos
    private JPanel commentPanel; // Painel para os comentários
    private String username;
    private String establishment;

    public MainScreen(String username, String establishment) {
        this.username = username;
        this.establishment = establishment;

        // Configurações da tela
        setTitle("CLEANLUXE - Tela Principal");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel superior
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(173, 216, 230)); // Azul claro
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel userLabel = new JLabel("Usuário: " + username);
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(userLabel);

        JLabel establishmentLabel = new JLabel("Estabelecimento: " + establishment);
        establishmentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(establishmentLabel);

        // Botão de sair
        JButton exitButton = new JButton("Sair");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBackground(new Color(255, 69, 0)); // Vermelho
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> {
            new LoginScreen(); // Cria nova instância da tela de login
            this.dispose(); // Fecha a tela principal
        });
        topPanel.add(exitButton);

        // Botão de recarregar
        JButton reloadButton = new JButton("Recarregar");
        reloadButton.setFont(new Font("Arial", Font.BOLD, 14));
        reloadButton.setBackground(new Color(30, 144, 255)); // Azul
        reloadButton.setForeground(Color.WHITE);
        reloadButton.addActionListener(e -> {
            loadBudgets();
            loadComments();
        });
        topPanel.add(reloadButton);

        // Adiciona o painel superior à tela
        add(topPanel, BorderLayout.NORTH);

        // Painel central com GridLayout
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new GridLayout(2, 1));

        // Painel de orçamentos
        budgetPanel = new JPanel();
        budgetPanel.setLayout(new BoxLayout(budgetPanel, BoxLayout.Y_AXIS));
        JScrollPane budgetScrollPane = new JScrollPane(budgetPanel);
        centralPanel.add(budgetScrollPane);

        // Painel de comentários
        commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
        JScrollPane commentScrollPane = new JScrollPane(commentPanel);
        centralPanel.add(commentScrollPane);

        // Adiciona o painel central à tela
        add(centralPanel, BorderLayout.CENTER);

        // Painel inferior
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(173, 216, 230)); // Azul claro

        JButton registerBudgetButton = new JButton("Cadastrar Orçamento");
        registerBudgetButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerBudgetButton.setBackground(new Color(30, 144, 255)); // Azul
        registerBudgetButton.setForeground(Color.WHITE);
        registerBudgetButton.addActionListener(e -> new BudgetRegistrationScreen(username));
        bottomPanel.add(registerBudgetButton);

        JButton registerServiceButton = new JButton("Cadastrar Serviço (Apenas Empregados)");
        registerServiceButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerServiceButton.setBackground(new Color(34, 139, 34)); // Verde
        registerServiceButton.setForeground(Color.WHITE);
        registerServiceButton.addActionListener(e -> {
            String password = JOptionPane.showInputDialog("Digite a senha de administrador:");
            if ("admin".equals(password)) {
                new ServiceRegistrationScreen();
            } else {
                JOptionPane.showMessageDialog(null, "Senha inválida. Acesso negado.");
            }
        });
        bottomPanel.add(registerServiceButton);

        // Botão para acessar a tela de comentários
        JButton commentButton = new JButton("Comentários sobre Orçamentos");
        commentButton.setFont(new Font("Arial", Font.BOLD, 14));
        commentButton.setBackground(new Color(255, 165, 0)); // Laranja
        commentButton.setForeground(Color.WHITE);
        commentButton.addActionListener(e -> new CommentScreen(username));
        bottomPanel.add(commentButton);

        add(bottomPanel, BorderLayout.SOUTH);
        loadBudgets();
        loadComments();

        setVisible(true);
    }

    private void loadBudgets() {
        budgetPanel.removeAll(); // Limpa o painel antes de recarregar
        List<String[]> budgets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("orcamentos.csv"))) {
            String line;
            br.readLine(); // Ignorar cabeçalho
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Verifique se a linha contém o número correto de colunas e se o cliente é o usuário logado
                if (data.length == 7 && data[0].trim().equals(username)) {
                    budgets.add(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Formatação para exibir orçamentos
        if (budgets.isEmpty()) {
            JLabel noBudgetLabel = new JLabel("NENHUM ORÇAMENTO CADASTRADO");
            budgetPanel.add(noBudgetLabel);
        } else {
            for (String[] budget : budgets) {
                JPanel budgetEntry = new JPanel();
                budgetEntry.setLayout(new BoxLayout(budgetEntry, BoxLayout.Y_AXIS));
                budgetEntry.add(new JLabel("Cliente: " + budget[0]));
                budgetEntry.add(new JLabel("Tipo de Serviço: " + budget[1]));
                budgetEntry.add(new JLabel("Data Inicial: " + budget[2]));
                budgetEntry.add(new JLabel("Data Final: " + budget[3]));
                budgetEntry.add(new JLabel("Área (m²): " + budget[4]));
                budgetEntry.add(new JLabel("Valor Final: " + budget[5]));
                budgetEntry.add(new JLabel("Status: " + budget[6]));
                budgetEntry.add(new JSeparator()); // Adiciona a linha de separação

                budgetPanel.add(budgetEntry);
            }
        }
        budgetPanel.revalidate(); // Atualiza o painel para exibir os novos dados
        budgetPanel.repaint(); // Redesenha o painel
    }

    private void loadComments() {
        commentPanel.removeAll(); // Limpa o painel antes de recarregar
        try (BufferedReader br = new BufferedReader(new FileReader("comentarios.csv"))) {
            String line;
            br.readLine(); // Ignorar cabeçalho
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[0].trim().equals(username)) {
                    JPanel commentEntry = new JPanel();
                    commentEntry.setLayout(new BoxLayout(commentEntry, BoxLayout.Y_AXIS));
                    commentEntry.add(new JLabel("Cliente: " + data[0]));
                    commentEntry.add(new JLabel("Serviço: " + data[1]));
                    commentEntry.add(new JLabel("Área (m²): " + data[2]));
                    commentEntry.add(new JLabel("Comentário: " + data[3]));
                    commentEntry.add(new JSeparator()); // Adiciona a linha de separação

                    commentPanel.add(commentEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar comentários.");
        }
        commentPanel.revalidate(); // Atualiza o painel para exibir os novos dados
        commentPanel.repaint(); // Redesenha o painel
    }

    public static void main(String[] args) {
        new MainScreen("teste", "Tipo Estabelecimento"); // Substitua pelos dados do cliente logado
    }
}
