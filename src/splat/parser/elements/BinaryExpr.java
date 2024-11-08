package splat.parser.elements;

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
        System.out.println("The left " + left + " right " + right);
        Type leftType = left.analyzeAndGetType(funcMap, varAndParamMap);
        Type rightType = right.analyzeAndGetType(funcMap, varAndParamMap);
        System.out.println("The " + operator + " " + leftType + " " + rightType);
        if (leftType == null || rightType == null) {
            return null;
        }

        switch (operator) {
            case "+":
            case "-":
            case "*":
            case "/":
                if ((leftType.equals(Type.INT) || leftType.equals(Type.FLOAT)) &&
                        leftType.equals(rightType)) {
                    return leftType; // Both operands must be of the same type, int or float
                } else {
//                    throw new SemanticAnalysisException("Type mismatch for operator " + operator, this);
                    return null;
                }
            case "&&":
            case "||":
                if (leftType.equals(Type.BOOL) && rightType.equals(Type.BOOL)) {
                    return Type.BOOL;
                } else {
//                    throw new SemanticAnalysisException("Logical operators require boolean operands", this);
                    return null;
                }
            case "==":
            case "!=":
                if (leftType.equals(rightType)) {
                    return Type.BOOL; // Comparison operators result in boolean
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
                    return Type.BOOL; // Relational operators result in boolean
                } else {
                    // throw new SemanticAnalysisException("Type mismatch for relational operator " + operator, this);
                    return null;
                }

            default:
                // throw new SemanticAnalysisException("Unknown binary operator: " + operator, this);
                return null;
        }
    }

    // Getters and other methods...
}