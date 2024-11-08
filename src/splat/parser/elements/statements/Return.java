package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class Return extends Statement {
    private Expression expr;

    public Return(Token tok, Expression expr) {
        super(tok);
        this.expr = expr;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        // Get the function return type
        Type expectedType = varAndParamMap.get("0result");

        if (expectedType == null) {
            throw new SemanticAnalysisException("Return statement is not neede", this);
        }

        System.out.println("Expected " + expectedType.getValue());
        if (expr == null && !expectedType.getValue().equals("Void")){
            throw new SemanticAnalysisException("Type mismatch in return statement", this);
        }

        // Ensure return expression type matches the function's return type
        Type actualType = expr.analyzeAndGetType(funcMap, varAndParamMap);

        System.out.println("Actual " + actualType.getValue());

        if (!expectedType.equals(actualType)) {
            throw new SemanticAnalysisException("Type mismatch in return statement", this);
        }

        varAndParamMap.put("1result", actualType);

    }
}
