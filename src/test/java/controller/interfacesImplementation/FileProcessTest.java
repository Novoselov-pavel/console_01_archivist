package controller.interfacesImplementation;

import controller.driver.FileProcess;
import controller.interfaces.FabricControllerInterface;
import exception.InvalidBashOption;
import model.FileItem;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessTest {



    @Test
    void getFileItemArrayListFromFile() {

    }

    @Test
    void copyFromInputDirectoryToDestination() {
    }
    @Test
    void getDirectoryPath() {
    }

    private FabricControllerInterface createSetting() throws UnsupportedEncodingException, InvalidBashOption {
        String[] input1= new String[3];
        input1[0] = "-a";
        input1[1] = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/";
        input1[2] = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestOutput/";
        return new FabricController(input1);
    }
}