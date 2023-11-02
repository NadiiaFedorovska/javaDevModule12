package org.example;

import org.thymeleaf.TemplateEngine;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/")
public class HelloServlet extends HttpServlet {

     @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        try (PrintWriter out = resp.getWriter()) {
            out.println("<html>");
            out.println("<head><title>HELLO</title></head>");
            out.println("<body>");
            out.println("<h1>HELLO</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}