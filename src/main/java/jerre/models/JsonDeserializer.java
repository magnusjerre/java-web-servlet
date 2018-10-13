package jerre.models;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonDeserializer {
    private static final Logger LOGGER = Logger.getLogger(JsonDeserializer.class.getCanonicalName());

    private static final String USER_PATTERN_1_SIMPLE = "^{\"username\":string,\"password\":string}$";
    private static final String USER_PATTERN_2_SIMPLE = "^{\"password\":string,\"username\":string}$";
    private static final Pattern USER_PATTERN_1 = Pattern.compile(convertToRegex(USER_PATTERN_1_SIMPLE));
    private static final Pattern USER_PATTERN_2 = Pattern.compile(convertToRegex(USER_PATTERN_2_SIMPLE));

    public static User deserializeUser(String json) {
        Matcher matcher1 = USER_PATTERN_1.matcher(json);
        if (matcher1.matches()) {
            return new User(matcher1.group(1), matcher1.group(2));
        }
        Matcher matcher2 = USER_PATTERN_2.matcher(json);
        if (matcher2.matches()) {
            return new User(matcher2.group(1), matcher2.group(2));
        }
        LOGGER.log(Level.INFO, String.format("JSON %s doesn't match any of the user-patterns", json));
        throw new IllegalArgumentException("Can't deserialize json");
    }

    public static String convertToRegex(String samplePattern) {
        return samplePattern
                .replaceAll("\\{", "\\\\{")
                .replaceAll("\\}", "\\\\}")
                .replaceAll("\\\"", "\\\\\"")
                .replaceAll("string", "\\\\\"([^\\\"]+)\\\\\"");
    }
}
