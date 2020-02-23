package model;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileItem implements Serializable {
    private static final long serialVersionUID = 20200125002L;
    public transient static final String DIR_CRC32 = "DIR";
    private Path filePath;
    private boolean isDirectory;
    private String crc32;
    private boolean isExitFile = false;

    public FileItem(Path filePath, boolean isDirectory, String crc32) {
        this.filePath = filePath;
        this.isDirectory = isDirectory;
        this.crc32 = crc32;
    }

    public FileItem(Path filePath) {
        this.filePath = filePath;
        this.isDirectory = Files.isDirectory(filePath);
        crc32 = null;
    }

    public FileItem(File inputFile) {
        if (inputFile.isDirectory()) {
            isDirectory = true;
            crc32 = DIR_CRC32;
        } else {
            isDirectory = false;
            crc32 = null;
        }
        this.filePath = inputFile.toPath();
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public String getCrc32() {
        return crc32;
    }

    public Path getFilePath() {
        return filePath;
    }

    public boolean isExitFile() {
        return isExitFile;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setCrc32(String crc32) {
        this.crc32 = crc32;
    }

    public void setExitFile(boolean exitFile) {
        isExitFile = exitFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileItem item = (FileItem) o;
        return isDirectory == item.isDirectory &&
                isExitFile == item.isExitFile &&
                Objects.equals(filePath, item.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, isDirectory, isExitFile);
    }


}
