package jerre.models;

import java.util.Objects;

public class User implements JsonSerializable<User>, XmlSerializable<User> {

    public final String username;
    public final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public String toJson() {
        return String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
    }

    @Override
    public String toXml() {
        return String.format("<person><username>%s</username><password>%s</password></person>",
                username,
                password
        );
    }
}
