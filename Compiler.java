package FinalProject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Compiler extends JFrame{
    private JPanel mainUI;
    private JButton openFileButton, lexicalAnalysisButton, syntaxAnalysisButton, semanticAnalysisButton, clearButton;
    private JTextArea resultText, codeText;
    private int lexicalAnalysis = 0;
    private int syntaxAnalysis = 0;
    private int semanticAnalysis = 0;
    private String chosenFile;

    public String getChosenFile() {
        return chosenFile;
    }

    public void setChosenFile(String chosenFile) {
        this.chosenFile = chosenFile;
    }

    public Compiler() {
        setContentPane(mainUI);
        setTitle("COMPILER");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileActionPerformed(e);
            }
        });

        lexicalAnalysisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lexicalAnalysisActionPerformed(e);
            }
        });

        syntaxAnalysisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                syntaxAnalysisActionPerformed(e);
            }
        });

        semanticAnalysisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                semanticAnalysisActionPerformed(e);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearActionPerformed(e);
            }
        });
    }

    private void openFileActionPerformed(ActionEvent e) {
        chooseFile();
    }

    private void lexicalAnalysisActionPerformed(ActionEvent e) {
        if (lexicalAnalysis == 1) {
            lexicalAnalysis();
            syntaxAnalysis = 1;
        } else {
            JOptionPane.showMessageDialog(Compiler.this, "You have not selected a file yet!");
        }
    }

    private void syntaxAnalysisActionPerformed(ActionEvent e) {
        if (syntaxAnalysis == 1) {
            syntaxAnalysis();
            semanticAnalysis = 1;
        } else {
            JOptionPane.showMessageDialog(Compiler.this, "You need to pass the lexical analysis first!");
        }
    }

    private void semanticAnalysisActionPerformed(ActionEvent e) {
        if (semanticAnalysis == 1) {
            semanticAnalysis();
        } else {
            JOptionPane.showMessageDialog(Compiler.this, "You need to pass the lexical and syntax analysis first!");
        }
    }

    private void clearActionPerformed(ActionEvent e) {
        dispose();
        new Compiler();
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            setChosenFile(String.valueOf(selectedFile));
            try {
                Scanner sc = new Scanner(selectedFile);
                while (sc.hasNextLine()) {
                    codeText.append(sc.nextLine() + "\n");
                }
                sc.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        lexicalAnalysis = 1;
    }

    private static final String[] DATA_TYPES = {"int", "double", "char", "String"};
    private static final String ASSIGNMENT_OPERATOR = "=";
    private static final String DELIMITER = ";";

    private void lexicalAnalysis() {
        String[] tokens = getChosenFile().split("\\s+|\\b\\d+\\.\\d+\\b|\\b\\d+\\b"); // Split the line by whitespace

        // Analyze each token
        for (String token : tokens) {
            if (isDataType(token)) {
                resultText.append("<data_type> ");
            } else if (token.equals(ASSIGNMENT_OPERATOR)) {
                resultText.append("<assignment_operator> ");
            } else if (token.equals(DELIMITER)) {
                resultText.append("<delimiter> ");
            } else if (isValue(token)) {
                resultText.append("<value> ");
            } else {
                resultText.append("<identifier> ");
            }
        }
    }

    private static boolean isDataType(String token) {
        for (String type : DATA_TYPES) {
            if (type.equals(token)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValue(String token) {
        if (token.startsWith("\"") || token.startsWith("'")) {
            return true;
        } else if (token.matches("\\b\\d+\\.\\d+\\b")) {
            return true;
        } else if (token.matches("\\b\\d+\\b")) {
            return true;
        } else
            return false;
    }

    private void syntaxAnalysis() {

    }

    private void semanticAnalysis() {

    }

    public static void main(String[] args) {
        new Compiler();
    }
}
