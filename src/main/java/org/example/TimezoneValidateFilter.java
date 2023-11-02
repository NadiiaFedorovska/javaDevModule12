package org.example;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.example.TimeConfig.*;

@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String timezoneParam = request.getParameter(PARAM_TIMEZONE);

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (isValidTimezone(timezoneParam)) {
            chain.doFilter(request, response);
        } else {
            handleInvalidTimezone(httpResponse);
        }
    }

    private boolean isValidTimezone(String timezoneParam) {
        return VALID_TIMEZONES.contains(timezoneParam) || timezoneParam == null;
    }

    private void handleInvalidTimezone(HttpServletResponse httpResponse) throws IOException {
        httpResponse.setContentType(CONTENT_TYPE_HTML);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        try (PrintWriter out = httpResponse.getWriter()) {
            out.println("<html>");
            out.println("<head><title>Invalid TimeZone</title></head>");
            out.println("<body>");
            out.println("<h2>Invalid TimeZone</h2>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}