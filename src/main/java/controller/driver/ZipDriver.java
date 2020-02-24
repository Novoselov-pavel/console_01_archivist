package controller.driver;


import model.FileItem;
import model.LoggerMessages;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipInputStream;

public class ZipDriver {

    private final Path zipFilePath;
    private ZipOutputStream zipOutputStream;
    private ZipInputStream zipInputStream;

    public ZipDriver(Path zipFilePath) {
        this.zipFilePath = zipFilePath;
    }


//    /**Get ZipOutputStream for constructor's path, singleton
//     *
//     * @return ZipOutputStream
//     * @throws IOException error
//     */
//    public ZipOutputStream getZipOutputStream() throws IOException {
//        if (zipInputStream == null) {
//            ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(zipFilePath));
//            return zip;
//        } else {
//            return zipOutputStream;
//        }
//    }
//
//    /**Get ZipInputStream for constructor's path, singleton
//     *
//     * @return ZipInputStream
//     * @throws IOException error
//     */
//    public ZipInputStream getZipInputStream() throws IOException {
//        if (zipInputStream == null) {
//            ZipInputStream zip = new ZipInputStream(Files.newInputStream(zipFilePath));
//            return zip;
//        } else {
//            return zipInputStream;
//        }
//    }
//
//
//    /**Closed ZipOutputStream
//     *
//     * @throws IOException error
//     */
//    public void closeOutputStream() throws IOException {
//        zipOutputStream.close();
//        zipOutputStream=null;
//    }
//
//    /**Closed ZipInputStream
//     *
//     * @throws IOException error
//     */
//    public void closeInputStream () throws IOException {
//        zipInputStream.close();
//        zipOutputStream = null;
//    }

    /**Write list of FilItem to ZipOutputStream
     *
     * @param list list of FileItem
     * @param basePath base path is needed to obtain relative path
     * @return list of FileItem with crc32 and relative path
     * @throws IOException on error
     */
    public List<FileItem> packListToZipFile(List<FileItem> list, Path basePath) throws IOException {
        try(ZipOutputStream zipStream = new ZipOutputStream(Files.newOutputStream(zipFilePath)) ) {
            for (FileItem item : list) {
                packFile(item,zipStream,basePath);
            }
        }
        return list;
    }

    /**Write FilItem to ZipOutputStream
     *
     * @param inputFile FileItem
     * @param stream ZipOutputStream
     * @return FileItem with relative paths
     * @throws IOException on error
     */
    private FileItem packFile (FileItem inputFile, ZipOutputStream stream, Path basePath)throws IOException {
        if (Files.isRegularFile(inputFile.getFilePath())) {
            ZipEntry entry = new ZipEntry(basePath.relativize(inputFile.getFilePath()).toString());
            stream.putNextEntry(entry);
            String crc32 = writeStreamAndReturnCRC(Files.newInputStream(inputFile.getFilePath()),stream);
            inputFile.setCrc32(crc32);
            inputFile.setFilePath(basePath.relativize(inputFile.getFilePath()));
            stream.closeEntry();
            return inputFile;
        } else if (Files.isDirectory(inputFile.getFilePath()) || Files.isSymbolicLink(inputFile.getFilePath())) {
            ZipEntry entry = new ZipEntry(getZipDirectory(basePath.relativize(inputFile.getFilePath())));
            stream.putNextEntry(entry);
            stream.closeEntry();
            inputFile.setCrc32(FileItem.DIR_CRC32);
            inputFile.setFilePath(basePath.relativize(inputFile.getFilePath()));
            return inputFile;
        }
        throw new IOException("Incorrect type of pack file");
    }


    /**Write inputStream to outputStream and return value of CRC32
     *
     * @param inputStream {@link InputStream} implementation
     * @param outputStream {@link OutputStream} implementation
     * @return {@link String} value of CRC32
     * @throws IOException of inputStream and outputStream
     */
    public String writeStreamAndReturnCRC(InputStream inputStream, OutputStream outputStream) throws IOException {
        CRC32 crc32 = new CRC32();
        while (inputStream.available()>0) {
            byte[] buffer;
            int maxLength = inputStream.available();
            if (maxLength>4048) {
                buffer = new byte[4048];
            } else {
                buffer = new byte[maxLength];
            }
            inputStream.read(buffer);
            crc32.update(buffer);
            outputStream.write(buffer);
        }
        return String.valueOf(crc32.getValue());
    }

    /**Unpack zip file and return List of FileItem with CRC
     *
     * @param destinationPath directory to unpack
     * @return List of FileItem with CRC
     * @throws IOException on error
     */
    public List<FileItem> unpackZipFile(Path destinationPath, String consoleEncode) throws IOException {
        List<FileItem> list = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(zipFilePath.toString(), consoleEncode)) {
            ArrayList<ZipEntry> inputList = Collections.list(zipFile.getEntries());
            for (ZipEntry entry : inputList) {
                list.add(writeZipEntryToFile(zipFile,entry,destinationPath));
            }
        }
        return list;
    }


    /**Unpack ZipEntry from ZipFile and return new FilItem with crc32.
     *
     * @param zipFile org.apache.tools.zip.ZipFile;
     * @param entry org.apache.tools.zip.ZipEntry;
     * @param destinationPath path there file has unpacked
     * @return new FilItem with crc32
     * @throws IOException on error
     */
    public FileItem writeZipEntryToFile(ZipFile zipFile, ZipEntry entry, Path destinationPath) throws IOException {
        Path path = destinationPath.resolve(getPathOfZipEntry(entry));
        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException e) {}

        if (isDirectory(entry)) {
            FileItem item = new FileItem(path,true,GetCrc32.DIR_CRC);
            return item;
        } else {
            try (OutputStream outStream = Files.newOutputStream(path)) {
                String crc = writeStreamAndReturnCRC(zipFile.getInputStream(entry),outStream);
                FileItem item = new FileItem(path,false,crc);
                return item;
            }
        }
    }

    /**Modified path to string with end symbol "/" in accordance with requirement  org.apache.tools.zip.ZipEntry;
     * @param path input directory path
     * @return string
     */
    private String getZipDirectory (Path path) {
        String s = path.toString();
        String endString = System.getProperty("file.separator");
        if (s.endsWith(endString)) {
            s = s.substring(0,s.length()-endString.length());
        }
        return s+"/";
    }

    /**Check if ZipEntry directory (name end with with a forward slash "/".)
     *
     * @param entry org.apache.tools.zip.ZipEntry
     * @return true if input ZipEntry is directory or false otherwise
     */
    private boolean isDirectory (ZipEntry entry) {
        return entry.getName().endsWith("/");
    }

    /**
     *
     * @param entry
     * @return
     */
    private Path getPathOfZipEntry (ZipEntry entry) {
        if (isDirectory(entry)) {
            return Paths.get(entry.getName().substring(0,entry.getName().length()-1)+System.getProperty("file.separator"));
        } else {
            return Paths.get(entry.getName());
        }
    }


}
