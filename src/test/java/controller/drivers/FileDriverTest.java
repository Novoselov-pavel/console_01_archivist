package controller.drivers;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FileDriverTest {

    @Test
    void getNewTodayFile() {
        FileDriver fileDriver = new FileDriver();
        String dir = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestOutput/99";
        Path path = Paths.get(dir);
        try {
            Path result = fileDriver.getNewTodayFile("%s_%d.zip",path,System.getProperty("consoleEncoding"));
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String dateString = dateFormat.format(date);
            StringBuilder builder = new StringBuilder();
            builder.append(dir).
                    append("/").
                    append(dateString).
                    append("_0.zip");
            Path check = Paths.get(builder.toString());
            assertEquals(result,check);
        } catch (UnsupportedEncodingException e) {
            fail();
        }
    }
}