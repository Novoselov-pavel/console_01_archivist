package controller.driver;


import model.FileItem;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
     * @throws IOException
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
     * @throws IOException
     */
    private FileItem packFile (FileItem inputFile, ZipOutputStream stream, Path basePath)throws IOException {
        ZipEntry entry = new ZipEntry(basePath.relativize(inputFile.getFilePath()).toString());
        if (Files.isRegularFile(inputFile.getFilePath())) {
            stream.putNextEntry(entry);
            String crc32 = writeStreamAndReturnCRC(Files.newInputStream(inputFile.getFilePath()),stream);
            inputFile.setCrc32(crc32);
            inputFile.setFilePath(basePath.relativize(inputFile.getFilePath()));
            stream.closeEntry();
            return inputFile;
        } else if (Files.isDirectory(inputFile.getFilePath()) || Files.isSymbolicLink(inputFile.getFilePath())) {
            stream.putNextEntry(entry);
            stream.closeEntry();
            inputFile.setCrc32(FileItem.DIR_CRC32);
            inputFile.setFilePath(basePath.relativize(inputFile.getFilePath()));
            return inputFile;
        }
        throw new IOException("Incorrect type of pack file");
    }


    /** write inputStream to outputStream and return value of CRC32
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

}
