package token;

import visitor.ExpressionTokenVisitor;

public interface Token {
    void accept(ExpressionTokenVisitor visitor);
}
