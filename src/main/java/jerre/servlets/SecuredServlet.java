package jerre.servlets;

import jerre.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecuredServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null || !req.getPathInfo().endsWith(".html")) throw new ServletException("Illegal path, must specifiy a static file");

        resp.setStatus(resp.SC_OK);
        resp.setContentType("text/html");
        resp.getWriter().println(Utils.readResource(SecuredServlet.class.getResource("/static/html/secured" + req.getPathInfo())));
    }
}
