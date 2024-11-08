package splat.parser.elements;

import java.util.List;
import java.util.Map;

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

    public List<Expression> getArguments() {
        return arguments;
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        FunctionDecl function = funcMap.get(functionName);

        if (function == null) {
//            throw new SemanticAnalysisException("Undefined function: " + functionName, this);
            return null;
        }

        // Check if argument types match parameter types
        List<Type> paramTypes = function.getParameterTypes();
        if (arguments.size() != paramTypes.size()) {
//            throw new SemanticAnalysisException("Argument count mismatch in function call: " + functionName, this);
            return null;
        }

        for (int i = 0; i < arguments.size(); i++) {
            Type argType = arguments.get(i).analyzeAndGetType(funcMap, varAndParamMap);
            if (!argType.equals(paramTypes.get(i))) {
//                throw new SemanticAnalysisException("Argument type mismatch in function call: " + functionName, this);
                return null;
            }
        }

        return function.getReturnType();
    }
}