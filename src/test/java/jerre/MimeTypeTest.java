package jerre;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class MimeTypeTest {
    @Test
    public void testGetMimeTypeFromValidString() {
        assertEquals(MimeType.JSON, MimeType.fromMimeType("application/json"));
        assertEquals(MimeType.JSON, MimeType.fromMimeType("application/json;charset=utf-8"));
        assertEquals(MimeType.XML_APPLICATION, MimeType.fromMimeType("text/xml;charset=utf-8"));
        assertEquals(MimeType.XML_APPLICATION, MimeType.fromMimeType("application/xml;charset=utf-8"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUnsupportedMimeTypeShouldThrowException() {
        MimeType.fromMimeType("yolo");
    }
}
