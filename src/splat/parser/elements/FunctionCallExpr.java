package splat.parser.elements;

import java.util.List;
import splat.lexer.Token;

public class FunctionCallExpr extends Expression {
    private String functionName;
    private List<Expression> arguments;
    private Token token;

    public FunctionCallExpr(String functionName, List<Expression> arguments, Token token) {
        super(token);
        this.functionName = functionName;
        this.arguments = arguments;
        this.token = token;
    }
    // Getters and other methods...

    public Token getToken() {
        return token;
    }
}