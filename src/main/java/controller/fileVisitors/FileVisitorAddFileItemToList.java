package controller.fileVisitors;

import model.FileItem;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**Implementation of {@link SimpleFileVisitor}. Creating {@link FileItem} for all files and directory and add
 * its to List.
 * <br>Function getList() return List.
 *
 */
public class FileVisitorAddFileItemToList extends SimpleFileVisitor<Path> {

    private final List<FileItem> list;
    private final List<Path> excludedPath;

    /** {@link java.nio.file.FileVisitor} implementation. Creating {@link FileItem} for all files and directory and add
     * its to List. Path in excludedPath List won't  be added in resultList
     *
     * @param list Output List,not null
     * @param excludedPath List of excluded path, can be null
     */
    public FileVisitorAddFileItemToList(List<FileItem> list, List<Path>  excludedPath) {
        this.list = list;
        this.excludedPath = excludedPath;
    }

    /**
     * Invoked for a file in a directory.
     *
     * <p> Unless overridden, this method returns {@link FileVisitResult#CONTINUE
     * CONTINUE}.
     *
     * @param file
     * @param attrs
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(excludedPath==null || !excludedPath.contains(file)) {
            FileItem fileItem = new FileItem(file,false,null);
            list.add(fileItem);
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * Invoked for a directory after entries in the directory, and all of their
     * descendants, have been visited.
     *
     * <p> Unless overridden, this method returns {@link FileVisitResult#CONTINUE
     * CONTINUE} if the directory iteration completes without an I/O exception;
     * otherwise this method re-throws the I/O exception that caused the iteration
     * of the directory to terminate prematurely.
     *
     * @param dir
     * @param exc
     */
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (excludedPath==null || !excludedPath.contains(dir)) {
            FileItem fileItem = new FileItem(dir,true,null);
            list.add(fileItem);
        }
        return FileVisitResult.CONTINUE;
    }

    /**Return list of FileItem for all files and directory
     *
     * @return List
     */
    public List<FileItem> getList() {
        return list;
    }
}
