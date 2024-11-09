package splat.parser.elements;

import splat.executor.Value;
import splat.lexer.Token;

import java.util.Map;

public class LiteralExpr extends Expression {
    public LiteralExpr(Token token){
        super(token);
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        String value = getToken().getValue();

        if (value.matches("-?\\d+")) {
            return Type.INT; // Assuming an INT type exists for integers
        } else if (value.matches("-?\\d*\\.\\d+")) {
            return Type.FLOAT; // Assuming a FLOAT type exists for decimal numbers
        } else if (value.equals("true") || value.equals("false")) {
            return Type.BOOLEAN; // Assuming a BOOLEAN type exists for booleans
        } else {
            return Type.STRING; // Assuming a STRING type exists for other literals
        }
    }

    @Override
    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) {
        //FIXME
        return null;
    }
}
