package jerre.servlets;

import jerre.HtmlParser;
import jerre.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HelloWorld extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HelloWorld.class.getCanonicalName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "HelloWorld doGet(..)-method called on thread: " + Thread.currentThread().getName());
        resp.setContentType("text/html");

        HashMap<String, Object> variables = new HashMap<>();
        variables.put("username", getNameToGreet(req));
        LOGGER.info("Adding the following variables to hello.html: " +
                variables.entrySet().stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.joining(","))
        );

        String helloPage = Utils.readResource(HelloWorld.class.getResource("/static/html/hello.html"));
        String parsedHtml = HtmlParser.parseHtml(helloPage, variables);
        LOGGER.info("Parsed html:");
        LOGGER.info(parsedHtml);
        resp.getWriter().print(parsedHtml);
    }

    private String getNameToGreet(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Object userAttribute = session.getAttribute("user");
        String userToWelcome = "world";
        if (userAttribute != null) {
            userToWelcome = (String) userAttribute;
        }
        return userToWelcome;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        LOGGER.log(Level.INFO, "HelloWorld init()-method called");
    }

    @Override
    public void destroy() {
        super.destroy();
        LOGGER.log(Level.INFO, "HelloWorld destroy()-method called");
    }
}
