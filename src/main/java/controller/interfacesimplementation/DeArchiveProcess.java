package controller.interfacesimplementation;


import controller.drivers.FileDriver;
import controller.drivers.ZipDriver;
import controller.interfaces.Crc32Interface;
import controller.interfaces.FabricControllerInterface;
import controller.interfaces.ProcessInterface;
import gui.interfaces.ExitProgramInterface;
import gui.interfaces.LoggerInterface;
import model.FileItem;
import model.IniClass;
import model.LoggerMessages;
import model.SettingInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

@SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
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
        Path outputTempPath = getTempPath(settings.getOutputPath(), "temp%d");
        FileDriver fileDriver = new FileDriver();
       try {
           loadIniFileAndCheck();
           List<FileItem> list = unPackZipFileToTempFolder(outputTempPath);
           checkUnPackFiles(list);
       } catch (Exception ex) {
           exitProgramInterface.exitProgram(2,ex, ex.getMessage());
       }
       try {
           fileDriver.copyFromInputToDestination(outputTempPath,settings.getOutputPath(),true);
       } catch (Exception ex) {
           exitProgramInterface.exitProgram(2, ex, ex.getMessage());
       }
       try {
           fileDriver.deleteFile(outputTempPath);
       } catch (NoSuchFileException e) {

       } catch (IOException e) {
           logger.writeErrorMessage(e,e.getMessage());
       }
        return true;
    }

    /**check crc for FileItem in List
     *
     * @param items List of FileItem
     * @return true
     * @throws Exception if check was failed
     */
    @SuppressWarnings("SameReturnValue")
    private boolean checkUnPackFiles(List<FileItem> items) throws Exception {
        for (FileItem item : items) {
            if (iniClass.checkCRC(item.getFilePath(),item.getCrc32()))
                logger.writeLogger(LoggerMessages.CHECK_CRC_OK,item.getFilePath().getFileName());
            else {
                logger.writeLogger(LoggerMessages.CHECK_CRC_FAIL,item.getFilePath().getFileName());
                throw new Exception("Fail crc of "+item.getFilePath().getFileName().toString());
            }
        }
        return true;
    }

    /**Unpack zip file to outputFolder
     *
     * @param outputFolder outputFolder to unpack zip file
     * @return true
     * @throws IOException on error
     */
    private List<FileItem> unPackZipFileToTempFolder(Path outputFolder) throws IOException {
        FileItem zipFileItem = searchZIP(iniClass.getItemList());
        logger.writeLogger(LoggerMessages.BEGIN_UNPACK,zipFileItem.getFilePath());
        ZipDriver zipDriver = new ZipDriver(settings.getInputPath().getParent().resolve(zipFileItem.getFilePath()));
        List<FileItem> list = zipDriver.unpackZipFile(settings.getOutputPath(),settings.getConsoleEncode());
        logger.writeLogger(LoggerMessages.END_PACK,zipFileItem.getFilePath());
        return list;
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

        zipFileItem.setFilePath(settings.getInputPath().getParent().resolve(zipFileItem.getFilePath()));
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

    private Path getTempPath(Path outputPath, @SuppressWarnings("SameParameterValue") String pathFormat) {
        int i = 0;
        String s = pathFormat+System.getProperty("file.separator");
        while (Files.exists(outputPath.resolve(String.format(s,i)))) {
            i++;
        }
        return outputPath.resolve(String.format(s,i));
    }



}

