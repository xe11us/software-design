package visitor;

import token.BraceToken;
import token.NumberToken;
import token.OperationToken;

public interface ExpressionTokenVisitor {
    void visit(BraceToken brace);
    void visit(NumberToken number);
    void visit(OperationToken operation);
}
