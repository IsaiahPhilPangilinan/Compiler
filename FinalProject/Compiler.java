package FinalProject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Compiler extends JFrame{
    private JPanel mainUI;
    private JButton openFileButton, lexicalAnalysisButton, syntaxAnalysisButton, semanticAnalysisButton, clearButton;
    private JTextArea resultText, codeText;
    private int lexicalAnalysis = 0;
    private int syntaxAnalysis = 0;
    private int semanticAnalysis = 0;
    private String chosenFile = "";
    private String expression = "";
    private boolean isPassed = false;

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
        if (syntaxAnalysis == 1 && lexicalAnalysis == 1) {
            syntaxAnalysis();
            if (isPassed == true) {
                semanticAnalysis = 1;
            } else {
                semanticAnalysis = 0;
            }
        } else {
            JOptionPane.showMessageDialog(Compiler.this, "You need to pass the lexical analysis first!");
        }
    }

    private void semanticAnalysisActionPerformed(ActionEvent e) {
        if (semanticAnalysis == 1 && syntaxAnalysis == 1 && lexicalAnalysis == 1) {
            String[] tokens = splitExpression(chosenFile).toArray(new String[0]);
            if (semanticAnalysis(tokens)) {
                resultText.append("\n\nSemantically Correct!");
            } else {
                resultText.append("\n\nSemantically Incorrect!");
            }
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
            chosenFile = "";
            File selectedFile = fileChooser.getSelectedFile();
            try {
                Scanner sc = new Scanner(selectedFile);
                while (sc.hasNextLine()) {
                    String input = sc.nextLine();
                    chosenFile += input;
                    codeText.append(input + "\n");
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
        String[] tokens = splitExpression(chosenFile).toArray(new String[0]);

        for (String token : tokens) {
            if (isDataType(token)) {
                expression += "<data_type>";
                resultText.append("<data_type> ");
            } else if (token.equals(ASSIGNMENT_OPERATOR)) {
                expression += "<assignment_operator>";
                resultText.append("<assignment_operator> ");
            } else if (token.equals(DELIMITER)) {
                expression += "<delimiter>";
                resultText.append("<delimiter> ");
            } else if (isValue(token)) {
                expression += "<value>";
                resultText.append("<value> ");
            } else {
                expression += "<identifier>";
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
        } else {
            return false;
        }

    }

    public static List<String> splitExpression(String chosenFile) {
        List<String> tokens = new ArrayList<>();

        String[] split = chosenFile.split("\\s+(?=(?:[^\"\\\\]*\"[^\"]*\\\")*[^\"]*$)");

        for (String token : split) {
            tokens.addAll(Arrays.asList(token.split("(?=;)|(?<=;)|(?<==)|(?==)")));
        }

        tokens.removeIf(String::isEmpty);

        return tokens;
    }

    private void syntaxAnalysis() {
        if (expression.equals("<data_type><identifier><assignment_operator><value><delimiter>")) {
            resultText.append("\n\nSyntax Analysis: Passed!");
            isPassed = true;
        } else {
            resultText.append("\n\nSyntax Analysis: Failed!");
            isPassed = false;
        }
    }

    private boolean semanticAnalysis(String[] tokens) {
        String dataType = tokens[0];
        String variableName = tokens[1];
        String assignmentOperator = tokens[2];
        String value = tokens[3].replaceAll(";$", "");

        try {
            if (dataType.equals("int")) {
                Integer.parseInt(value);
            } else if (dataType.equals("double")) {
                Double.parseDouble(value);
            } else if (dataType.equals("String")) {
                if (!value.startsWith("\"") || !value.endsWith("\"")) {
                    return false;
                }
            } else if (dataType.equals("char")) {
                if (!value.startsWith("\'") || !value.endsWith("\'")) {
                    return false;
                }
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        new Compiler();
    }
}
