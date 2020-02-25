package controller.interfacesImplementation;

import controller.interfaces.FabricControllerInterface;
import exception.InvalidBashOption;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

class FileProcessTest {


    private FabricControllerInterface createSetting() throws UnsupportedEncodingException, InvalidBashOption {
        String[] input1= new String[3];
        input1[0] = "-a";
        input1[1] = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/";
        input1[2] = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestOutput/";
        return new FabricController(input1);
    }
}