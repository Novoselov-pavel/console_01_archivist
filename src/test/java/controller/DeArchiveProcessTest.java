package controller;

import controller.interfaces.FabricControllerInterface;
import controller.interfacesimplementation.FabricController;
import exception.InvalidBashOption;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

class DeArchiveProcessTest {

    @Test
    void write() throws UnsupportedEncodingException, InvalidBashOption {
        String[] input3= new String[3];
        input3[0] = "-d";
        input3[1] = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestOutput/20200226_0_prop.ini";
        input3[2] = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/Test2/";
        FabricControllerInterface fabricControllerInterface = new FabricController(input3);
        fabricControllerInterface.workFormSetting();
    }

}