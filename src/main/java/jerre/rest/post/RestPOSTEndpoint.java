package jerre.rest.post;

import jerre.rest.RestEndpoint;

public interface RestPOSTEndpoint<ReturnType, BodyType> extends RestEndpoint {
    ReturnType handleRequest(String path, String body);
    Class<BodyType> getBodyType();
//    default void validateBodyType(Object o) {
//        if (!getBodyType().isInstance(o)) throw new IllegalArgumentException("Body not matching the exepected type: " + getBodyType());
//    }
    default void validate(String path, Object o) {
        validatePath(path);
//        validateBodyType(o);
    }
}
