package controller;

import gui.Archivist;
import model.FileItem;
import model.IniClass;
import model.Settings;
import org.apache.tools.zip.Zip64Mode;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;



public class ArchiveProcess implements ProcessInterface {
    private final Settings setting;
    private FileItem outputFile;
    private FileItem iniFile;
    private ArrayList<FileItem> fileList = new ArrayList<>();

    public ArchiveProcess(Settings setting) {
        this.setting = setting;
        checkInputPath(Paths.get(setting.getInputPath()));
        checkOutputPath(Paths.get(setting.getOutputPath()));
        getOutputFiles();
    }


    public ArchiveProcess() {
        this.setting = Archivist.getSettings();
        checkInputPath(Paths.get(setting.getInputPath()));
        checkOutputPath(Paths.get(setting.getOutputPath()));
        getOutputFiles();
    }

    public boolean write() {
        try(FileOutputStream stream = new FileOutputStream(outputFile.getFullFileName());
            ZipOutputStream zipOut = new ZipOutputStream(stream)) {
            zipOut.setEncoding(setting.getConsoleEncode());
            zipOut.setUseZip64(Zip64Mode.Always);
            File fileSource = new File(setting.getInputPath());
            fileList = FileItem.getFileItemArrayListListFromFile(fileSource,fileList,setting.getInputPath());
            writeFileListToZIP(zipOut);
        } catch (FileNotFoundException ex) {
            Archivist.exitProgramm(2,ex.getMessage());
        } catch (IOException e) {
            Archivist.exitProgramm(2,e.getMessage());
        }

        GetCrc32 zipCRC = new GetCrc32();
        try {
            zipCRC.update(outputFile.getFullFileName());
            outputFile.setCrc32(zipCRC.getValue());
        } catch (IOException e) {
            Archivist.exitProgramm(2,e.getMessage());
        }
        fileList.add(outputFile);
        IniClass iniClass = new IniClass(fileList,iniFile);
        iniClass.storeToFile();
        return true;
    }


    private void writeFileListToZIP(ZipOutputStream stream) {
        ArrayList<FileItem> task = new ArrayList<>(fileList);
        Iterator<FileItem> iterator = task.iterator();
        int currentLoop = 0;
        int maxIter = setting.MAX_ITERATION*fileList.size();
        while (iterator.hasNext()) {
            FileItem fileItem = iterator.next();
            try {
                ZipEntry entry = new ZipEntry(fileItem.getRelativeFilePath());
                stream.putNextEntry(entry);
                if (!fileItem.isDirectory()) {
                    GetCrc32 getCrc32 = new GetCrc32();
                    FileInputStream fileInputStream = new FileInputStream(fileItem.getFullFileName());
                    BufferedInputStream bufferedStream = new BufferedInputStream(fileInputStream);
                    while (bufferedStream.available()>0){
                        byte[] buffer;
                        int maxLength = bufferedStream.available();
                        if (maxLength>4048) {
                            buffer = new byte[4048];
                        } else {
                            buffer = new byte[maxLength];
                        }
                        bufferedStream.read(buffer);
                        getCrc32.update(buffer);
                        stream.write(buffer);
                    }
                    fileItem.setCrc32(getCrc32.getValue());
                }
                iterator.remove();
            } catch (FileNotFoundException e) {
                Archivist.exitProgramm(2,e.getMessage());
            } catch (IOException e) {
                //Ignore IOException
            }

            if (++currentLoop % fileList.size() ==0 && currentLoop<maxIter) {
              try {
                    Thread.sleep((int)Math.random()*setting.MAX_TIMEOUT);
                  } catch (InterruptedException e) {}
            }
        }
    }

    /**Check and create name for output file and for ini file, write path into String @outputFileName and @iniFileName
     *
     * @return
     */
    private void getOutputFiles () {
        try {
            Path path = Paths.get(setting.getOutputPath());
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String dateString = new String(dateFormat.format(date).getBytes("UTF-8"), setting.getConsoleEncode());

            int i = 0;
            while (Files.exists(Paths.get(FileItem.getPath(setting.getOutputPath(),String.format(setting.OUTPUT_FILE_NAME_FORMAT,dateString,i))))) {
                i++;
            }
            outputFile = new FileItem(setting.getOutputPath(),setting.getOutputPath()+String.format(setting.OUTPUT_FILE_NAME_FORMAT,dateString,i),false,null);
            outputFile.setExitFile(true);
            iniFile = new FileItem(setting.getOutputPath(),setting.getOutputPath()+String.format(setting.INI_FILE_NAME_FORMAT,dateString,i),false,null);
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
