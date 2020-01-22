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

    private String fileNameEncoding(String fileName) throws UnsupportedEncodingException {
        return new String(fileName.getBytes(setting.getConsoleEncode()),setting.getFileEncode());
    }

    private String fileNameEncoding(long data) throws UnsupportedEncodingException {
        return new String( Long.toString(data).getBytes(setting.getConsoleEncode()),setting.getFileEncode());
    }


    private String zipFileName (File file) {
        return zipFileName(file.getPath(),file.isDirectory());
    }

    private String zipFileName (String fileName, boolean isDirectory) {
        String retValue;
        if (isDirectory) {
            retValue= fileName.substring(setting.getInputPath().length()) + "/";
        } else {
            retValue = fileName.substring(setting.getInputPath().length());
        }
        return retValue;
    }



    /**Check and create name for output file and for ini file, write path into String @outputFileName and @iniFileName
     *
     * @return
     */
    private void getOutputFileName () {
        try {
            Path path = Paths.get(setting.getOutputPath());
            checkOutputPath(path);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String dateString = new String(dateFormat.format(date).getBytes("UTF-8"), setting.getConsoleEncode());

            int i = 0;
            while (Files.exists(Paths.get(getPath(setting.getOutputPath(),String.format(OUTPUT_FILE_NAME_FORMAT,dateString,i))))) {
                i++;
            }
            iniFileName = getPath(setting.getOutputPath(),String.format(INI_FILE_NAME_FORMAT,dateString,i));
            outputFileName = getPath(setting.getOutputPath(),String.format(OUTPUT_FILE_NAME_FORMAT,dateString,i));
        } catch (UnsupportedEncodingException ex) {
            Archivist.exitProgramm(2,ex.getMessage());
        }
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
