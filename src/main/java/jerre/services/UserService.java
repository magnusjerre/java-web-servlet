package jerre.services;

import jerre.models.User;
import jerre.repositories.UserRepository;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getCanonicalName());

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Collection<User> getUsers() {
        return userRepository.getUsers();
    }

    public User addUser(User user) {
        LOGGER.log(Level.INFO, "Trying to get user " + user.username);
        return userRepository.addUser(user);
    }

}
