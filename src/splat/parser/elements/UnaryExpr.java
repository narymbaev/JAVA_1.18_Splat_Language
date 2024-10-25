package splat.parser.elements;

import splat.lexer.Token;

public class UnaryExpr extends Expression {
    private String operator;
    private Expression expr;
    private Token token;

    public UnaryExpr(String operator, Expression expr, Token token) {
        super(token);
        this.operator = operator;
        this.expr = expr;
        this.token = token;
    }

    // Getters and other methods...
}