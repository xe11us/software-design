package ru.akirakozov.sd.refactoring.utils;

import java.io.PrintWriter;
import java.util.List;

public class HTMLUtils {
    public static void writeHTMLDocument(PrintWriter writer, List<String> headers, String text) {
        writer.println("<html><body>");
        headers.forEach(header -> writer.println("<h1>" + header + "</h1>"));
        writer.println(text);
        writer.println("</body></html>");
    }
}
