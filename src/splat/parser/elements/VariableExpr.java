package splat.parser.elements;

import splat.executor.Value;
import splat.lexer.Token;

import java.util.Map;

public class VariableExpr extends Expression {
    public VariableExpr(Token token){
        super(token);
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        String varName = getToken().getValue();
        Type varType = varAndParamMap.get(varName);

        return varType;
    }

    @Override
    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) {
        if (!varAndParamMap.containsKey(getToken().getValue())) {
            throw new RuntimeException("Variable not defined: " + getToken().getValue());
        }
        return varAndParamMap.get(getToken().getValue());
    }
}
