package jerre.filters;

import jerre.servlets.LoginForm;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class.getCanonicalName());
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.log(Level.INFO, "AuthenticationFilter init()-method called");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.log(Level.INFO, "AuthenticationFilter doFilter(...)-method called");
        if (!(request instanceof HttpServletRequest)) throw new ServletException("Invalid request type");

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        HttpSession session = httpRequest.getSession(false);
        if (session == null) throw new ServletException("User doesn't have a valid session");

        LocalDateTime dateTime = (LocalDateTime) session.getAttribute(LoginForm.LOGIN_VALID_TIME);
        if (Duration.between(dateTime, LocalDateTime.now()).toMinutes() > 5) {
            session.setAttribute(LoginForm.LOGIN_VALID_TIME, LocalDateTime.now().plusMinutes(10));
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOGGER.log(Level.INFO, "AuthenticationFilter destroy()-method called");
    }
}
