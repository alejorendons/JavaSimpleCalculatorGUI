import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String text;
    private int pos;
    private Character currentChar;

    public Lexer(String text) {
        this.text = text;
        this.pos = 0;
        this.currentChar = text.length() > 0 ? text.charAt(0) : null;
    }

    private void advance() {
        pos++;
        if (pos > text.length() - 1) {
            currentChar = null; // Fin del texto
        } else {
            currentChar = text.charAt(pos);
        }
    }

    private void skipWhitespace() {
        while (currentChar != null && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    private String number() {
        StringBuilder result = new StringBuilder();
        while (currentChar != null && (Character.isDigit(currentChar) || currentChar == '.')) {
            result.append(currentChar);
            advance();
        }
        return result.toString();
    }

    public List<Token> getTokens() throws Exception {
        List<Token> tokens = new ArrayList<>();
        while (currentChar != null) {
            if (Character.isWhitespace(currentChar)) {
                skipWhitespace();
                continue;
            }

            if (Character.isDigit(currentChar)) {
                tokens.add(new Token(Token.TokenType.NUMBER, number()));
                continue;
            }

            switch (currentChar) {
                case '+':
                    tokens.add(new Token(Token.TokenType.PLUS, "+"));
                    break;
                case '-':
                    tokens.add(new Token(Token.TokenType.MINUS, "-"));
                    break;
                case '*':
                    tokens.add(new Token(Token.TokenType.MUL, "*"));
                    break;
                case '/':
                    tokens.add(new Token(Token.TokenType.DIV, "/"));
                    break;
                case '(':
                    tokens.add(new Token(Token.TokenType.LPAREN, "("));
                    break;
                case ')':
                    tokens.add(new Token(Token.TokenType.RPAREN, ")"));
                    break;
                default:
                    throw new Exception("Error: Caracter inválido '" + currentChar + "'");
            }
            advance();
        }
        tokens.add(new Token(Token.TokenType.EOF, null)); // Token de fin de archivo
        return tokens;
    }
}
