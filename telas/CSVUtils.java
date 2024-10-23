package telas;

import java.io.*;
import javax.swing.JOptionPane;

public class CSVUtils {

    private static final String FILE_PATH = "C:\\Users\\paulo.victor\\Documents\\sistema_CleanLuxe\\usuarios.csv";

    // Salva um novo usuário no arquivo CSV
    public static void salvarUsuario(String username, String password, String endereco, String telefone, String estabelecimento) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {  // 'true' para anexar ao arquivo
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

    // Autentica o usuário a partir do arquivo CSV
    // Autentica o usuário a partir do arquivo CSV e retorna o tipo de estabelecimento
    public static String autenticarUsuario(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 5) {
                    String storedUsername = dados[0].trim();
                    String storedPassword = dados[1].trim();
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        return dados[4].trim();  // Retorna o tipo de estabelecimento
                    }
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Arquivo de usuários não encontrado.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;  // Usuário ou senha inválidos
    }

    // Verifica se o usuário já existe
    public static boolean usuarioExiste(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 1) {
                    String storedUsername = dados[0].trim();
                    if (storedUsername.equals(username)) {
                        return true;  // Usuário já existe
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Se o arquivo não existe, nenhum usuário existe ainda
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;  // Usuário não existe
    }
}
