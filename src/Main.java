import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Calculadora Simple. Escribe 'salir' para terminar.");

        while (true) {
            System.out.print(">> ");
            String input = scanner.nextLine();

            if ("salir".equalsIgnoreCase(input)) {
                break;
            }

            if (input.trim().isEmpty()) {
                continue;
            }

            try {
                // 1. Lexer: Convertir texto a tokens
                Lexer lexer = new Lexer(input);
                List<Token> tokens = lexer.getTokens();
                System.out.println("Tokens: " + tokens);

                // 2. Parser: Analizar tokens y calcular
                Parser parser = new Parser(tokens);
                double result = parser.parse();

                // 3. Mostrar resultado
                System.out.println("Resultado: " + result);

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
        System.out.println("Calculadora cerrada.");
    }
}