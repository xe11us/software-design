package visitor;

import token.BraceToken;
import token.LeftBraceToken;
import token.RightBraceToken;
import token.OperationToken;
import token.NumberToken;
import token.Token;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class ParserVisitor implements ExpressionTokenVisitor {
    private final List<Token> tokens = new ArrayList<>();
    private final Stack<Token> stack = new Stack<>();

    @Override
    public void visit(BraceToken brace) {
        if (brace.getClass() == LeftBraceToken.class) {
            stack.add(brace);
        } else if (brace.getClass() == RightBraceToken.class) {
            if (stack.isEmpty()) {
                throwNotPairedBraceException();
            }
            Token current = stack.pop();
            while (!(current.getClass() == LeftBraceToken.class)) {
                tokens.add(current);
                if (stack.isEmpty()) {
                    throwNotPairedBraceException();
                }
                current = stack.pop();
            }
        }
    }

    @Override
    public void visit(NumberToken number) {
        tokens.add(number);
    }

    @Override
    public void visit(OperationToken operation) {
        if (!stack.isEmpty()) {
            Token current = stack.peek();
            while (current instanceof OperationToken &&
                    ((OperationToken) current).getPriority() >= operation.getPriority()) {
                tokens.add(stack.pop());
                if (stack.isEmpty()) {
                    break;
                }
                current = stack.peek();
            }
        }
        stack.add(operation);
    }

    public List<Token> visit(List<Token> tokens) {
        this.tokens.clear();
        for (Token token : tokens) {
            token.accept(this);
        }
        while (!stack.isEmpty()) {
            this.tokens.add(stack.pop());
        }
        return this.tokens;
    }

    private void throwNotPairedBraceException() {
        throw new IllegalStateException("Right brace without pairing left brace found");
    }
}