package jerre.servlets;

import jerre.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ErrorHandler extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ErrorHandler.class.getCanonicalName());
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) req.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) req.getAttribute("javax.servlet.error.servlet_name");

        LOGGER.log(Level.INFO, String.format("exception-class: %s, exception-message: %s, status code: %d, servlet name: %s",
                throwable.getClass().getCanonicalName(),
                throwable.getMessage(),
                statusCode,
                servletName));

        resp.setStatus(statusCode);
        resp.setContentType("text/html");
        resp.getWriter().println(Utils.readResource(ErrorHandler.class.getResource("/static/html/errorpage.html")));
    }

    /**
     * This one is needed to intercept any post-calls that have failed
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
