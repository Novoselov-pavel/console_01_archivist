package controller.interfacesImplementation;


import controller.interfaces.Crc32Interface;
import controller.interfaces.FabricControllerInterface;
import controller.interfaces.ProcessInterface;
import gui.ExitProgramInterface;
import gui.LoggerInterface;
import model.FileItem;
import model.IniClass;
import model.LoggerMessages;
import model.SettingInterface;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.List;

public class DeArchiveProcess implements ProcessInterface {
    private final FabricControllerInterface controller;
    private final ExitProgramInterface exitProgramInterface;
    private final SettingInterface settings;
    private final LoggerInterface logger;
    private final Crc32Interface crc32;
    private IniClass iniClass;


    public DeArchiveProcess(FabricControllerInterface controller) {
        this.controller = controller;
        exitProgramInterface = controller.getExitProgramInterface();
        settings = controller.getSettings();
        logger = controller.getLoggerInterface();
        crc32 = controller.getCRC32Class();

    }


    /**Unpack files
     *
     * @return
     */
    public boolean write() {
        loadIniFileAndCheck();
        Path outputTempPath = outputTempPath(settings.getOutputPath());
        unPackZipFileToTempFolder(outputTempPath);



        String inputPath = iniClass.getIniFileItem().getDirectoryName();
        String outputPath = settings.getOutputPath();








        fileInterface.copyFromInputDirectoryToDestination(outputTempPath,outputPath,true);
        return true;
    }

    /**Unpack zip file to outputFolder
     *
     * @param outputFolder outputFolder to unpack zip file
     * @return true
     * @throws IOException on error
     */
    private boolean unPackZipFileToTempFolder(Path outputFolder) throws IOException {
        FileItem zipFileItem = searchZIP(iniClass.getItemList());
        logger.writeLogger(String.format(LoggerMessages.BEGIN_UNPACK.getFormatter(),zipFileItem.getFilePath()));

        //TODO

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

    }

    /**Load ini file and check CRC zip file.
     *
     * @return true
     * @throws IOException at error
     */
    private boolean loadIniFileAndCheck() throws IOException, ClassNotFoundException {
        logger.writeLogger(String.format(LoggerMessages.READ_INI_FILE.getFormatter(),settings.getInputPath()));
        FileItem fileItem = new FileItem(settings.getInputPath());
        iniClass = new IniClass(null,fileItem);
        iniClass.loadFromFile();

        FileItem zipFileItem = searchZIP(iniClass.getItemList());
        logger.writeLogger(String.format(LoggerMessages.BEGIN_UNPACK.getFormatter(),zipFileItem.getFilePath()));

        zipFileItem.setFilePath(settings.getInputPath());
        crc32.reset();
        crc32.update(zipFileItem.getFilePath().toFile());
        if (!zipFileItem.getCrc32().equals(crc32.getValue())) {
            throw new IllegalArgumentException("Zip File's CRC32 does not valid");
        }
        return true;
    }

    private FileItem searchZIP(List<FileItem> list) {
        for (FileItem item : list) {
                if (item.isExitFile()) return item;
        }
        return null;
    }

    private Path outputTempPath (Path outputPath) {
        int i = 0;
        String pathFormat = "temp%d"+System.getProperty("file.separator");
        while (Files.exists(outputPath.resolve(String.format(pathFormat,i)))) {
            i++;
        }
        return outputPath.resolve(String.format(pathFormat,i));
    }

    private boolean isDirectory(String path) {
        return path.endsWith("/");
    }

}

