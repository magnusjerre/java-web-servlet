package jerre.rest;

import java.util.regex.Pattern;

public interface RestEndpoint {
    Pattern getUrlPattern();
    default boolean isPathMatch(String pathToMatch) {
        return getUrlPattern().matcher(pathToMatch).matches();
    }
    default void validatePath(String path) {
        if (!isPathMatch(path)) throw new IllegalArgumentException("Invalid path for " + this.getClass().getCanonicalName());
    }
}
