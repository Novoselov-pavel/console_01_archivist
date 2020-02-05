package controller.interfaces;

import java.io.InputStream;
import java.io.OutputStream;

public interface FileInterface {

    public void createAllPathDirectory(String fullPath);

    public String writeStreamAndReturnCRC(InputStream inputStream, OutputStream outputStream);


}
