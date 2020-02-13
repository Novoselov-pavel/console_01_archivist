package controller.interfaces;

import model.FileItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface FileInterface {

    public void createAllPathDirectory(String fullPath);

    public String writeStreamAndReturnCRC(InputStream inputStream, OutputStream outputStream) throws IOException;

    public void copyFromInputDirectoryToDestination(String inputPath, String destinationPath, boolean overwrite);

    /**Return path of file that doesn't exist in directory of directoryPath.
     *
     * @param fileNameFormat may be follow String in {@link String#format(String, Object...)}
     *                       <br>Must contain %s and %d. For example "%s%d.zip"
     * @param firstPartFileName value of %s in fileNameFormat
     * @param directoryPath directory of this file
     */
    String getFreeFileName(String fileNameFormat, String firstPartFileName, String directoryPath);

    /**Check and return path for Directory.
     * <br>Example: if path: /dir1/dir2
     * <br>return: /dir/dir2/
     * <br>if path: /dir/dir2/
     * <br> return input path
     *
     * @param path path of directory
     * @return String
     */
    String getDirectoryPath (String path);

    /**Adding into List {@link FileItem} element for each file in sourceDirectory and below.
     * If file isn't Directory return input list.
     * @param file start Directory
     * @param list list where FileItem will be add. If it is null, will be create new List.
     * @param basePath path for calculation FileItem.getRelativeFilePath(); If it is null, file Directory is used as basePath
     * @return List<FileItem>
     */
     List<FileItem> getFileItemArrayListListFromFile (File file, java.util.List<FileItem> list, String basePath);
}
