package visitor;

import token.BraceToken;
import token.NumberToken;
import token.OperationToken;
import token.Token;
import token.AddToken;
import token.SubtractToken;
import token.MultiplyToken;
import token.DivideToken;

import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;

public class CalcVisitor implements ExpressionTokenVisitor {
    public final Stack<Integer> stack = new Stack<>();

    @Override
    public void visit(BraceToken brace) {
    }

    @Override
    public void visit(NumberToken number) {
        stack.add(number.getValue());
    }

    @Override
    public void visit(OperationToken operation) {
        if (stack.size() < 2) {
            throw new IllegalArgumentException("Less than 2 arguments, operation " + operation.toString());
        }
        int second = stack.pop();
        int first = stack.pop();
        stack.add(getOperationResult(operation).apply(first, second));
    }

    public int visit(List<Token> tokens) {
        stack.clear();
        for (Token token: tokens) {
            token.accept(this);
        }
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Incorrect expression");
        }
        return stack.peek();
    }

    private BiFunction<Integer, Integer, Integer> getOperationResult(OperationToken operation) {
        if (operation.getClass() == AddToken.class) {
            return Integer::sum;
        } else if (operation.getClass() == SubtractToken.class) {
            return (a, b) -> a - b;
        } else if (operation.getClass() == MultiplyToken.class) {
            return (a, b) -> a * b;
        } else if (operation.getClass() == DivideToken.class) {
            return (a, b) -> a / b;
        } else {
            throw new IllegalArgumentException("Unknown operation " + operation);
        }
    }
}
