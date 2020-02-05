package controller.interfacesImplementation;

import controller.GetCrc32;
import controller.interfaces.FileInterface;
import controller.interfaces.ProcessInterface;
import gui.Archivist;
import gui.ExitProgramInterface;
import model.FileItem;
import model.IniClass;
import model.Settings;
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
    private Settings settings = Archivist.getSettings();
    private IniClass iniClass;
    private ExitProgramInterface exitProgramInterface = new FabricController().getExitProgramInterface();
    private FileInterface fileInterface = new FabricController().getFileInterface();


    public DeArchiveProcess() {
        load();
    }

    public DeArchiveProcess(Settings settings) {
        this.settings = settings;
        load();
    }

    private void load() {
        FileItem fileItem = new FileItem(new File(settings.getInputPath()));
        iniClass = new IniClass(null,fileItem);
        iniClass.loadFromFile();
    }

    public boolean write() {
        String inputPath = iniClass.getIniFileItem().getDirectoryName();
        String outputPath = settings.getOutputPath();
        String outputTempPath = outputTempPath(settings.getOutputPath());
        FileItem zipFileItem = searchZIP(iniClass.getItemList());

        zipFileItem.setDirectoryName(inputPath);
        GetCrc32 zipCrc= new GetCrc32();
        try {
            zipCrc.update(zipFileItem.getFile());
        } catch (IOException e) {
            exitProgramInterface.exitProgram(2,e,"Zip File doesn't exist");
        }
        if (!zipFileItem.getCrc32().equals(zipCrc.getValue())) {
            exitProgramInterface.exitProgram(2, new IllegalArgumentException(),"Zip File's CRC32 does not valid");
        }


        try (ZipFile zipFile = new ZipFile(zipFileItem.getFullFileName(), settings.getConsoleEncode())) {
            Enumeration<ZipEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                fileInterface.createAllPathDirectory(outputPath+entry.getName());


                try (FileOutputStream outputStream  = new FileOutputStream(outputPath+entry.getName());
                     InputStream stream = zipFile.getInputStream(entry)) {
                    String crc = fileInterface.writeStreamAndReturnCRC(stream, outputStream);
                    if (!iniClass.checkCRC(entry.getName(),crc))
                        throw  new Exception(String.format("File %s have wrong CRC",entry.getName()));
                } catch (Exception e) {
                    exitProgramInterface.exitProgram(2,e,"File dearchiving problem. Perhaps wrong archive");
                }
            }
        } catch (Exception e) {
            exitProgramInterface.exitProgram(2,e,null);
        }




        //TODO
        return false;
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


}

