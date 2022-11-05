package ru.akirakozov.sd.refactoring.utils;

import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

public class HTMLUtils {
    public static void writeHTMLDocument(PrintWriter writer, List<String> headers, String text) {
        writer.print("<html><body>\n");
        headers.forEach(header -> writer.println("<h1>" + header + "</h1>"));
        if (!Objects.isNull(text) && text.length() > 0) {
            writer.println(text);
        }
        writer.print("</body></html>\n");
    }
}
