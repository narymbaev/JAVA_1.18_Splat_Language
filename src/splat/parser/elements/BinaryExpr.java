package splat.parser.elements;

import splat.executor.Value;
import splat.lexer.Token;

import javax.sound.midi.SysexMessage;
import java.util.Map;


public class BinaryExpr extends Expression {
    private Expression left;
    private String operator;
    private Expression right;
    private Token token;

    public BinaryExpr(Expression left, String operator, Expression right, Token token) {
        super(token);
        this.left = left;
        this.operator = operator;
        this.right = right;
        this.token = token;
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        Type leftType = left.analyzeAndGetType(funcMap, varAndParamMap);
        Type rightType = right.analyzeAndGetType(funcMap, varAndParamMap);
        if (leftType == null || rightType == null) {
            return null;
        }

        switch (operator) {
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
                if ((leftType.equals(Type.INT) || leftType.equals(Type.FLOAT)) &&
                        leftType.equals(rightType)) {
                    return leftType; // Both operands must be of the same type, int or float
                } else {
//                    throw new SemanticAnalysisException("Type mismatch for operator " + operator, this);
                    return null;
                }
            case "&&":
            case "||":
            case "or":
            case "and":
                if (leftType.equals(Type.BOOLEAN) && rightType.equals(Type.BOOLEAN)) {
                    return Type.BOOLEAN;
                } else {
//                    throw new SemanticAnalysisException("Logical operators require boolean operands", this);
                    return null;
                }
            case "==":
            case "!=":
                if (leftType.equals(rightType)) {
                    return Type.BOOLEAN; // Comparison operators result in boolean
                } else {
//                    throw new SemanticAnalysisException("Type mismatch for comparison operator " + operator, this);
                    return null;
                }
            case "<":
            case ">":
            case "<=":
            case ">=":
                if ((leftType.equals(Type.INT) || leftType.equals(Type.FLOAT) || leftType.equals(Type.STRING)) &&
                        leftType.equals(rightType)) {
                    return Type.BOOLEAN; // Relational operators result in boolean
                } else {
                    // throw new SemanticAnalysisException("Type mismatch for relational operator " + operator, this);
                    return null;
                }

            default:
                // throw new SemanticAnalysisException("Unknown binary operator: " + operator, this);
                return null;
        }
    }

    @Override
    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) {
        //FIXME
        return null;
    }

    // Getters and other methods...
}