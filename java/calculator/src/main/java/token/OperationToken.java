package token;

import visitor.ExpressionTokenVisitor;

public class OperationToken extends ExpressionToken {
    private final int priority;

    public OperationToken(String value, int priority) {
        super(value);
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public void accept(ExpressionTokenVisitor visitor) {
        visitor.visit(this);
    }
}
