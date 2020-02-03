package controller;

import exception.InvalidBashOption;
import model.Settings;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

class DeArchiveProcessTest {

    @Test
    void write() throws UnsupportedEncodingException, InvalidBashOption {

        String[] input3= new String[3];
        input3[0] = "-d";
        input3[1] = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestOutput/202001280_prop.ini";
        input3[2] = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/Test2/";

        BashOptionRead bashOptionRead1 = new BashOptionRead(input3);
        Settings setting = bashOptionRead1.getSettings();
        DeArchiveProcess deArchiveProcess = new DeArchiveProcess(setting);
        deArchiveProcess.write();
    }
}