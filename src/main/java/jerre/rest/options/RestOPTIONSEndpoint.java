package jerre.rest.options;

import jerre.rest.RestEndpoint;
import jerre.rest.get.RestGETEndpoint;
import jerre.rest.post.RestPOSTEndpoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RestOPTIONSEndpoint implements RestEndpoint {

    private static final Logger LOGGER = Logger.getLogger(RestOPTIONSEndpoint.class.getCanonicalName());

    private final Collection<? extends RestEndpoint>[] endPointCollections;

    public RestOPTIONSEndpoint(Collection<? extends RestEndpoint>... endPointCollection) {
        this.endPointCollections = endPointCollection;
    }

    @Override
    public boolean isPathMatch(String pathToMatch) {
        for (Collection<? extends RestEndpoint> endpointCollection : endPointCollections) {
            for (RestEndpoint endpoint : endpointCollection) {
                if (endpoint.isPathMatch(pathToMatch)) return true;
            }
        }
        return false;
    }

    @Override
    public Pattern getUrlPattern() {
        return null;
    }

    public String handleRequest(String path) {
        List<String> methods = new ArrayList<>();
        for (Collection<? extends RestEndpoint> endpointCollection : endPointCollections) {
            for (RestEndpoint endpoint : endpointCollection) {
                if (endpoint.isPathMatch(path)) {
                    if (endpoint instanceof RestPOSTEndpoint) {
                        methods.add("POST");
                        break;
                    } else if (endpoint instanceof RestGETEndpoint) {
                        methods.add("GET");
                        break;
                    }
                    LOGGER.info("Found path match for " + path + ", but didn't find the type of endpoint (GET, POST..)");
                    break;
                }
            }
        }
        return methods.stream().collect(Collectors.joining(","));
    }
}
