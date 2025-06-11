import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.util.List;

public class CalculatorGUI extends JFrame implements ActionListener {

    private JTextField display;
    private StringBuilder inputBuffer;
    private boolean newCalculationStarted; // Para manejar el inicio de nuevas operaciones después de un resultado

    public CalculatorGUI() {
        super("Calculadora Simple");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(370, 500); // Tamaño un poco más grande para mejor estética
        setMinimumSize(new Dimension(320, 450)); // Para evitar que se haga muy pequeña
        setLayout(new BorderLayout(8, 8)); // Más espaciado entre componentes principales
        getContentPane().setBackground(new Color(230, 230, 230)); // Color de fondo gris claro

        inputBuffer = new StringBuilder();
        newCalculationStarted = false;

        // --- Pantalla de la calculadora (Display) ---
        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 42)); // Fuente aún más grande y negrita
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBackground(Color.WHITE); // Fondo blanco para el display
        display.setForeground(Color.BLACK); // Color del texto
        display.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Más padding interno
        add(display, BorderLayout.NORTH);

        // --- Panel de botones ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 4, 12, 12)); // 6 filas, 4 columnas, más espaciado entre botones
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12)); // Más padding alrededor del panel
        buttonPanel.setBackground(new Color(230, 230, 230)); // Mismo color de fondo que la ventana

        // Definición de los botones.
        // Se separan los paréntesis y se reordenan algunos operadores para una disposición más común.
        String[][] buttonLayout = {
                {"C", "(", ")", "/"},
                {"7", "8", "9", "*"},
                {"4", "5", "6", "-"},
                {"1", "2", "3", "+"},
                {"Copy", "0", ".", "="}
        };

        for (String[] row : buttonLayout) {
            for (String text : row) {
                JButton button = createButton(text);
                buttonPanel.add(button);
            }
        }
        add(buttonPanel, BorderLayout.CENTER);

        // Ajustar el tamaño de la ventana al contenido y centrar
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24)); // Fuente para los botones ligeramente más grande
        button.addActionListener(this);

        // Estilos para diferentes tipos de botones
        switch (text) {
            case "=":
                button.setBackground(new Color(255, 140, 0)); // Naranja vibrante para el igual
                button.setForeground(Color.WHITE);
                button.setFont(new Font("Arial", Font.BOLD, 30)); // Más grande y negrita
                break;
            case "C":
                button.setBackground(new Color(210, 210, 210)); // Gris claro para Clear
                button.setForeground(Color.BLACK);
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                button.setBackground(new Color(180, 180, 180)); // Gris medio para operadores
                button.setForeground(Color.BLACK);
                break;
            case "(":
            case ")":
                button.setBackground(new Color(180, 180, 180)); // Gris medio para paréntesis
                button.setForeground(Color.BLACK);
                break;
            case ".":
                button.setBackground(new Color(255, 255, 255)); // Blanco para el punto decimal
                button.setForeground(Color.BLACK);
                break;
            case "Copy":
                button.setBackground(new Color(120, 180, 255)); // Azul claro para Copy
                button.setForeground(Color.BLACK);
                break;
            default: // Números
                button.setBackground(new Color(255, 255, 255)); // Blanco puro para números
                button.setForeground(Color.BLACK);
                break;
        }
        button.setFocusPainted(false); // Quitar el borde de foco
        button.setBorder(BorderFactory.createRaisedBevelBorder()); // Efecto 3D sutil para los botones
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("=")) {
            try {
                if (inputBuffer.length() == 0) {
                    display.setText("");
                    return;
                }
                Lexer lexer = new Lexer(inputBuffer.toString());
                List<Token> tokens = lexer.getTokens();
                Parser parser = new Parser(tokens);
                double result = parser.parse();

                // Formatear el resultado para evitar .0 en enteros y controlar decimales
                if (result == (long) result) {
                    display.setText(String.format("%d", (long) result));
                } else {
                    display.setText(String.format("%.6f", result)); // Mostrar hasta 6 decimales para mayor precisión
                    // Opcional: remover ceros finales si no son significativos
                    display.setText(display.getText().replaceAll("\\.?0*$", ""));
                }
                inputBuffer.setLength(0);
                inputBuffer.append(display.getText());
                newCalculationStarted = true;
            } catch (Exception ex) {
                display.setText("Error: " + ex.getMessage());
                inputBuffer.setLength(0);
                newCalculationStarted = true;
            }
        } else if (command.equals("C")) {
            inputBuffer.setLength(0);
            display.setText("");
            newCalculationStarted = false;
        } else if (command.equals("Copy")) {
            String textToCopy = display.getText();
            if (!textToCopy.isEmpty() && !textToCopy.startsWith("Error")) {
                StringSelection stringSelection = new StringSelection(textToCopy);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                // JOptionPane.showMessageDialog(this, "Resultado copiado", "Copiado", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Manejo de inicio de nueva operación para números y paréntesis (no para operadores)
            if (newCalculationStarted) {
                // Si el display muestra un error, borrarlo antes de empezar a escribir
                if (display.getText().startsWith("Error")) {
                    inputBuffer.setLength(0);
                } else if (!isOperator(command) && !command.equals(".")) {
                    // Si es un número o '(' después de un resultado, limpiar
                    inputBuffer.setLength(0);
                }
                newCalculationStarted = false;
            }

            // Evitar múltiples puntos decimales en el mismo número
            if (command.equals(".") && inputBuffer.toString().contains(".")) {
                // Puedes añadir lógica más sofisticada aquí para verificar si el punto está dentro del número actual
                // Por ahora, una comprobación simple para toda la cadena
                return; // No añadir el punto si ya hay uno
            }

            inputBuffer.append(command);
            display.setText(inputBuffer.toString());
        }
    }

    private boolean isOperator(String command) {
        return command.equals("+") || command.equals("-") || command.equals("*") || command.equals("/");
    }

    public static void main(String[] args) {
        // Asegura que la GUI se ejecute en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(CalculatorGUI::new);
    }
}