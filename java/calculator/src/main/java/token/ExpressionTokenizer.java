package token;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import token.Tokenizer;

public class ExpressionTokenizer implements Tokenizer {
    private final InputStreamReader reader;
    private char currentSymbol;

    public ExpressionTokenizer(InputStream inputStream) {
        this.reader = new InputStreamReader(inputStream);
        currentSymbol = getNextNotWhitespaceSymbol();
    }

    private static final char END_CHAR = '\uFFFF';
    private static final Set<Character> notNumberChars = Set.of('(', ')', '+', '-', '*', '/');

    private State state = State.Start;
    private Integer numberAccumulator = null;

    @Override
    public Token next() {
        if (state == State.Start) {
            if (currentSymbol == END_CHAR) {
                state = State.End;
                return null;
            }
            if (notNumberChars.contains(currentSymbol)) {
                Token token = toToken();
                currentSymbol = getNextNotWhitespaceSymbol();
                return token;
            }
            if (Character.isDigit(currentSymbol)) {
                state = State.Number;
                numberAccumulator = Character.getNumericValue(currentSymbol);
                currentSymbol = getNextNotWhitespaceSymbol();
                return next();
            }
            state = State.Error;
            throw new IllegalArgumentException(String.format("Unexpected character: %c.", currentSymbol));
        }
        if (state == State.Number) {
            if (Character.isDigit(currentSymbol)) {
                numberAccumulator = 10 * numberAccumulator + Character.getNumericValue(currentSymbol);
                currentSymbol = getNextNotWhitespaceSymbol();
                return next();
            }
            state = State.Start;
            Integer number = numberAccumulator;
            numberAccumulator = null;
            return new NumberToken(number);
        }
        if (state == State.Error) {
            throw new IllegalStateException(String.format("Unexpected character: %c.", currentSymbol));
        }
        return null; /* state == State.End */
    }

    @Override
    public List<Token> getTokens() {
        List<Token> result = new ArrayList<>();
        Token currentToken = next();
        while (currentToken != null) {
            result.add(currentToken);
            currentToken = next();
        }
        return result;
    }

    private Token toToken() {
        switch (currentSymbol) {
            case '(': {
                return new LeftBraceToken();
            }
            case ')': {
                return new RightBraceToken();
            }
            case '+': {
                return new AddToken();
            }
            case '-': {
                return new SubtractToken();
            }
            case '*': {
                return new MultiplyToken();
            }
            case '/': {
                return new DivideToken();
            }
            default: {
                throw new IllegalStateException(String.format("Unexpected character: %c.", currentSymbol));
            }
        }
    }

    private char getNextNotWhitespaceSymbol() {
        Objects.requireNonNull(reader);
        char currentSymbol;
        try {
            currentSymbol = (char) reader.read();
            while (Character.isWhitespace(currentSymbol)) {
                currentSymbol = (char) reader.read();
            }
            return currentSymbol;
        } catch (IOException e) {
            state = State.Error;
            e.printStackTrace();
            return '\0';
        }
    }

    private enum State {
        Start,
        Number,
        Error,
        End
    }
}
