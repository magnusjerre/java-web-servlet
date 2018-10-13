package jerre;

import jerre.repositories.UserRepository;
import jerre.services.UserService;

import java.util.logging.Logger;

public class ContextLoader {

    private static final Logger LOGGER = Logger.getLogger(ContextLoader.class.getCanonicalName());
    private static ContextLoader contextLoader;

    private final UserRepository userRepository;
    private final UserService userService;


    private ContextLoader(){
        userRepository = new UserRepository();
        LOGGER.info(String.format("Adding a new %s to the context", UserRepository.class.getCanonicalName()));
        userService = new UserService(userRepository);
        LOGGER.info(String.format("Adding a new %s to the context", UserService.class.getCanonicalName()));
    }

    public static ContextLoader getInstance() {
        return contextLoader == null ? contextLoader = new ContextLoader() : contextLoader;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public UserService getUserService() {
        return userService;
    }

}
