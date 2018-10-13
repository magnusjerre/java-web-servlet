package jerre.rest.get;

import jerre.models.User;
import jerre.repositories.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GETUser implements RestGETEndpoint<User> {

    private UserRepository userRepository;
    private Pattern pattern = Pattern.compile("^\\/user\\/([\\w\\d]+)$");

    public GETUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Pattern getUrlPattern() {
        return pattern;
    }

    @Override
    public User handleRequest(String path) {
        validatePath(path);
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            String userId = matcher.group(1);
            return userRepository.getUsers().stream()
                    .filter(user -> user.username.equalsIgnoreCase(userId))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
