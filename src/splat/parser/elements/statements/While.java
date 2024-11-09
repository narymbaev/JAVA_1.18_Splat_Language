package splat.parser.elements.statements;

import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public class While extends Statement {
    private Expression condition;
    private List<Statement> stmts;

    public While(Token tok, Expression condition, List<Statement> stmts) {
        super(tok);
        this.condition = condition;
        this.stmts = stmts;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type conditionType = condition.analyzeAndGetType(funcMap, varAndParamMap);


        if (conditionType == null) {
            throw new SemanticAnalysisException("While loop condition must be a boolean", this);
        }

        // Check if the condition type is boolean
        if (!conditionType.equals(Type.BOOL) && !conditionType.equals(Type.BOOLEAN)) {
            throw new SemanticAnalysisException("While loop condition must be a boolean", this);
        }

        // Analyze the loop body
        for (Statement stmt : stmts) {
            stmt.analyze(funcMap, varAndParamMap);
        }

    }

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall {
        // FIXME
    }
}
