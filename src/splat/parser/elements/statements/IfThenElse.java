package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public class IfThenElse extends Statement {
    private Expression condition;
    private List<Statement> thenStmts;
    private List<Statement> elseStmts;

    public IfThenElse(Token tok, Expression condition, List<Statement> thenStmts, List<Statement> elseStmts) {
        super(tok);
        this.condition = condition;
        this.thenStmts = thenStmts;
        this.elseStmts = elseStmts;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        // Ensure condition is boolean
        System.out.println("AAAAAA");

        System.out.println("The cond " + condition);

        Type conditionType = condition.analyzeAndGetType(funcMap, varAndParamMap);

        System.out.println("AAAAAA " + conditionType);

        if (conditionType == null){
            throw new SemanticAnalysisException("Condition is not set properly", this);
        }

        if (!(conditionType.equals(Type.BOOL) || conditionType.equals(Type.BOOLEAN))) {
            throw new SemanticAnalysisException("Condition in if statement must be boolean", this);
        }


        // Analyze then block
        for (Statement stmt : thenStmts) {
            stmt.analyze(funcMap, varAndParamMap);
        }

        // Analyze else block, if present
        if (elseStmts != null) {
            for (Statement stmt : elseStmts) {
                stmt.analyze(funcMap, varAndParamMap);
            }
        }
    }
}