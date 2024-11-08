package splat.parser.elements;

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
}
