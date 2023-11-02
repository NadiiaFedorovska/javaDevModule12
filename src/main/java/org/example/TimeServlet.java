package org.example;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import static org.example.TimeConfig.*;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;
    protected int zoneId;
    String currentTime;
    String stringZoneId;


    @Override
    public void init() {
        engine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("d:/my_folder/goit/javadevmodule12/src/main/resources/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);

        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(CONTENT_TYPE_HTML);
        String timezoneParam = request.getParameter(PARAM_TIMEZONE);
        TimeZone timezone = TimeZone.getTimeZone(DEFAULT_TIMEZONE);

        checkTimezoneParam(request, response, timezoneParam, timezone);
        formatData(timezone);
        useTemplate(request, response);
    }

    protected void formatData(TimeZone timezone) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setTimeZone(timezone);
        currentTime = dateFormat.format(new Date());
    }

    protected void useTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("current", currentTime);
        params.put("zone", zoneId);

        Context simpleContext = new Context(request.getLocale(), params);

        engine.process("time", simpleContext, response.getWriter());
        response.getWriter().close();
    }

    private void checkTimezoneParam(HttpServletRequest request, HttpServletResponse response,
                                    String timezoneParam, TimeZone timezone) {
        if (timezoneParam != null) {
            if (timezoneParam.equals("+")) {
                stringZoneId = timezoneParam.substring(4).trim();
            } else {
                stringZoneId = timezoneParam.substring(3).trim();
            }
            zoneId = Integer.parseInt(stringZoneId);
            timezone.setRawOffset(zoneId * MILLISECONDS_IN_HOUR);

            Cookie timeZoneCookie = new Cookie("lastTimeZone", stringZoneId);
            response.addCookie(timeZoneCookie);
        } else {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lastTimeZone")) {
                    timezone.setRawOffset(zoneId * MILLISECONDS_IN_HOUR);
                    break;
                }
            }

            if (timezone == null) {
                timezone = TimeZone.getTimeZone(DEFAULT_TIMEZONE);
            }
        }
    }
}
