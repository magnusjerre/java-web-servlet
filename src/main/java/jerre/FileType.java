package jerre;

public enum FileType {

    CSS("css", "text/css", "/static/css"),
    JAVASCRIPT("js", "text/javascript", "/static/javascript");


    public final String fileEnding, contentType, path;
    FileType(String fileEnding, String contentType, String path) {
        this.fileEnding = fileEnding;
        this.contentType = contentType;
        this.path = path;
    }

    public static FileType fromFileName(String filename) {
        for (FileType ft : FileType.values()) {
            if (filename.endsWith(ft.fileEnding)) {
                return ft;
            }
        }

        throw new IllegalArgumentException("Unsupported filetype: " + filename);
    }
}
