package controller;

import gui.Archivist;
import model.Settings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;


public class ArchiveProcess {
    Settings setting;

    public ArchiveProcess(Settings setting) {
        this.setting = setting;
    }

    public boolean write() {
        Path sourcePath = Paths.get(setting.getInputPath());
        checkInputPath(sourcePath);

    }

    private String getOutputFileName () {
        Path path = Paths.get(setting.getOutputPath());
        checkOutputPath(path);

        /// TODO generate new filename
    }

    private void checkInputPath (Path sourcePath) {
        try {
            if (!Files.exists(sourcePath)) {
                Archivist.exitProgramm(2,"Source path isn't valid");
            }
            if (!Files.isDirectory(sourcePath)) {
                Archivist.exitProgramm(2,"Source path isn't valid. It isn't directory");
            }
        } catch (SecurityException ex) {
            Archivist.exitProgramm(2,ex.getMessage());
        }
    }

    private void checkOutputPath (Path sourcePath) {
        try {
            if (Files.exists(sourcePath)) {
                if (!Files.isDirectory(sourcePath)) {
                    Archivist.exitProgramm(2,"Source path isn't valid. It isn't directory");
                }
            }
        } catch (SecurityException ex) {
            Archivist.exitProgramm(2,ex.getMessage());
        }
    }


}
