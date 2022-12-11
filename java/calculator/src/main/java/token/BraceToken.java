package token;

import visitor.ExpressionTokenVisitor;

public class BraceToken extends ExpressionToken {
    public BraceToken(String value) {
        super(value);
    }

    @Override
    public void accept(ExpressionTokenVisitor visitor) {
        visitor.visit(this);
    }
}
