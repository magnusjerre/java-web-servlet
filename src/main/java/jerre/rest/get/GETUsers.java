package jerre.rest.get;

import jerre.models.User;
import jerre.repositories.UserRepository;
import jerre.rest.RestEndpointPatterns;

import java.util.Collection;
import java.util.regex.Pattern;

public class GETUsers implements RestGETEndpoint<Collection<User>> {

    private UserRepository userRepository;
//    private Pattern urlPattern = Pattern.compile("\\/users$");
    private Pattern urlPattern = RestEndpointPatterns.USERS;

    public GETUsers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Pattern getUrlPattern() {
        return urlPattern;
    }

    @Override
    public Collection<User> handleRequest(String path) {
        if (!urlPattern.matcher(path).matches()) throw new IllegalArgumentException("Invalid url for GETUsers");
        return userRepository.getUsers();
    }
}
