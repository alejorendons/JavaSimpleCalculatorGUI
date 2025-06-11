import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int pos;
    private Token currentToken;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
        this.currentToken = tokens.get(0);
    }

    private void advance() {
        pos++;
        if (pos < tokens.size()) {
            currentToken = tokens.get(pos);
        }
    }

    private void expect(Token.TokenType type) throws Exception {
        if (currentToken.type == type) {
            advance();
        } else {
            throw new Exception("Error de Sintaxis: Se esperaba '" + type + "' pero se encontró '" + currentToken.type + "'.");
        }
    }

    // Regla: factor ::= NUMBER | LPAREN expr RPAREN
    private double factor() throws Exception {
        Token token = currentToken;
        if (token.type == Token.TokenType.NUMBER) {
            advance();
            return Double.parseDouble(token.value);
        } else if (token.type == Token.TokenType.LPAREN) {
            advance();
            double result = expr();
            expect(Token.TokenType.RPAREN);
            return result;
        }
        throw new Exception("Error de Sintaxis: Se esperaba un número o una expresión entre paréntesis.");
    }

    // Regla: term ::= factor ((MUL | DIV) factor)*
    private double term() throws Exception {
        double result = factor();

        // **MEJORA AQUI: Manejar multiplicación implícita (opcional) o error de sintaxis**
        // Si tu calculadora NO debe permitir multiplicación implícita (como 2(3)),
        // entonces el siguiente bloque es crucial para dar un error más específico.
        // Si QUISIERAS permitirla, tendrías que modificar la gramática para inferir MUL.
        // Dado que la gramática actual NO la soporta, es un error de sintaxis.
        if (currentToken.type == Token.TokenType.LPAREN && pos > 0 &&
                (tokens.get(pos - 1).type == Token.TokenType.NUMBER || tokens.get(pos - 1).type == Token.TokenType.RPAREN)) {
            // Este es el caso de "2(" o ")("
            throw new Exception("Error de Sintaxis: Operador esperado antes de '('.");
        }


        while (currentToken.type == Token.TokenType.MUL || currentToken.type == Token.TokenType.DIV) {
            Token op = currentToken;
            advance();
            if (op.type == Token.TokenType.MUL) {
                result *= factor();
            } else {
                double divisor = factor();
                if (divisor == 0) throw new Exception("Error: División por cero.");
                result /= divisor;
            }
        }
        return result;
    }

    // Regla: expr ::= term ((PLUS | MINUS) term)*
    private double expr() throws Exception {
        double result = term();
        while (currentToken.type == Token.TokenType.PLUS || currentToken.type == Token.TokenType.MINUS) {
            Token op = currentToken;
            advance();
            if (op.type == Token.TokenType.PLUS) {
                result += term();
            } else {
                result -= term();
            }
        }
        return result;
    }

    public double parse() throws Exception {
        double result = expr();
        if (currentToken.type != Token.TokenType.EOF) {
            // Este error sigue siendo válido para casos como "2 + 3 algo"
            throw new Exception("Error de Sintaxis: Caracteres inesperados al final de la expresión o operador faltante.");
        }
        return result;
    }
}