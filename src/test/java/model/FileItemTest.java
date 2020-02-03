package model;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileItemTest {

    private FileItem item1 = generateFileItem1();
    private FileItem item2 = generateFileItem();
    private FileItem item3 = generateFileItem();

    FileItem generateFileItem1() {
        String inputDirName = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/";
        String filepath = "testFile.txt";
        FileItem item = new FileItem(inputDirName,inputDirName+filepath,false,null);
        return item;
    }

    FileItem generateFileItem() {
        FileItem item = new FileItem(new File("/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/testFile.txt"));
        return item;
    }

    @Test
    void testGetPath() {
        String relativePath = "директория 1/Test3.txt";
        String basePath = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/";
        assertEquals(FileItem.getPath(basePath,relativePath), basePath+relativePath);
    }

    @Test
    void getDirectoryFromFile() {
        File file = new File("/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/директория 1/Test3.txt");
        String dir = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/директория 1/";
        assertEquals(FileItem.getDirectoryFromFile(file),dir);
    }

    @Test
    void testGetDirectoryFromFile() {
    }

    @Test
    void relativeFilePathFromFull() {
        File file = new File("/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/директория 1/Test3.txt");
        String relativePath = "директория 1/Test3.txt";
        String basePath = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/";
        assertEquals(FileItem.getRelativeFilePathFromFull(basePath,file),relativePath);
    }

    @Test
    void testRelativeFilePathFromFull() {
        assertEquals(item1.getFile(), item2.getFile());
    }

    @Test
    void getFile() {
        assertEquals(item1.getFile(), item2.getFile());
    }

    @Test
    void getFullFileName() {
        assertEquals(item1.getFullFileName(), item2.getFullFileName());
    }

    @Test
    void isDirectory() {
        assertEquals(item2.isDirectory(),false);
    }

    @Test
    void getDirectoryName() {
        String inputDirName = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput";
        item3.setDirectoryName(inputDirName);
        assertEquals(item3.getDirectoryName(),"/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/");
    }

    @Test
    void getRelativeFilePath() {
        assertEquals(item1, item2);
    }

}