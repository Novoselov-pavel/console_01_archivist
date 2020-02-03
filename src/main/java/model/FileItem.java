package model;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

public class FileItem implements Serializable {
    private static final long serialVersionUID = 20200125002L;
    public transient static final String DIR_CRC32 = "DIR";
    private String directoryName;
    private String relativeFilePath;
    private boolean isDirectory;
    private String crc32;
    private boolean isExitFile = false;

    public static String getPath (String directory, String fileName) {
        if (directory.endsWith("/")) {
            return directory+fileName;
        } else {
            return directory+"/"+fileName;
        }
    }

    public static String getDirectoryFromFile (File file) {
        return getDirectoryFromFile(file.getPath());
    }

    public static String getDirectoryFromFile (String filePath) {
        filePath = filePath.substring(0,filePath.lastIndexOf("/")+1);
        return filePath;
    }

    public static String getRelativeFilePathFromFull(String basePath, File file) {
        return getRelativeFilePathFromFull(basePath,file.getPath(),file.isDirectory());
    }

    public static String getRelativeFilePathFromFull(String basePath, String fullPath, boolean isDirectory) {
        String retValue;
        if (isDirectory) {
            retValue= fullPath.substring(basePath.length()) + "/";
        } else {
            retValue = fullPath.substring(basePath.length());
        }
        return retValue;
    }


    public FileItem(String basePath, String fullPath, boolean isDirectory, String crc32) {
        this.directoryName = basePath;
        this.relativeFilePath = getRelativeFilePathFromFull(basePath,fullPath,isDirectory);
        this.isDirectory = isDirectory;
        this.crc32 = crc32;
    }

    public FileItem(String basePath, File inputFile) {
        if (inputFile.isDirectory()) {
            isDirectory = true;
            crc32 = DIR_CRC32;
        } else {
            isDirectory = false;
            crc32 = null;
        }
        this.directoryName = basePath;
        this.relativeFilePath = getRelativeFilePathFromFull(basePath,inputFile);

    }

    public FileItem(File inputFile) {
        if (inputFile.isDirectory()) {
            isDirectory = true;
            crc32 = DIR_CRC32;
        } else {
            isDirectory = false;
            crc32 = null;
        }
        this.directoryName = getDirectoryFromFile(inputFile);
        this.relativeFilePath = getRelativeFilePathFromFull(directoryName,inputFile);

    }


    public File getFile() {
        return new File(getFullFileName());
    }

    public String getFullFileName () {
        return getPath(directoryName,relativeFilePath);
    }


    public boolean isDirectory() {
        return isDirectory;
    }

    public void setCrc32(String crc32) {
        this.crc32 = crc32;
    }

    public boolean isExitFile() {
        return isExitFile;
    }

    public void setExitFile(boolean exitFile) {
        isExitFile = exitFile;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = checkAndReturnDirectoryPath(directoryName,true);
    }

    public String getRelativeFilePath() {
        return relativeFilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof FileItem)) return false;
        FileItem item = (FileItem) o;
        return isDirectory == item.isDirectory &&
                isExitFile == item.isExitFile &&
                Objects.equals(directoryName, item.directoryName) &&
                Objects.equals(relativeFilePath, item.relativeFilePath) &&
                Objects.equals(crc32, item.crc32);
    }

    @Override
    public int hashCode() {
        return Objects.hash(directoryName, relativeFilePath, isDirectory, crc32, isExitFile);
    }

    private String checkAndReturnDirectoryPath (String path, boolean isDirectory) {
        if (isDirectory) {
            if (!path.endsWith("/")) {
                return path+"/";
            } else
                return path;
        }
        else
            return path;

    }











}
