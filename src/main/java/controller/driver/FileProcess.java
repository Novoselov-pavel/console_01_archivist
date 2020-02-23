package controller.driver;

import controller.fileVisitors.FileVisitorAddFileItemToList;
import controller.fileVisitors.FileVisitorDelete;
import model.FileItem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileProcess {


    /**Check and return non-existed path of the file from fileFormat string.
     *
     * @param fileNameFormat according with {@link String#format(String, Object...)} may have two parameter: %s %d.
     *                       For example: "%s_%d.zip"
     * @param basePath path of directory when files will be checking.
     * @param consoleEncoding current consoleEncoding from System.getProperty("consoleEncoding")
     * @return
     */
    public Path getNewTodayFile (final String fileNameFormat,final Path basePath, final String consoleEncoding) throws UnsupportedEncodingException {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = new String(dateFormat.format(date).getBytes(StandardCharsets.UTF_8), consoleEncoding);
        int i = 0;
        while (Files.exists(basePath.resolve(new String(String.format(fileNameFormat,dateString,i).getBytes(),consoleEncoding)))) {
            i++;
        }
        return  basePath.resolve(new String(String.format(fileNameFormat,dateString,i).getBytes(),consoleEncoding));
    }

    /**Adding into List {@link FileItem} element for each file in sourceDirectory and below.
     * If file isn't Directory return input list. Start directory doesn't added in result.
     * @param sourceDirectory start Directory
     * @param list list where FileItem will be add. If it is null, will be create new List.
     * @return List<FileItem>
     */
    public List<FileItem> getFileItemArrayListFromFile(final Path sourceDirectory, List<FileItem> list) throws IOException {
        if (list == null)
            list = new ArrayList<>();

        if (!Files.isRegularFile(sourceDirectory)) list.add(new FileItem(sourceDirectory,false,null));

        if (!Files.isDirectory(sourceDirectory)) return list;

        FileVisitorAddFileItemToList visitor = new FileVisitorAddFileItemToList(list, Collections.singletonList(sourceDirectory));
        Files.walkFileTree(sourceDirectory,visitor);
        return visitor.getList();
    }

    /**Copy all from input path to destination path. File names doesn't change. Symbolic link copy as link.
     *
     * @param inputPath input path of file or directory
     * @param destinationPath destination path
     * @param overwrite  boolean overwrite
     */
    public void copyFromInputToDestination(final Path inputPath, final Path destinationPath, final boolean overwrite) throws IOException {

        if (Files.isRegularFile(inputPath)|| Files.isSymbolicLink(inputPath)) {
            Files.createDirectories(destinationPath);
            if (overwrite)
                Files.copy(inputPath,destinationPath,StandardCopyOption.REPLACE_EXISTING);
            else
                Files.copy(inputPath,destinationPath);
            return;
        }

        if(Files.isDirectory(inputPath)) {
            Files.createDirectories(destinationPath);
             DirectoryStream<Path> stream = Files.newDirectoryStream(inputPath);
             for (Path path : stream) {
                 copyFromInputToDestination(path,destinationPath.resolve(path.getFileName()),overwrite);
             }
        }
    }

    /**Delete all files, directory and sub directory from the path
     *
     * @param deletedPath
     */
    public void deleteFile(final Path deletedPath) throws IOException {
        Files.walkFileTree(deletedPath,new FileVisitorDelete());
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
