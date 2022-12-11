package token;

import visitor.ExpressionTokenVisitor;

public class NumberToken extends ExpressionToken {
    private final int value;

    public NumberToken(Integer value) {
        super(value.toString());
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void accept(ExpressionTokenVisitor visitor) {
        visitor.visit(this);
    }
}
