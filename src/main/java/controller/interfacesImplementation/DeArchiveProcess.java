package controller.interfacesImplementation;


import controller.interfaces.Crc32Interface;
import controller.interfaces.FabricControllerInterface;
import controller.interfaces.FileInterface;
import controller.interfaces.ProcessInterface;
import gui.ExitProgramInterface;
import gui.LoggerInterface;
import model.FileItem;
import model.IniClass;
import model.LoggerMessages;
import model.SettingInterface;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;

public class DeArchiveProcess implements ProcessInterface {
    private final FabricControllerInterface controller;
    private final ExitProgramInterface exitProgramInterface;
    private final FileInterface fileInterface;
    private final SettingInterface settings;
    private final LoggerInterface logger;
    private final Crc32Interface crc32;
    private IniClass iniClass;


    public DeArchiveProcess(FabricControllerInterface controller) {
        this.controller = controller;
        exitProgramInterface = controller.getExitProgramInterface();
        fileInterface = controller.getFileInterface();
        settings = controller.getSettings();
        logger = controller.getLoggerInterface();
        crc32 = controller.getCRC32Class();
        load();
    }

    private void load() {
        logger.writeLogger(String.format(LoggerMessages.READ_INI_FILE.getFormatter(),settings.getInputPath()));
        FileItem fileItem = new FileItem(new File(settings.getInputPath()));
        iniClass = new IniClass(null,fileItem);
        iniClass.loadFromFile();
    }

    public boolean write() {
        String inputPath = iniClass.getIniFileItem().getDirectoryName();
        String outputPath = settings.getOutputPath();
        String outputTempPath = outputTempPath(settings.getOutputPath());
        FileItem zipFileItem = searchZIP(iniClass.getItemList());
        logger.writeLogger(String.format(LoggerMessages.BEGIN_UNPACK.getFormatter(),zipFileItem.getRelativeFilePath()));

        zipFileItem.setDirectoryName(inputPath);
        crc32.reset();
        try {
            crc32.update(zipFileItem.getFile());
        } catch (IOException e) {
            exitProgramInterface.exitProgram(2,e,"Zip File doesn't exist");
        }
        if (!zipFileItem.getCrc32().equals(crc32.getValue())) {
            exitProgramInterface.exitProgram(2, new IllegalArgumentException(),"Zip File's CRC32 does not valid");
        }


        try (ZipFile zipFile = new ZipFile(zipFileItem.getFullFileName(), settings.getConsoleEncode())) {
            Enumeration<ZipEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                logger.writeLogger(String.format(LoggerMessages.BEGIN_UNPACK.getFormatter(),entry.getName()));

                fileInterface.createAllPathDirectory(outputTempPath+entry.getName());

                if (isDirectory(entry.getName())) {
                    logger.writeLogger(String.format(LoggerMessages.END_UNPACK.getFormatter(),entry.getName()));
                    continue;
                }

                try (FileOutputStream outputStream  = new FileOutputStream(outputTempPath+entry.getName());
                     InputStream stream = zipFile.getInputStream(entry)) {
                    String crc = fileInterface.writeStreamAndReturnCRC(stream, outputStream);
                    if (!iniClass.checkCRC(entry.getName(),crc))
                        throw  new Exception(String.format("File %s have wrong CRC",entry.getName()));

                    logger.writeLogger(String.format(LoggerMessages.END_UNPACK.getFormatter(),entry.getName()));
                } catch (Exception e) {
                    exitProgramInterface.exitProgram(2,e,"File dearchiving problem. Perhaps wrong archive");
                }
            }
        } catch (Exception e) {
            exitProgramInterface.exitProgram(2,e,null);
        }
        fileInterface.copyFromInputDirectoryToDestination(outputTempPath,outputPath,true);
        return true;
    }

    private FileItem searchZIP(List<FileItem> list) {
        for (FileItem item : list) {
                if (item.isExitFile()) return item;
        }
        return null;
    }

    private String outputTempPath (String outputPath) {
        int i = 0;
        String pathFormat = outputPath+"temp%d/";
        while (Files.exists(Paths.get(String.format(pathFormat,i)))) {
            i++;
        }
        return String.format(pathFormat,i);
    }

    private boolean isDirectory(String path) {
        return path.endsWith("/");
    }

}

