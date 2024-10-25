package splat.parser.elements;

import splat.lexer.Token;


public class BinaryExpr extends Expression {
    private Expression left;
    private String operator;
    private Expression right;
    private Token token;

    public BinaryExpr(Expression left, String operator, Expression right, Token token) {
        super(token);
        this.left = left;
        this.operator = operator;
        this.right = right;
        this.token = token;
    }

    // Getters and other methods...
}