package controller.interfacesImplementation;

import controller.interfaces.FabricControllerInterface;
import exception.InvalidBashOption;
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
    void getFreeFileName() {

        try {
            FabricControllerInterface controller = createSetting();
            String s = controller.getFileInterface().getFreeFileName(controller.getSettings().getOUTPUT_FILE_NAME_FORMAT(),"file1",controller.getSettings().getOutputPath());
            assertEquals(s,controller.getSettings().getOutputPath()+String.format(controller.getSettings().getOUTPUT_FILE_NAME_FORMAT(),"file1",0));

        } catch (Exception e) {
            fail();
        }


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