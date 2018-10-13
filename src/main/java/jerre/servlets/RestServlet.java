package jerre.servlets;

import jerre.ContextLoader;
import jerre.MimeType;
import jerre.Utils;
import jerre.rest.get.GETUser;
import jerre.rest.get.GETUsers;
import jerre.rest.get.RestGETEndpoint;
import jerre.rest.options.RestOPTIONSEndpoint;
import jerre.rest.post.POSTUser;
import jerre.rest.post.RestPOSTEndpoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jerre.Utils.extractPostInput;
import static jerre.Utils.writeJson;
import static jerre.Utils.writeXml;

public class RestServlet extends HttpServlet {

    private static Logger LOGGER = Logger.getLogger(RestServlet.class.getCanonicalName());
    private ContextLoader contextLoader;
    private List<RestGETEndpoint<?>> getEndpoints;
    private List<RestPOSTEndpoint<?, ?>> postEndpoints;
    private RestOPTIONSEndpoint optionEndPoint;

    @Override
    public void init() throws ServletException {
        contextLoader = ContextLoader.getInstance();
        getEndpoints = new ArrayList<>();
        postEndpoints = new ArrayList<>();
        addRestEndpoint(new GETUsers(contextLoader.getUserRepository()));
        addRestEndpoint(new GETUser(contextLoader.getUserRepository()));
        addRestPostEndpoint(new POSTUser(contextLoader.getUserService()));
        optionEndPoint = new RestOPTIONSEndpoint(getEndpoints, postEndpoints);
    }

    private void addRestEndpoint(RestGETEndpoint<?> restGETEndpoint) {
        getEndpoints.add(restGETEndpoint);
        LOGGER.log(Level.INFO, String.format("Added REST GET endpoint: /rest%s\n", restGETEndpoint.getUrlPattern()));
    }

    private void addRestPostEndpoint(RestPOSTEndpoint<?, ?> restPOSTEndpoint) {
        postEndpoints.add(restPOSTEndpoint);
        LOGGER.log(Level.INFO, String.format("Added REST POST endpoint: /rest%s\n", restPOSTEndpoint.getUrlPattern()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String pathToMatch = req.getPathInfo();
        LOGGER.log(Level.INFO, "GET-message received for path: " + pathToMatch);
        for (RestGETEndpoint<?> endpoint : getEndpoints) {
            if (endpoint.isPathMatch(pathToMatch)) {
                LOGGER.log(Level.INFO, "Found endpoint match for " + pathToMatch);
                Object result = endpoint.handleRequest(req.getPathInfo());
                LOGGER.log(Level.INFO, String.format("GET-result, classtype: %s. Result: %s", result.getClass().getCanonicalName(), result.toString()));
                MimeType supportedMimeType = MimeType.findFirstSupportedMimeType(Utils.getAcceptHeader(req));
                LOGGER.log(Level.INFO, "Supported mimetype is " + supportedMimeType.acceptedMimeType);
                switch (supportedMimeType) {
                    case JSON: {
                        writeJson(result, resp);
                        break;
                    }
                    case XML_APPLICATION: {
                        writeXml(result, resp);
                        break;
                    }
                    case XML_TEXT: {
                        writeXml(result, resp);
                        break;
                    }
                    default: {
                        LOGGER.log(Level.INFO, "Failed to return response with requested mimetype " + supportedMimeType.acceptedMimeType);
                        throw new IllegalArgumentException("We can't create the request mimetype = " + supportedMimeType.acceptedMimeType);
                    }
                }

                resp.setStatus(resp.SC_OK);
                resp.setContentType(supportedMimeType.acceptedMimeType);
                LOGGER.log(Level.INFO, String.format("Responding with mimetype %s and status %d", resp.getContentType(), resp.SC_OK));
                return;
            }
        }
        LOGGER.log(Level.INFO, String.format("Invalid GET path: %s%s", req.getServletPath(), req.getPathInfo()));
        throw new ServletException(String.format("Invalid GET path: %s%s", req.getServletPath(), req.getPathInfo()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String pathToMatch = req.getPathInfo();
        LOGGER.log(Level.INFO, "POST-message received for path: " + pathToMatch);
        for (RestPOSTEndpoint<?, ?> postEndpoint : postEndpoints) {
            if (postEndpoint.isPathMatch(pathToMatch)) {
                LOGGER.log(Level.INFO, "Found endpoint match for " + pathToMatch);
                String postDataReader = extractPostInput(req);
                LOGGER.log(Level.INFO, "Successfully extracted post-data");
                Object result = postEndpoint.handleRequest(pathToMatch, postDataReader);
                LOGGER.log(Level.INFO, String.format("POST-result, classtype: %s. Result: %s", result.getClass().getCanonicalName(), result.toString()));
                writeJson(result, resp);
                resp.setContentType(MimeType.JSON.acceptedMimeType);
                resp.setStatus(resp.SC_OK);
                LOGGER.log(Level.INFO, String.format("Responding with mimetype %s and status %d", resp.getContentType(), resp.SC_OK));
                return;
            }
        }
        LOGGER.log(Level.INFO, String.format("Invalid POST path: %s%s", req.getServletPath(), req.getPathInfo()));
        throw new ServletException(String.format("Invalid POST path: %s%s", req.getServletPath(), req.getPathInfo()));
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String pathToMatch = req.getPathInfo();
        LOGGER.info("OPTIONS-message received for path " + pathToMatch);
        if (optionEndPoint.isPathMatch(pathToMatch)) {
            LOGGER.info("Found endpoint match for " + pathToMatch + " in " + optionEndPoint.getClass().getCanonicalName());
            resp.setHeader("Allow", optionEndPoint.handleRequest(pathToMatch));
            resp.setStatus(resp.SC_OK);
            return;
        }
        LOGGER.info(String.format("Invalid OPTIONS path: %s%s", req.getServletPath(), pathToMatch));
        throw new ServletException(String.format("Invalid OPTIONS path: %s%s", req.getServletPath(), pathToMatch));
    }
}
