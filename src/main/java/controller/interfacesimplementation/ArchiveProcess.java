package controller.interfacesimplementation;

import controller.drivers.FileDriver;
import controller.drivers.ZipDriver;
import controller.interfaces.Crc32Interface;
import controller.interfaces.FabricControllerInterface;
import controller.interfaces.ProcessInterface;
import exception.InvalidBashOption;
import gui.interfaces.ExitProgramInterface;
import gui.interfaces.LoggerInterface;
import model.FileItem;
import model.IniClass;
import model.LoggerMessages;
import model.SettingInterface;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**Archived files and writing Ini File
 *
 */
@SuppressWarnings("CatchMayIgnoreException")
public class ArchiveProcess implements ProcessInterface {
    private static int currentIteration = 1;
    private final FabricControllerInterface fabric;
    private final SettingInterface setting;
    private final LoggerInterface logger;
    private final ExitProgramInterface exit;
    private FileItem outputFile;
    private FileItem iniFile;
    private List<FileItem> fileList = new ArrayList<>();

    public ArchiveProcess(FabricControllerInterface fabric) {
        this.fabric = fabric;
        this.setting = fabric.getSettings();
        this.logger = fabric.getLoggerInterface();
        this.exit = fabric.getExitProgramInterface();
        getOutputFiles();
    }

    /**Pack files
     *
     * @return true
     */
    public boolean write() {
        checkInputPath();
        //writing zip archive
        try {
            writeZip();
            addOutputFileToList();
            writeIniFile();
        } catch (IOException e) {
            exit.exitProgram(2,e,e.getMessage());
        }
        //writing  ini file
        return true;
    }

    /** Create and save ini file;
     *
     * @return true
     * @throws IOException at error
     */
    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    private boolean writeIniFile() throws IOException {
        IniClass iniClass = new IniClass(fileList,iniFile);
        iniClass.storeToFile();
        logger.writeLogger(LoggerMessages.END_ALL_PACK_PROCESS,outputFile.getFilePath());
        return true;
    }

    /**Add outputFile to fileList
     *
     * @return true
     * @throws IOException at error
     */
    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    private boolean addOutputFileToList() throws IOException {
        Crc32Interface zipCRC = fabric.getCRC32Class();
        zipCRC.update(outputFile.getFilePath().toFile());
        outputFile.setCrc32(zipCRC.getValue());
        outputFile.setExitFile(true);
        outputFile.setFilePath(outputFile.getFilePath().getFileName());
        logger.writeLogger(LoggerMessages.WRITE_INI_FILE,iniFile.getFilePath());
        fileList.add(outputFile);
        return true;
    }

    /** write zip file, on Exception try to write {@link SettingInterface#getMAX_ITERATION()} times with random delay
     * initialized with {@link SettingInterface#getMAX_TIMEOUT()}.
     *
     * @return true
     * @throws IOException if it can't end writing
     */
    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    private boolean writeZip() throws IOException {
        try {
            Files.createDirectories(outputFile.getFilePath().getParent());
            FileDriver fileDriver = new FileDriver();
            ZipDriver zipDriver = new ZipDriver(outputFile.getFilePath());
            logger.writeLogger(LoggerMessages.BEGIN_PACK,outputFile.getFilePath());
            fileList= fileDriver.getFileItemArrayListFromFile(setting.getInputPath(),fileList);
            zipDriver.packListToZipFile(fileList,setting.getInputPath(),setting.getConsoleEncode());

            logger.writeLogger(LoggerMessages.END_PACK,outputFile.getFilePath());
        } catch (IOException e) {
            ///block there trying to
            FileDriver fileDriver = new FileDriver();
            try{
                fileDriver.deleteFile(outputFile.getFilePath());
            } catch (IOException ex) {  }

            for (; currentIteration <setting.getMAX_ITERATION();) {
                try {
                    Thread.sleep(new Random(setting.getMAX_TIMEOUT()).nextLong());
                    writeZip();
                    return true;
                }catch (Exception ex) {
                    currentIteration++;
                }
            }
            throw e;
        }
        return true;
    }


    /**Check and create name for output file and for ini file, write path into String @outputFileName and @iniFileName
     *
     */
    private void getOutputFiles() {
        FileDriver fileDriver = new FileDriver();
        try {
            Path outputPath = fileDriver.getNewTodayFile(setting.getOUTPUT_FILE_NAME_FORMAT(), setting.getOutputPath(), setting.getConsoleEncode());
            Path  iniFilePath = fileDriver.getNewTodayFile(setting.getINI_FILE_NAME_FORMAT(),setting.getOutputPath(),setting.getConsoleEncode());
            iniFile = new FileItem(iniFilePath,false,null);
            outputFile = new FileItem(outputPath,false,null);
        } catch (Exception e) {
            fabric.getExitProgramInterface().exitProgram(2,e,e.getMessage());
        }
    }

    /**check settings
     *
     */
    private void checkInputPath() {
        if (!Files.exists(setting.getInputPath())) {
            exit.exitProgram(2,new InvalidBashOption(),"Input file doesn't exist");
        }
    }
}
