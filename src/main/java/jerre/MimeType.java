package jerre;


public enum MimeType {
    JSON("application/json;charset=utf-8"),
    XML_APPLICATION("application/xml;charset=utf-8"),
    XML_TEXT("text/xml;charset=utf-8"),
    HTML("text/html;charset=utf-8"),
    CSS("text/css;charset=utf-8"),
    JAVASCRIPT("text/javascript;charset=utf-8"),
    X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded");

    public final String acceptedMimeType;

    MimeType(String mimetype) {
        this.acceptedMimeType = mimetype;
    }

    public static MimeType fromMimeType(final String otherMimeType) {
        int indexOfParameter = otherMimeType.indexOf(';');
        String mimeTypeWihtoutParameter = indexOfParameter < 0 ? otherMimeType : otherMimeType.substring(0, indexOfParameter);
        MimeType[] allMimeTypes = values();
        for (MimeType mimeType : allMimeTypes) {
            if (mimeType.acceptedMimeType.contains(mimeTypeWihtoutParameter)) {
                return mimeType;
            }
        }
        throw new IllegalArgumentException("Invalid MimeType: " + otherMimeType);
    }

    public static MimeType findFirstSupportedMimeType(String[] mimeTypes) {
        for (String mimeType : mimeTypes) {
            try {
                return fromMimeType(mimeType);
            } catch (IllegalArgumentException e) {
                // Do nothing here, continue looking for other possible mime types
            }
        }
        throw new IllegalArgumentException("None of the mime types are supported: " + mimeTypes);
    }

}
