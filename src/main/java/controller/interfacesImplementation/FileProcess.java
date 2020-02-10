package controller.interfacesImplementation;

import controller.GetCrc32;
import controller.interfaces.FileInterface;
import model.FileItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;

public class FileProcess implements FileInterface {

    @Override
    public void copyFromInputDirectoryToDestination(String inputPath, String destinationPath, boolean overwrite) {
        createAllPathDirectory(destinationPath);
        File file = new File(inputPath);
        if (!file.exists() || !file.isDirectory()) {
            createAllPathDirectory(inputPath);
        }
        ArrayList<FileItem> list = FileItem.getFileItemArrayListListFromFile(file,null, inputPath);
        ArrayList<FileItem> workList = new ArrayList<>(list);

        Iterator<FileItem> iterator = workList.iterator();
        while (iterator.hasNext()) {
            FileItem currentFileItem = iterator.next();

            File destinationFile = new File(destinationPath+currentFileItem.getRelativeFilePath());
            if (destinationFile.exists()) {
                if (overwrite)
                    try {
                        Files.copy(currentFileItem.getPath(), Paths.get(destinationPath + currentFileItem.getRelativeFilePath()), StandardCopyOption.REPLACE_EXISTING);
                        iterator.remove();
                    } catch (IOException ex) {
                        //TODO in Future
                    }
            }
            else {
                createAllPathDirectory(destinationPath+currentFileItem.getRelativeFilePath());
                try {
                    Files.copy(currentFileItem.getPath(), Paths.get(destinationPath + currentFileItem.getRelativeFilePath()));
                    iterator.remove();
                } catch (IOException ex) {
                    //TODO in Future
                }
            }
        }
        try {
            Files.walkFileTree(Paths.get(inputPath), new FileVisitorDelete());
        } catch (Exception ex) {
            //TODO in Future
        }

    }


    @Override
    public void createAllPathDirectory(String path) {
        path = getDirectoryFromFile(path);
        File file = new File(path);
        file.mkdirs();
    }

    @Override
    public String writeStreamAndReturnCRC(InputStream inputStream, OutputStream outputStream) throws IOException {
        GetCrc32 crc32Interface = new FabricController().getCRC32Class();
        while (inputStream.available()>0) {
            byte[] buffer;
            int maxLength = inputStream.available();
            if (maxLength>4048) {
                buffer = new byte[4048];
            } else {
                buffer = new byte[maxLength];
            }
            inputStream.read(buffer);
            crc32Interface.update(buffer);
            outputStream.write(buffer);
        }
        return crc32Interface.getValue();
    }


    private void copyDir (String inputPath, String destinationPath, boolean overwrite) {

    }

    private String getDirectoryFromFile (String filePath) {
        filePath = filePath.substring(0,filePath.lastIndexOf("/")+1);
        return filePath;
    }
}
