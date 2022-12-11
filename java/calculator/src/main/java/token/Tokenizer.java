package token;

import java.util.List;

public interface Tokenizer {
    Token next();
    List<Token> getTokens();
}
