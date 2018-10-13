package jerre.rest.post;

import jerre.models.JsonDeserializer;
import jerre.models.User;
import jerre.services.UserService;

import java.util.regex.Pattern;

public class POSTUser implements RestPOSTEndpoint<User, User> {

    private UserService userService;
    private Pattern urlPattern = Pattern.compile("^\\/user$");

    public POSTUser(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User handleRequest(String path, String body) {
        validatePath(path);
        User newUser = JsonDeserializer.deserializeUser(body);
        return userService.addUser(newUser);
    }

    @Override
    public Class<User> getBodyType() {
        return User.class;
    }

    @Override
    public Pattern getUrlPattern() {
        return urlPattern;
    }
}
