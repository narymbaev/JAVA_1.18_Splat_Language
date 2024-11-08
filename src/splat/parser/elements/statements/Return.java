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
        System.out.println("Return analyze started");
        Type expectedType = varAndParamMap.get("0result");

        if (expectedType == null) {
            throw new SemanticAnalysisException("Return statement is not needed", this);
        }

        System.out.println("Expected " + expectedType + " " + expr);
        if (expr == null && !expectedType.getValue().equals("void")){
            throw new SemanticAnalysisException("Type mismatch in return statement", this);
        }

        Type actualType;

        if (expr == null) {
            actualType = Type.VOID;
        } else {
            actualType = expr.analyzeAndGetType(funcMap, varAndParamMap);
        }

        System.out.println("Actual " + actualType + " " + expr);

        if (!expectedType.equals(actualType)) {
            throw new SemanticAnalysisException("Type mismatch in return statement", this);
        }

        varAndParamMap.put("1result", actualType);

    }
}
