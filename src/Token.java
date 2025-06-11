public class Token {
    enum TokenType {
        NUMBER, PLUS, MINUS, MUL, DIV, LPAREN, RPAREN, EOF
    }

    TokenType type;
    String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token(" + type + ", " + value + ")";
    }
}
