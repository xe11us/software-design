import token.ExpressionTokenizer;
import token.Token;
import token.Tokenizer;
import visitor.CalcVisitor;
import visitor.ParserVisitor;
import visitor.PrintVisitor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Calculator {
    public static void main(String[] args) {
        File file = new File("src/main/resources/input.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("src/main/resources/output.txt"), StandardCharsets.UTF_8))) {
                calc(reader, writer);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void calc(BufferedReader reader, Writer writer) throws IOException {
        ParserVisitor parserVisitor = new ParserVisitor();
        CalcVisitor calcVisitor = new CalcVisitor();
        PrintVisitor printVisitor = new PrintVisitor(writer);

        String expression = reader.readLine();
        if (expression == null) {
            return;
        }

        InputStream stream = new ByteArrayInputStream(expression.getBytes(StandardCharsets.UTF_8));
        Tokenizer tokenizer = new ExpressionTokenizer(stream);
        List<Token> tokens = tokenizer.getTokens();
        List<Token> parsedTokens = parserVisitor.visit(tokens);
        int result = calcVisitor.visit(parsedTokens);

        printVisitor.visit(parsedTokens);
        writer.write("\n" + result);
    }
}