package controller;

import gui.Archivist;
import model.Settings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;


public class ArchiveProcess {
    private final Settings setting = Archivist.getSettings();
    private final String OUTPUT_FILE_NAME_FORMAT = "%s%d.zip";
    private final String INI_FILE_NAME_FORMAT = "%s%d_prop.ini";
    private final int MAX_ITERATION = 10;
    private final int MAX_TIMEOUT = 30_000;
    private FileItem outputFile;
    private FileItem iniFile;
    private ArrayList<FileItem> fileList = new ArrayList<>();


    public ArchiveProcess() {
        checkInputPath(Paths.get(setting.getInputPath()));
        checkOutputPath(Paths.get(setting.getOutputPath()));
        getOutputFiles();
    }

    public boolean write() {
        try(FileOutputStream stream = new FileOutputStream(outputFile.getEndFileName());
            ZipOutputStream zipOut = new ZipOutputStream(stream)) {
            zipOut.setEncoding(setting.getConsoleEncode());
            zipOut.setUseZip64(Zip64Mode.Always);
            File fileSource = new File(setting.getInputPath());
            fillFileList(fileSource);
            writeFileListToZIP(zipOut);
        } catch (FileNotFoundException ex) {
            Archivist.exitProgramm(2,ex.getMessage());
        } catch (IOException e) {
            Archivist.exitProgramm(2,e.getMessage());
        }

        GetCrc32 zipCRC = new GetCrc32();
        try {
            zipCRC.update(outputFile.getFile());
            outputFile.setCrc32(zipCRC.getValue());
        } catch (IOException e) {
            Archivist.exitProgramm(2,e.getMessage());
        }
        fileList.add(outputFile);

        return true;
    }

    private void fillFileList(File fileSource) {
        File[] files = fileSource.listFiles();
        for (File file : files) {
            fileList.add(new FileItem(setting.getInputPath(),setting.getOutputPath(),file));
            if (file.isDirectory()) {
                fillFileList(file);
            }
        }
    }

    private void writeFileListToZIP(ZipOutputStream stream) {
        Iterator<FileItem> iterator = fileList.iterator();
        int currentLoop = 0;
        int maxIter = MAX_ITERATION*fileList.size();
        int startArraySize = fileList.size();
        while (iterator.hasNext()) {
            FileItem fileItem = iterator.next();
            try {
                ZipEntry entry = new ZipEntry(fileItem.getZipFileName());
                stream.putNextEntry(entry);
                if (!fileItem.isDirectory()) {
                    GetCrc32 getCrc32 = new GetCrc32();
                    FileInputStream fileInputStream = new FileInputStream(fileItem.getStartFileName());
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
                    iterator.remove();
                }
            } catch (FileNotFoundException e) {
                Archivist.exitProgramm(2,e.getMessage());
            } catch (IOException e) {
            }
            if (++currentLoop % startArraySize == 0) {
                if (currentLoop<maxIter) {
                    try {
                        Thread.sleep((int)Math.random()*MAX_TIMEOUT);
                    } catch (InterruptedException e) {
                    }
               }
            }
        }
    }


    private String fileNameEncoding(String fileName) throws UnsupportedEncodingException {
        return new String(fileName.getBytes(setting.getConsoleEncode()),setting.getFileEncode());
    }

    private String fileNameEncoding(long data) throws UnsupportedEncodingException {
        return new String( Long.toString(data).getBytes(setting.getConsoleEncode()),setting.getFileEncode());
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
            while (Files.exists(Paths.get(FileItem.getPath(setting.getOutputPath(),String.format(OUTPUT_FILE_NAME_FORMAT,dateString,i))))) {
                i++;
            }
            outputFile = new FileItem(setting.getInputPath(),setting.getOutputPath(),String.format(OUTPUT_FILE_NAME_FORMAT,dateString,i),false,null);
            iniFile = new FileItem(setting.getInputPath(), setting.getOutputPath(),String.format(INI_FILE_NAME_FORMAT,dateString,i), false,null);
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
