package jerre;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void testSplitMimeTypes() {
        assertEquals(1, Utils.splitMimeTypes(MimeType.JSON.acceptedMimeType).length);
        assertEquals(2,
                Utils.splitMimeTypes(MimeType.JSON.acceptedMimeType + ", " + MimeType.XML_APPLICATION.acceptedMimeType).length);
        assertEquals(3,
                Utils.splitMimeTypes(MimeType.JSON.acceptedMimeType + ", "
                        + MimeType.XML_APPLICATION.acceptedMimeType + ","
                        + MimeType.XML_APPLICATION.acceptedMimeType)
                        .length);
    }

    @Test
    public void test() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("username", "magnus");
        stringObjectHashMap.put("password", "jerre");
        System.out.println(Utils.convertSimpleMapToJsonString(stringObjectHashMap));
    }
}
