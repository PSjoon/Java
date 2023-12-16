import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main extends JFrame {

    private JTextField fileNameField;
    private JTextField filePathField; 
    private JComboBox<String> transactionTypeComboBox;

    public Main() {
        setTitle("Upload de Contas");
        setSize(1000, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fileNameField = new JTextField();
        filePathField = new JTextField();
        transactionTypeComboBox = new JComboBox<>(new String[]{"Entrada", "Saída"});

        JButton chooseFileButton = new JButton("Escolher Arquivo");
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                escolherArquivo();
            }
        });

        JButton uploadButton = new JButton("Upload");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarUpload();
            }
        });

        setLayout(new java.awt.GridLayout(4, 2));

        add(new JLabel("Tipo de Transação:"));
        add(transactionTypeComboBox);
        add(new JLabel("Nome do Arquivo:"));
        add(fileNameField);
        add(new JLabel("Caminho do Arquivo:"));
        add(filePathField);
        add(uploadButton);
        add(chooseFileButton);

        setVisible(true);
    }

    private void escolherArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

private void realizarUpload() {
    String fileName = fileNameField.getText();
    String filePath = filePathField.getText();
    String transactionType = (String) transactionTypeComboBox.getSelectedItem();

    if (filePath.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Selecione um arquivo primeiro.");
        return;
    }

    File sourceFile = new File(filePath);

    if (!sourceFile.exists()) {
        JOptionPane.showMessageDialog(this, "O arquivo selecionado não existe.");
        return;
    }

    String destinationFolder = "";
    if ("Entrada".equals(transactionType)) {
        destinationFolder = "/home/joonie/MEGAsync/contas/" + getNomeDoAnoAtual() + "/entradas/" + getNomeDoMesAtual();
    } else if ("Saída".equals(transactionType)) {
        destinationFolder = "/home/joonie/MEGAsync/contas/" + getNomeDoAnoAtual() + "/saida/" + getNomeDoMesAtual();
    }

    File destinationDir = new File(destinationFolder);
    if (!destinationDir.exists()) {
        destinationDir.mkdirs();
    }

    String destinationPath = destinationFolder + File.separator + fileName;

    try {
        Files.move(sourceFile.toPath(), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
        JOptionPane.showMessageDialog(this, "Upload realizado com sucesso! Arquivo movido para: " + destinationPath);
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro ao mover o arquivo: " + e.getMessage());
    }
}

private String getNomeDoMesAtual() {
    return java.time.format.DateTimeFormatter.ofPattern("MMMM").format(java.time.LocalDate.now());
}

private String getNomeDoAnoAtual() {
    return java.time.format.DateTimeFormatter.ofPattern("yyyy").format(java.time.LocalDate.now());
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }
}
