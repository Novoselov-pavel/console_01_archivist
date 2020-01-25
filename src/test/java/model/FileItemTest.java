package model;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileItemTest {

    @Test
    void getPath() {


    }

    FileItem generateFileItem1() {
        String inputDirName = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/";
        String outputDirName = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestOutput/";
        String filepath = "testFile.txt";
        FileItem item = new FileItem(inputDirName,outputDirName,filepath,false,null);
        return item;
    }

    FileItem generateFileItem2() {
        String inputDirName = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/";
        String outputDirName = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestOutput/";
        File file = new File("/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/testFile.txt");
        FileItem item = new FileItem(inputDirName,outputDirName,file);
        return item;
    }

    FileItem generateFileItem3() {
        String inputDirName = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/";
        String outputDirName = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestOutput/";
        File file = new File("/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/directory1/Test2.txt");
        FileItem item = new FileItem(inputDirName,outputDirName,file);
        return item;
    }

    FileItem generateFileItem4() {
        String inputDirName = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/";
        String outputDirName = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestOutput/";
        String filepath = "directory1/Test2.txt";
        FileItem item = new FileItem(inputDirName,outputDirName,filepath,false,null);
        return item;
    }


    @Test
    void getEndFileName() {
        FileItem first = generateFileItem1();
        FileItem third = generateFileItem3();
        String firstString = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestOutput/testFile.txt";
        String thirdString = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestOutput/directory1/Test2.txt";

        assertTrue(first.getEndFileName().equals(firstString) && third.getEndFileName().equals(thirdString));
    }

    @Test
    void getStartFileName() {
        FileItem first = generateFileItem1();
        FileItem third = generateFileItem3();
        String firstString = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/testFile.txt";
        String thirdString = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/directory1/Test2.txt";
        assertTrue(first.getStartFileName().equals(firstString) && third.getStartFileName().equals(thirdString));
    }

    @Test
    void testEquals() {
        FileItem first = generateFileItem1();
        FileItem second = generateFileItem2();
        FileItem third = generateFileItem3();
        FileItem fourth = generateFileItem4();
        assertTrue(first.equals(second)&& third.equals(fourth));
    }
}