import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class TaschenrechnerGUI extends JFrame implements ActionListener {
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String MULTIPLY = "*";
    private static final String DIVIDE = "/";
    private static final String EQUALS = "=";
    private static final String AC = "AC";
    private static final String C = "C";
    private static final String COMMA = ",";

    private final JTextField eingabeFeld;

    public TaschenrechnerGUI() {
        // Setze Titel und Layout
        super("Taschenrechner");
        setLayout(new BorderLayout());

        // Setze das Standardverhalten beim Schließen des Fensters
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Erstelle GUI-Elemente
        eingabeFeld = new JTextField();
        eingabeFeld.setFont(new Font("Arial", Font.PLAIN, 24));
        JButton[] zahlenButtons = new JButton[10];
        for (int i = 0; i < 10; i++) {
            zahlenButtons[i] = new JButton(String.valueOf(i));
            zahlenButtons[i].setActionCommand(String.valueOf(i));
            zahlenButtons[i].addActionListener(this);
        }
        JButton kommaButton = createButton(COMMA);
        JButton plusButton = createButton(PLUS);
        JButton minusButton = createButton(MINUS);
        JButton malButton = createButton(MULTIPLY);
        JButton geteiltButton = createButton(DIVIDE);
        JButton gleichButton = createButton(EQUALS);
        JButton acButton = createButton(AC);
        JButton cButton = createButton(C);

        // Füge Elemente zum Frame hinzu
        add(eingabeFeld, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new GridLayout(5, 4));
        centerPanel.add(acButton);
        centerPanel.add(new JLabel(""));
        centerPanel.add(new JLabel(""));
        centerPanel.add(geteiltButton);
        centerPanel.add(zahlenButtons[7]);
        centerPanel.add(zahlenButtons[8]);
        centerPanel.add(zahlenButtons[9]);
        centerPanel.add(malButton);
        centerPanel.add(zahlenButtons[4]);
        centerPanel.add(zahlenButtons[5]);
        centerPanel.add(zahlenButtons[6]);
        centerPanel.add(minusButton);
        centerPanel.add(zahlenButtons[1]);
        centerPanel.add(zahlenButtons[2]);
        centerPanel.add(zahlenButtons[3]);
        centerPanel.add(plusButton);
        centerPanel.add(zahlenButtons[0]);
        centerPanel.add(kommaButton);
        centerPanel.add(cButton);
        centerPanel.add(gleichButton);
        add(centerPanel, BorderLayout.CENTER);

        // Setze Größe und Sichtbarkeit
        setSize(400, 300);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setActionCommand(text);
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        String eingabe = eingabeFeld.getText();

        switch (action) {
            case AC -> {
                eingabeFeld.setText("");
                return;
            }
            case C -> {
                if (eingabe.length() > 0) {
                    eingabe = eingabe.substring(0, eingabe.length() - 1);
                    eingabeFeld.setText(eingabe);
                }
                return;
            }
            case COMMA -> {
                addNumberOrComma(".");
                return;
            }
            case EQUALS -> {
                Stack<String> postfix = ShuntingYard.parse(eingabe);
                Stack<Double> stack = new Stack<>();
                while (!postfix.isEmpty()) {
                    String token = postfix.pop();
                    if (ShuntingYard.isNumber(token)) {
                        stack.push(Double.parseDouble(token));
                    } else if (ShuntingYard.isOperator(token)) {
                        if (stack.size() < 2) {
                            // Fehlerbehandlung hier
                            return;
                        }
                        double zahl2 = stack.pop();
                        double zahl1 = stack.pop();
                        switch (token) {
                            case PLUS -> stack.push(zahl1 + zahl2);
                            case MINUS -> stack.push(zahl1 - zahl2);
                            case MULTIPLY -> stack.push(zahl1 * zahl2);
                            case DIVIDE -> stack.push(zahl1 / zahl2);
                        }
                    }
                }
                double ergebnis = stack.pop();
                eingabe += " = " + ergebnis;
                eingabeFeld.setText(eingabe);
                return;
            }
            case PLUS, MINUS, MULTIPLY, DIVIDE -> {
                addOperation(action);
                return;
            }
        }

        for (int i = 0; i < 10; i++) {
            if (action.equals(String.valueOf(i))) {
                addNumberOrComma(String.valueOf(i));
                return;
            }
        }
    }

    private void addOperation(String operation) {
        String eingabe = eingabeFeld.getText();
        eingabe += " " + operation + " ";
        eingabeFeld.setText(eingabe);
    }

    private void addNumberOrComma(String text) {
        String eingabe = eingabeFeld.getText();
        eingabe += text;
        eingabeFeld.setText(eingabe);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaschenrechnerGUI::new);
    }
}
