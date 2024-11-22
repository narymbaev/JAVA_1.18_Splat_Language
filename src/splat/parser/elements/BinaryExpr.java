package splat.parser.elements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.BooleanValue;
import splat.executor.values.IntValue;
import splat.lexer.Token;

import javax.sound.midi.SysexMessage;
import java.util.Map;
import java.util.Objects;


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
    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall, ExecutionException {
        Value leftValue = left.evaluate(funcMap, varAndParamMap);
        Value rightValue = right.evaluate(funcMap, varAndParamMap);

        // Perform the binary operation
        switch (operator) {
            case "+":
                return new IntValue(((IntValue) leftValue).getValue() + ((IntValue) rightValue).getValue());
            case "-":
                return new IntValue(((IntValue) leftValue).getValue() - ((IntValue) rightValue).getValue());
            case "*":
                return new IntValue(((IntValue) leftValue).getValue() * ((IntValue) rightValue).getValue());
            case "/":
                if (((IntValue) rightValue).getValue() == 0) {
                    throw new ExecutionException("Division by zero", this);
                }
                return new IntValue(((IntValue) leftValue).getValue() / ((IntValue) rightValue).getValue());
            case "%":
                return new IntValue(((IntValue) leftValue).getValue() % ((IntValue) rightValue).getValue());
            case "&&":
            case "and":
                return new BooleanValue(((BooleanValue) leftValue).getValue() && ((BooleanValue) rightValue).getValue());
            case "||":
            case "or":
                return new BooleanValue(((BooleanValue) leftValue).getValue() || ((BooleanValue) rightValue).getValue());
            case "<":
                return new BooleanValue(((IntValue) leftValue).getValue() < ((IntValue) rightValue).getValue());
            case ">":
                return new BooleanValue(((IntValue) leftValue).getValue() > ((IntValue) rightValue).getValue());
            case "<=":
                return new BooleanValue(((IntValue) leftValue).getValue() <= ((IntValue) rightValue).getValue());
            case ">=":
                return new BooleanValue(((IntValue) leftValue).getValue() >= ((IntValue) rightValue).getValue());

            case "==":
                if (leftValue instanceof IntValue && rightValue instanceof IntValue) {
                    return new BooleanValue(((IntValue) leftValue).getValue() == ((IntValue) rightValue).getValue());
                } else if (leftValue instanceof BooleanValue && rightValue instanceof BooleanValue) {
                    return new BooleanValue(((BooleanValue) leftValue).getValue() == ((BooleanValue) rightValue).getValue());
                } else {
                    throw new RuntimeException("Type mismatch in equality comparison");
                }

            case "!=":
                if (leftValue instanceof IntValue && rightValue instanceof IntValue) {
                    return new BooleanValue(!Objects.equals(((IntValue) leftValue).getValue(), ((IntValue) rightValue).getValue()));
                } else if (leftValue instanceof BooleanValue && rightValue instanceof BooleanValue) {
                    return new BooleanValue(((BooleanValue) leftValue).getValue() != ((BooleanValue) rightValue).getValue());
                } else {
                    throw new RuntimeException("Type mismatch in inequality comparison");
                }

            default:
                throw new RuntimeException("Unknown operator: " + operator);
        }
    }
}