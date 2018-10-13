package jerre.repositories;

import jerre.models.User;

import java.util.Collection;
import java.util.HashSet;

public class UserRepository {

    private final Collection<User> allUsers;

    public UserRepository() {
        this.allUsers = new HashSet<>();
        allUsers.add(new User("user", "user"));
        allUsers.add(new User("jerre", "jerre"));
    }

    public Collection<User> getUsers() {
        return allUsers;
    }

    public User addUser(User user) {
        if (allUsers.add(user)) {
            return user;
        }
        throw new IllegalArgumentException("Couldn't add user");
    }

}
