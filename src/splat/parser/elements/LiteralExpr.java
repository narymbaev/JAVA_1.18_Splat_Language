package splat.parser.elements;

import splat.lexer.Token;

public class LiteralExpr extends Expression {
    public LiteralExpr(Token token){
        super(token);
    }
}
