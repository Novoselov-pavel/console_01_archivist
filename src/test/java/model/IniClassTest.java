package model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class IniClassTest {

    @Test
    void storeToFile() {
        FileItem iniFileItem = createIniFileItem();
        ArrayList<FileItem> files = createFileItem();
        IniClass iniClass = new IniClass(files,iniFileItem);
        try {
            iniClass.storeToFile();
        } catch (IOException e) {
            fail();
        }
        assertTrue(true);
    }

    @Test
    void loadFromFile() {
        ArrayList<FileItem> files = new ArrayList<>();
        FileItem iniFileItem = createIniFileItem();
        IniClass iniClass = new IniClass(null,iniFileItem);
        try {
            iniClass.loadFromFile();
        } catch (Exception e) {
            fail();
        }
        assertTrue(true);
    }

    @Test
    void storeAndLoadFromFile() {
        FileItem iniFileItem = createIniFileItem();
        ArrayList<FileItem> files = createFileItem();
        IniClass iniClass = new IniClass(files,iniFileItem);
        try {
            iniClass.storeToFile();
        } catch (IOException e) {
           fail();
        }

        IniClass iniClassLoad = new IniClass(null,iniFileItem);
        try {
            iniClassLoad.loadFromFile();
        } catch (Exception e) {
            fail();
        }
        assertTrue(iniClass.getItemList().equals(iniClassLoad.getItemList()));
    }


    ArrayList<FileItem> createFileItem() {
        String inputDirName = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/";
        ArrayList<FileItem> list = new ArrayList<>();

        FileItem item = new FileItem(inputDirName, new File("/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/тестовый файлик (1).txt"));
        FileItem item2= new FileItem(inputDirName, new File("/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/testFile2.txt"));

        FileItem item3= new FileItem(inputDirName, new File("/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/директория 1/Test3.txt"));

        FileItem item4= new FileItem(inputDirName,new File("/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestInput/директория 1/oops1/Test4.txt"));

        item.setCrc32("102051");
        item2.setCrc32("10500");
        item3.setCrc32("450250");

        list.add(item);
        list.add(item2);
        list.add(item3);
        list.add(item4);

        return list;
    }

    FileItem createIniFileItem() {
        String outputDirName = "/home/pavel/IdeaProjects/npn/console/01_archivist/src/test/java/controller/TestOutput/";
        String filepath = "iniFile.ini";
        FileItem item = new FileItem(new File(outputDirName+filepath));
        return item;
    }
}