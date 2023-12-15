package FinalProject;

import javax.swing.*;

public class Try extends JFrame{
    private JPanel mainUI;
    private JButton openFile, lexicalAnalysis, syntaxAnalysis, semanticAnalysis, clear;
    private JTextArea codeText, resultText;

    public Try() {
        setContentPane(mainUI);
        setTitle("COMPILER NG TATLONG ITIK");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Try();
    }
}
