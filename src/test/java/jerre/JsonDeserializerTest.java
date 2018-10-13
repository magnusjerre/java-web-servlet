package jerre;

import jerre.models.JsonDeserializer;
import jerre.models.User;
import org.junit.Assert;
import org.junit.Test;

public class JsonDeserializerTest {
    @Test
    public void testPasswordUsername() {
        User user = JsonDeserializer.deserializeUser(new User("magnus", "jerre").toJson());
        Assert.assertEquals("magnus", user.username);
        Assert.assertEquals("jerre", user.password);
    }

    @Test
    public void testUsernamePassword() {
        User user = JsonDeserializer.deserializeUser("{\"username\":\"magnus\",\"password\":\"jerre\"}");
        Assert.assertEquals("magnus", user.username);
        Assert.assertEquals("jerre", user.password);
    }

    @Test
    public void testConvertToRegexUsingStringsOnly() {
        Assert.assertEquals("\\{\\\"username\\\":\\\"([^\"]+)\\\",\\\"password\\\":\\\"([^\"]+)\\\"\\}",
                JsonDeserializer.convertToRegex("{\"username\":string,\"password\":string}"));
    }
}
