package jerre.rest.get;

import jerre.rest.RestEndpoint;

public interface RestGETEndpoint<T> extends RestEndpoint {
    T handleRequest(String path);
}
