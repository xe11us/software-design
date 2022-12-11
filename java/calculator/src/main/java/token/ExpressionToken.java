package token;

import visitor.ExpressionTokenVisitor;

import java.util.Objects;

public abstract class ExpressionToken implements Token {
    private final String value;

    public ExpressionToken(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass() && Objects.equals(((ExpressionToken) obj).value, value);
    }

    @Override
    public String toString() {
        return value;
    }
}
