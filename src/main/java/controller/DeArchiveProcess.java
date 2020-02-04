package controller;

import gui.Archivist;
import gui.ExitProgramInterface;
import model.FileItem;
import model.IniClass;
import model.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DeArchiveProcess implements ProcessInterface {
    private Settings settings = Archivist.getSettings();
    private IniClass iniClass;
    private ExitProgramInterface exitProgramInterface = new FabricaController().getExitProgramInterface();


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
        FileItem zipFile = searchZIP(iniClass.getItemList());

        zipFile.setDirectoryName(inputPath);
        GetCrc32 zipCrc= new GetCrc32();
        try {
            zipCrc.update(zipFile.getFile());
        } catch (IOException e) {
            exitProgramInterface.exitProgram(2,e,"Zip File doesn't exist");
        }
        if (!zipFile.getCrc32().equals(zipCrc.getValue())) {
            exitProgramInterface.exitProgram(2, new IllegalArgumentException(),"Zip File's CRC32 does not valid");
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

