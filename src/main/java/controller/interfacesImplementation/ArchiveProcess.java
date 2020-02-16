package controller.interfacesImplementation;

import controller.interfaces.Crc32Interface;
import controller.interfaces.FabricControllerInterface;
import controller.interfaces.ProcessInterface;
import gui.LoggerInterface;
import model.FileItem;
import model.IniClass;
import model.LoggerMessages;
import model.SettingInterface;
import org.apache.tools.zip.Zip64Mode;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**Archived files and writing Ini File
 *
 */
public class ArchiveProcess implements ProcessInterface {
    private final FabricControllerInterface fabric;
    private final SettingInterface setting;
    private final LoggerInterface logger;
    private FileItem outputFile;
    private FileItem iniFile;
    private List<FileItem> fileList = new ArrayList<>();

    public ArchiveProcess(FabricControllerInterface fabric) {
        this.fabric = fabric;
        this.setting = fabric.getSettings();
        this.logger = fabric.getLoggerInterface();
        getOutputFiles();
    }

    /**Archive files
     *
     * @return
     */
    public boolean write() {
        try(FileOutputStream stream = new FileOutputStream(outputFile.getFullFileName());
            ZipOutputStream zipOut = new ZipOutputStream(stream)) {
            zipOut.setEncoding(setting.getConsoleEncode());
            zipOut.setUseZip64(Zip64Mode.Always);
            File fileSource = new File(setting.getInputPath());
            logger.writeLogger(String.format(LoggerMessages.BEGIN_PACK.getFormatter(),fileSource.getName()));
            fileList = fabric.getFileInterface().getFileItemArrayListFromFile(fileSource,fileList,setting.getInputPath());
            writeFileListToZIP(zipOut);
        } catch (FileNotFoundException ex) {
            fabric.getExitProgramInterface().exitProgram(2,ex, ex.getMessage());
        } catch (IOException ex) {
            fabric.getExitProgramInterface().exitProgram(2,ex, ex.getMessage());
        }

        Crc32Interface zipCRC = fabric.getCRC32Class();
        try {
            zipCRC.update(outputFile.getFullFileName());
            outputFile.setCrc32(zipCRC.getValue());
        } catch (IOException ex) {
            fabric.getExitProgramInterface().exitProgram(2,ex, ex.getMessage());
        }
        logger.writeLogger(String.format(LoggerMessages.WRITE_INI_FILE.getFormatter(),iniFile.getRelativeFilePath()));
        fileList.add(outputFile);
        IniClass iniClass = new IniClass(fileList,iniFile);
        iniClass.storeToFile();
        logger.writeLogger(String.format(LoggerMessages.END_PACK.getFormatter(),setting.getInputPath()));
        return true;
    }

    /** Write fileList into ZipOutputStream stream
     *
     * @param stream
     */
    private void writeFileListToZIP(ZipOutputStream stream) {
        ArrayList<FileItem> task = new ArrayList<>(fileList);
        Iterator<FileItem> iterator = task.iterator();
        int currentLoop = 0;
        int maxIter = setting.getMAX_ITERATION()*fileList.size();
        while (iterator.hasNext()) {
            FileItem fileItem = iterator.next();
            logger.writeLogger(String.format(LoggerMessages.BEGIN_PACK.getFormatter(),fileItem.getRelativeFilePath()));
            try {
                ZipEntry entry = new ZipEntry(fileItem.getRelativeFilePath());
                stream.putNextEntry(entry);
                if (!fileItem.isDirectory()) {
                    try (FileInputStream fileInputStream = new FileInputStream(fileItem.getFullFileName())) {
                        String crc32 = fabric.getFileInterface().writeStreamAndReturnCRC(fileInputStream,stream);
                        fileItem.setCrc32(crc32);
                        logger.writeLogger(String.format(LoggerMessages.END_PACK.getFormatter(),fileItem.getRelativeFilePath()));
                        iterator.remove();
                    } catch (IOException ignored) {
                        //Ignore exception
                    }
                } else {
                    logger.writeLogger(String.format(LoggerMessages.END_PACK.getFormatter(),fileItem.getRelativeFilePath()));
                    iterator.remove();
                }
            } catch (IOException ignored) {
                //Ignore exception
            }


            if (++currentLoop % fileList.size() ==0 && currentLoop<maxIter) {
              try {
                    Thread.sleep((int)Math.random()*setting.getMAX_TIMEOUT());
                  } catch (InterruptedException e) {}
            } else if (currentLoop == maxIter) {
                fabric.getExitProgramInterface().exitProgram(2,new IOException(),"Problem with access to files");
            }
        }
    }

    /**Check and create name for output file and for ini file, write path into String @outputFileName and @iniFileName
     *
     * @return
     */
    private void getOutputFiles () {
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String dateString = new String(dateFormat.format(date).getBytes("UTF-8"), setting.getConsoleEncode());
            String iniFilePath = fabric.getFileInterface().
                    getFreeFileName(setting.getINI_FILE_NAME_FORMAT(),dateString,setting.getOutputPath());
            String outputFilePath = fabric.getFileInterface().
                    getFreeFileName(setting.getOUTPUT_FILE_NAME_FORMAT(),dateString,setting.getOutputPath());
            iniFile = new FileItem(setting.getOutputPath(),new File(iniFilePath));
            outputFile = new FileItem(setting.getOutputPath(),new File(outputFilePath));

        } catch (UnsupportedEncodingException ex) {
            fabric.getExitProgramInterface().exitProgram(2,ex,ex.getMessage());
        }
    }

}
