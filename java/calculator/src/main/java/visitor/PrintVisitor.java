package visitor;

import token.BraceToken;
import token.NumberToken;
import token.OperationToken;
import token.Token;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class PrintVisitor implements ExpressionTokenVisitor {
    private final Writer writer;

    public PrintVisitor(Writer writer) {
        this.writer = writer;
    }
    @Override
    public void visit(BraceToken brace) {
        write(brace.toString() + " ");
    }

    @Override
    public void visit(NumberToken number) {
        write(number.toString() + " ");
    }

    @Override
    public void visit(OperationToken operation) {
        write(operation.toString() + " ");
    }

    public void visit(List<Token> tokens) {
        for (Token token: tokens) {
            token.accept(this);
        }
        try {
            writer.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void write(String message) {
        try {
            writer.write(message);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
