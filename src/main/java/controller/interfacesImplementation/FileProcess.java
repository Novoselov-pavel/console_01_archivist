package controller.interfacesImplementation;

import controller.GetCrc32;
import controller.fileVisitors.FileVisitorAddFileItemToList;
import controller.fileVisitors.FileVisitorDelete;
import controller.interfaces.Crc32Interface;
import controller.interfaces.FileInterface;
import gui.LoggerInterface;
import model.FileItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileProcess implements FileInterface {

    private final LoggerInterface loggerInterface;

    public FileProcess(LoggerInterface loggerInterface) {
        this.loggerInterface = loggerInterface;
    }

    /**Adding into List {@link FileItem} element for each file in sourceDirectory and below.
     * If file isn't Directory return input list.
     * @param file start Directory
     * @param list list where FileItem will be add. If it is null, will be create new List.
     * @param basePath path for calculation FileItem.getRelativeFilePath(); If it is null, file Directory is used as basePath
     * @return List<FileItem>
     */
    public List<FileItem> getFileItemArrayListFromFile(File file, List<FileItem> list, String basePath) {
        if (!file.isDirectory()) return list;

        if (list == null)
            list = new ArrayList<>();

        if (basePath==null || basePath.isBlank())
            basePath = getDirectoryPath(file.getPath());
        else {
            if (!checkBasePath(file,basePath)) {
                basePath = getDirectoryPath(file.getPath());
            }
        }

        FileVisitorAddFileItemToList visitor = new FileVisitorAddFileItemToList(list,basePath);
        try {
            Files.walkFileTree(file.toPath(),visitor);
        } catch (IOException e) {
            loggerInterface.writeErrorMessage(e,null);
        }

        return visitor.getList();
    }

    @Override
    public void copyFromInputDirectoryToDestination(String inputPath, String destinationPath, boolean overwrite) {
        createAllPathDirectory(destinationPath);
        File file = new File(inputPath);
        if (!file.exists() || !file.isDirectory()) {
            createAllPathDirectory(inputPath);
        }
        List<FileItem> list = getFileItemArrayListFromFile(file,null, inputPath);
        ArrayList<FileItem> workList = new ArrayList<>(list);

        Iterator<FileItem> iterator = workList.iterator();
        while (iterator.hasNext()) {
            FileItem currentFileItem = iterator.next();

            File destinationFile = new File(destinationPath+currentFileItem.getRelativeFilePath());
            if (destinationFile.exists()) {
                if (overwrite)
                    try {
                        Files.copy(currentFileItem.getPath(), Paths.get(destinationPath + currentFileItem.getRelativeFilePath()), StandardCopyOption.REPLACE_EXISTING);
                        iterator.remove();
                    } catch (IOException ex) {
                        loggerInterface.writeErrorMessage(ex,ex.getMessage());
                    }
            }
            else {
                createAllPathDirectory(destinationPath+currentFileItem.getRelativeFilePath());
                try {
                    Files.copy(currentFileItem.getPath(), Paths.get(destinationPath + currentFileItem.getRelativeFilePath()));
                    iterator.remove();
                } catch (IOException ex) {
                    loggerInterface.writeErrorMessage(ex,ex.getMessage());
                }
            }
        }
        try {
            Files.walkFileTree(Paths.get(inputPath), new FileVisitorDelete());
        } catch (Exception ex) {
            loggerInterface.writeErrorMessage(ex,ex.getMessage());
        }

    }

    /**
     * Return path of file that doesn't exist in directory of directoryPath.
     *
     * @param fileNameFormat    may be follow String in {@link String#format(String, Object...)}
     *                          <br>Must contain %s and %d. For example "%s%d.zip"
     * @param firstPartFileName value of %s in fileNameFormat
     * @param directoryPath     directory of this file
     */
    @Override
    public String getFreeFileName(String fileNameFormat, String firstPartFileName, String directoryPath) {
        String checkDirPath = getDirectoryPath(directoryPath);
        int i = 0;
        while (new File(checkDirPath+String.format(fileNameFormat,fileNameFormat,i)).exists()) {
            i++;
        }
        return checkDirPath+String.format(fileNameFormat,fileNameFormat,i);
    }

    /**
     * Check and return path for Directory.
     * <br>Example: if path: /dir1/dir2
     * <br>return: /dir/dir2/
     * <br>if path: /dir/dir2/
     * <br> return input path
     *
     * @param path path of directory
     * @return String
     */
    @Override
    public String getDirectoryPath(String path) {
        if (path == null || path.isBlank()) return path;
        if (path.endsWith("/")) return path;

        return path+"/";
    }


    @Override
    public void createAllPathDirectory(String path) {
        path = getDirectoryFromFile(path);
        File file = new File(path);
        file.mkdirs();
    }

    @Override
    public String writeStreamAndReturnCRC(InputStream inputStream, OutputStream outputStream) throws IOException {
        Crc32Interface crc32Interface = new GetCrc32();
        while (inputStream.available()>0) {
            byte[] buffer;
            int maxLength = inputStream.available();
            if (maxLength>4048) {
                buffer = new byte[4048];
            } else {
                buffer = new byte[maxLength];
            }
            inputStream.read(buffer);
            crc32Interface.update(buffer);
            outputStream.write(buffer);
        }
        return crc32Interface.getValue();
    }




    private String getDirectoryFromFile (String filePath) {
        filePath = filePath.substring(0,filePath.lastIndexOf("/")+1);
        return filePath;
    }


    /**Validating basePath. If path of the file contain basePath return true, else return false.
     *
     * @param file file
     * @param basePath basePath, not Null
     * @return boolean
     */
    private boolean checkBasePath(File file, String basePath) {
        return file.getPath().indexOf(basePath)>-1;
    }
}
