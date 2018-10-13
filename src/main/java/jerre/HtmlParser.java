package jerre;

import java.util.Map;

public class HtmlParser {

    public static String parseHtml(String htmlContent, Map<String, Object> values) {
        String output = htmlContent;

        for (String key : values.keySet()) {
            output = output.replaceAll("\\$\\{"+ key + "\\}", values.get(key).toString());
        }
        return output;
    }
}
