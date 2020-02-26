package controller.driver;

import controller.fileVisitors.FileVisitorAddFileItemToList;
import controller.fileVisitors.FileVisitorDelete;
import model.FileItem;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("CatchMayIgnoreException")
public class FileDriver {

    private static final Map<Path, Path> pathMap = new HashMap<>(); ///use in copyFromInputToDestination();

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

        if (Files.isRegularFile(sourceDirectory)) {
            list.add(new FileItem(sourceDirectory,false,null));
            return list;
        }

        if (!Files.isDirectory(sourceDirectory)) return list;

        FileVisitorAddFileItemToList visitor = new FileVisitorAddFileItemToList(list, Collections.singletonList(sourceDirectory));
        Files.walkFileTree(sourceDirectory,visitor);
        return visitor.getList();
    }

    /**Safe copy all from input path to destination path. File names doesn't change. Symbolic link copy as link.
     * on error rollback all
     * @param inputPath input path of file or directory
     * @param destinationPath destination path
     * @param overwrite  boolean overwrite
     * @throws IOException at error
     */
    public void copyFromInputToDestination(final Path inputPath, final Path destinationPath, final boolean overwrite) throws IOException {
       /// TODO change for safe version with rollback
        try {
            if (Files.isRegularFile(inputPath) || Files.isSymbolicLink(inputPath)) {
                copyFile(inputPath,destinationPath,overwrite);
            } else if (Files.isDirectory(inputPath)) {
                copyDirectory(inputPath,destinationPath,overwrite);
            }
        } finally {
            deleteAllTempFile();
        }

    }

    /**Delete all files, directory and sub directory from the path
     *
     * @param deletedPath
     * @throws IOException at error
     */
    public void deleteFile(final Path deletedPath) throws IOException {
        Files.walkFileTree(deletedPath,new FileVisitorDelete());
    }


    /**Validating basePath. If path of the file contain basePath return true, else return false.
     *
     * @param file file
     * @param basePath basePath, not Null
     * @return boolean     *
     */
    private boolean checkBasePath(File file, String basePath) {
        return file.getPath().indexOf(basePath)>-1;
    }

    /**Copy file from inputPath to destinationPath call rollback() on error and throw IOException
     *
     * @param inputPath  input path of file
     * @param destinationPath destination path
     * @param overwrite boolean overwrite
     * @throws IOException on error
     */
    private void copyFile (final Path inputPath, final Path destinationPath, final boolean overwrite) throws IOException {
        if (Files.exists(destinationPath)) {
            createTempFileAndAddToMap(destinationPath);
        } else {
            pathMap.put(destinationPath,null);
        }
        try {
            Files.createDirectories(destinationPath);
        } catch (FileAlreadyExistsException ex) {}
        if (overwrite)
            try {
                Files.copy(inputPath,destinationPath,StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                rollback();
                throw ex;
            }
        else
        if (!Files.exists(destinationPath)) {
            try {
                Files.copy(inputPath,destinationPath);
            } catch (IOException ex) {
                rollback();
                throw ex;
            }
        }
    }

    /**Copy directory from inputPath to destinationPath call rollback() on error and throw IOException
     *
     * @param inputPath input path of directory
     * @param destinationPath destination path
     * @param overwrite boolean overwrite
     * @throws IOException on error
     */
    private void copyDirectory(final Path inputPath, final Path destinationPath, final boolean overwrite) throws IOException {
        for (Path path : destinationPath) {
            if (!Files.exists(path)) {
                pathMap.put(path,null);
                try {
                Files.createDirectory(path);
                } catch (IOException ex) {
                    rollback();
                    throw ex;
                }
            }
        }
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(inputPath);
            for (Path path : stream) {
                copyFromInputToDestination(path,destinationPath.resolve(path.getFileName()),overwrite);
            }
        } catch (IOException ex) {
            rollback();
            throw ex;
        }
    }

    private void deleteAllTempFile() {
        for (Map.Entry<Path, Path> entry : pathMap.entrySet()) {
            if (entry.getValue()!=null) {
                try {
                    deleteFile(entry.getValue());
                } catch (Exception ex) {};
            }
        }
    }

    /**Try to rollback all change. all error are ignored
     *
     */
    private void rollback() {
        for (Map.Entry<Path, Path> entry : pathMap.entrySet()) {
            if (entry.getValue()==null) {
                try {
                    Files.delete(entry.getKey());
                } catch (Exception ex) {}
            } else {
                try {
                    Files.copy(entry.getValue(),entry.getKey(),StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {  }
            }
        }
    }

    /**Create copy file from  inputPath to temp file and
     *
     * @param inputPath
     * @throws IOException
     */
    private void createTempFileAndAddToMap(final Path inputPath) throws IOException {
        Path path = Files.createTempFile(inputPath.getFileName().toString(),null);
        Files.copy(inputPath,path,StandardCopyOption.REPLACE_EXISTING);
        pathMap.put(inputPath,path);
    }


}
