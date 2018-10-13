package jerre;

import jerre.models.JsonSerializable;
import jerre.models.XmlSerializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class.getCanonicalName());

    public static String readResource(URL url) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String str = null;
            while ((str = br.readLine()) != null) {
                builder.append(str);
            }
        }
        return builder.toString();
    }

    public static String[] getAcceptHeader(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return splitMimeTypes(accept);
    }

    public static String[] splitMimeTypes(String mimetypes) {
        return mimetypes.split(",\\s*");
    }

    public static void writeJson(Object obj, HttpServletResponse resp) throws IOException {
        if (obj instanceof JsonSerializable) {
            resp.getWriter().print(((JsonSerializable) obj).toJson());
            return;
        }
        if (obj instanceof Collection) {
            Collection collection = ((Collection) obj);
            if (collection.isEmpty()) {
                resp.getWriter().print("[]");
                return;
            } else if (collection.stream().anyMatch(cObj -> cObj instanceof JsonSerializable)) {
                resp.getWriter().print(String.format("[%s]",
                        collection.stream()
                                .filter(cObj -> cObj instanceof JsonSerializable)
                                .map(cObj -> ((JsonSerializable) cObj).toJson())
                                .collect(Collectors.joining(","))));
                return;
            }
        }
        LOGGER.log(Level.WARNING, "Attempted to create a JSON response for an object that isn't an instance of JsonSerializable");
        throw new IllegalArgumentException("Error creating JSON");
    }

    public static void writeXml(Object obj, HttpServletResponse resp) throws IOException {
        if (obj instanceof XmlSerializable) {
            resp.getWriter().print(((XmlSerializable) obj).toXml());
            return;
        }
        if (obj instanceof Collection) {
            Collection collection = ((Collection) obj);
            if (collection.isEmpty()) {
                resp.getWriter().print("[]");
                return;
            } else if (collection.stream().anyMatch(cObj -> cObj instanceof XmlSerializable)) {
                Object relevantObj = collection.stream().filter(cObj -> cObj instanceof XmlSerializable).findFirst().get();
                final String mainTagName = relevantObj.getClass().getSimpleName().toLowerCase();
                resp.getWriter().print(String.format("<%s>%s</%s>",
                        mainTagName,
                        collection.stream()
                                .filter(cObj -> cObj instanceof XmlSerializable)
                                .map(cObj -> ((XmlSerializable) cObj).toXml())
                                .collect(Collectors.joining()),
                        mainTagName));
                return;
            }
        }
        LOGGER.log(Level.WARNING, "Attempted to create a XML-response for an object that isn't an instance of XmlSerializable");
        throw new IllegalArgumentException("Error creating XML");
    }

    public static String extractPostInput(HttpServletRequest request) throws IOException {
        try (Reader reader = extractPostInputReader(request);
             BufferedReader br = new BufferedReader(reader)
        ) {
            StringBuilder builder = new StringBuilder();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                builder.append(temp);
            }
            return builder.toString();
        }
    }

    public static Reader extractPostInputReader(HttpServletRequest request) throws IOException {
        MimeType mimeType = MimeType.fromMimeType(request.getContentType());
        switch (mimeType) {
            case X_WWW_FORM_URLENCODED: {
                HashMap<String, Object> stringObjectHashMap = new HashMap<>();
                for (Object key : request.getParameterMap().keySet()) {
                    stringObjectHashMap.put(
                            key.toString(),
                            ((String[]) request.getParameterMap().get(key))[0]
                    );
                }
                return mapToReader(stringObjectHashMap);
            }
            case JSON: {
                return request.getReader();
            }
            default:
                return new StringReader("{}");
        }
    }

    public static Reader mapToReader(Map map) {
        return new StringReader(convertSimpleMapToJsonString(map));
    }

    public static String convertSimpleMapToJsonString(Map map) {
        return "{" +
                map.keySet().stream()
                        .map(key -> String.format("\"%s\":\"%s\"", key, map.get(key)))
                        .collect(Collectors.joining(",")) +
                "}";
    }
}
