package model;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

public class FileItem implements Serializable {
    private static final long serialVersionUID = 20200125001L;
    public transient static final String DIR_CRC32 = "DIR";
    private String startDirectoryName;
    private String endDirectoryName;
    private String filePath;
    private boolean isDirectory;
    private String crc32;


    public static String getPath (String directory, String fileName) {
        if (directory.endsWith("/")) {
            return directory+fileName;
        } else {
            return directory+"/"+fileName;
        }
    }


    public FileItem(String startDirectoryName, String endDirectoryName,String filePath, boolean isDirectory, String crc32) {
        this.startDirectoryName = startDirectoryName;
        this.endDirectoryName = endDirectoryName;
        this.filePath = filePath;
        this.isDirectory = isDirectory;
        this.crc32 = crc32;

    }

    public FileItem (String startDirectoryName, String endDirectoryName, File inputFile) {
        if (inputFile.isDirectory()) {
            isDirectory = true;
            crc32 = DIR_CRC32;
        } else {
            isDirectory = false;
            crc32 = null;
        }
        this.startDirectoryName = startDirectoryName;
        this.endDirectoryName = endDirectoryName;
        this.filePath = zipFileName(inputFile.getAbsolutePath(),isDirectory);

    }


    public File getFile () {
        return new File(getStartFileName());
    }

    public String getEndFileName () {
        return getPath(endDirectoryName,filePath);
    }

    public String getStartFileName () {
        return getPath(startDirectoryName,filePath);
    }

    public String getZipFileName() {
        return filePath;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setCrc32(String crc32) {
        this.crc32 = crc32;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof FileItem)) return false;
        FileItem fileItem = (FileItem) o;
        return isDirectory == fileItem.isDirectory &&
                Objects.equals(startDirectoryName, fileItem.startDirectoryName) &&
                Objects.equals(endDirectoryName, fileItem.endDirectoryName) &&
                Objects.equals(filePath, fileItem.filePath) &&
                Objects.equals(crc32, fileItem.crc32);
    }

    @Override
    public int hashCode() {
        return 1025+31*Objects.hash(startDirectoryName, endDirectoryName, filePath, isDirectory, crc32);
    }

    private String zipFileName (File file) {
        return zipFileName(file.getPath(),file.isDirectory());
    }

    private String zipFileName (String fileName, boolean isDirectory) {
        String retValue;
        if (isDirectory) {
            retValue= fileName.substring(startDirectoryName.length()) + "/";
        } else {
            retValue = fileName.substring(startDirectoryName.length());
        }
        return retValue;
    }





}
