package controller.fileVisitors;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;


/** Implementation of {@link SimpleFileVisitor} for delete all files and directory with Files.walkFileTree
 *
 */
public class FileVisitorDelete extends SimpleFileVisitor<Path> {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(Files.exists(file))
        Files.delete(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (exc==null) {
            if(Files.exists(dir))
                Files.delete(dir);
            return FileVisitResult.CONTINUE;
        } else {
            throw exc;
        }

    }
}
