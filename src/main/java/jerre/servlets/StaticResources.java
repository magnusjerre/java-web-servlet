package jerre.servlets;

import jerre.FileType;
import jerre.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

public class StaticResources extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null) throw new ServletException("Illegal path, must specifiy a static file");

        FileType type = FileType.fromFileName(req.getPathInfo());
        resp.setContentType(type.contentType);
        URL resource = HelloWorld.class.getResource(type.path + req.getPathInfo());
        if (resource == null) throw new ServletException("File not found");
        resp.getWriter().println(Utils.readResource(resource));
    }

}
