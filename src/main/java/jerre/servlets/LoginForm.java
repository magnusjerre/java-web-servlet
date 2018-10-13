package jerre.servlets;

import jerre.ContextLoader;
import jerre.HtmlParser;
import jerre.Utils;
import jerre.models.User;
import jerre.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginForm extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginForm.class.getCanonicalName());

    public static final String LOGIN_VALID_TIME = "LOGIN_VALID_TIME";
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = ContextLoader.getInstance().getUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("loginerror", "");

        String loginForm = Utils.readResource(LoginForm.class.getResource("/static/html/login.html"));
        String generatedHtml = HtmlParser.parseHtml(loginForm, variables);

        resp.setContentType("text/html");
        resp.getWriter().print(generatedHtml);
        resp.setStatus(resp.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "Post received");
        String iUsername = req.getParameter("username");
        String iPassword = req.getParameter("password");

        Optional<User> user = userService.getUsers().stream().filter(u -> u.username.equalsIgnoreCase(iUsername)).findFirst();
        if (!user.isPresent() || !user.get().password.equalsIgnoreCase(iPassword)) {
            LOGGER.log(Level.INFO, "Invalid username / password");
            HashMap<String, Object> variables = new HashMap<>();
            variables.put("loginerror", "<p class=\"error\">Invalid username or password</p>");
            String loginForm = Utils.readResource(LoginForm.class.getResource("/static/html/login.html"));
            String generatedHtml = HtmlParser.parseHtml(loginForm, variables);
            resp.getWriter().print(generatedHtml);
            resp.setStatus(resp.SC_UNAUTHORIZED);
            resp.setContentType("text/html");
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("user", user.get().username);
        session.setAttribute(LOGIN_VALID_TIME, LocalDateTime.now());

        resp.sendRedirect("hello");
    }
}
