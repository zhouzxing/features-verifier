package utils;

/**
 * @className: OTF2TTF
 * @author: geeker
 * @date: 12/15/25 8:36 PM
 * @Version: 1.0
 * @description: todo jar 远端库丢失！
 */
/*
import com.google.typography.font.sfntly.Font;
import com.google.typography.font.sfntly.FontFactory;
import com.google.typography.font.sfntly.data.WritableFontData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SfntlyConverter {

    public static void convertOTFtoTTF(File otfFile, File ttfFile) throws IOException {
        FontFactory factory = FontFactory.getInstance();

        try (FileInputStream fis = new FileInputStream(otfFile);
             FileOutputStream fos = new FileOutputStream(ttfFile)) {

            Font[] fonts = factory.loadFonts(fis);
            if (fonts.length > 0) {
                WritableFontData data = fonts[0].serialize();
                fos.write(data.array());
            }
        }
    }
}
*/
