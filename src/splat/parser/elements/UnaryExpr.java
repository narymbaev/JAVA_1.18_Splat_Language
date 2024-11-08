package splat.parser.elements;

import splat.lexer.Token;

import java.util.Map;

public class UnaryExpr extends Expression {
    private String operator;
    private Expression expr;
    private Token token;

    public UnaryExpr(String operator, Expression expr, Token token) {
        super(token);
        this.operator = operator;
        this.expr = expr;
        this.token = token;
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        Type exprType = expr.analyzeAndGetType(funcMap, varAndParamMap);

        if (exprType == null){
            return null;
        }

        if (operator.equals("-")) {
            if (exprType.equals(Type.INT) || exprType.equals(Type.FLOAT)) {
                return exprType; // Negation applies to int and float
            }
//            else {
//                throw new SemanticAnalysisException("Invalid type for unary operator '-'", this);
//            }
        } else if (operator.equals("!")) {
            if (exprType.equals(Type.BOOLEAN)) {
                return Type.BOOLEAN; // Logical NOT applies to booleans
            }
//            else {
//                throw new SemanticAnalysisException("Invalid type for unary operator '!'", this);
//            }
        } else if (operator.equals("not")) {
            if (exprType.equals(Type.BOOLEAN)) {
                return Type.BOOLEAN;
            }
        } else if (operator.equals("and")) {
            if (exprType.equals(Type.BOOLEAN)) {
                return Type.BOOLEAN;
            }
        }
//        else {
//            throw new SemanticAnalysisException("Unknown unary operator: " + operator, this);
//        }
        return null;
    }

    // Getters and other methods...
}