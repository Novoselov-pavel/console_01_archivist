package controller.driver;

import controller.interfaces.Crc32Interface;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public class GetCrc32 implements Crc32Interface {
    private final int BUFFER_SIZE = 8192;
    public static final String DIR_CRC = "DIR";
    private String returnCrc32="";
    private CRC32 crc32 = new CRC32();


    /**Updates the CRC-32 checksum with the specified file.
     *
     * @param fileName
     * @throws IOException
     */
    public void update (String fileName) throws IOException {
        File file = new File(fileName);
        update(file);
    }

    /**Updates the CRC-32 checksum with the specified file.
     *
     * @param file
     * @throws IOException
     */
    public void update (File file) throws IOException {
        if (file.isDirectory()) {
            returnCrc32 = DIR_CRC;
            return;
        }

        FileInputStream stream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);

        while (bufferedInputStream.available()>0){
            byte[] buffer;
            int maxLength = bufferedInputStream.available();
            if (maxLength>BUFFER_SIZE) {
                buffer = new byte[BUFFER_SIZE];
            } else {
                buffer = new byte[maxLength];
            }
            bufferedInputStream.read(buffer);
            update(buffer);
        }
        bufferedInputStream.close();
    }

    /**
     * Updates the CRC-32 checksum with the specified byte (the low
     * eight bits of the argument b).
     */
    public void update(int b) {
        crc32.update(b);
    }

    /**
     * Updates the CRC-32 checksum with the specified array of bytes.
     *
     * @throws ArrayIndexOutOfBoundsException
     *         if {@code off} is negative, or {@code len} is negative, or
     *         {@code off+len} is negative or greater than the length of
     *         the array {@code b}.
     */
    public void update(byte[] b, int off, int len) {
        crc32.update(b, off, len);
    }

    /**
     * Updates the CRC-32 checksum with the bytes from the specified buffer.
     *
     * The checksum is updated with the remaining bytes in the buffer, starting
     * at the buffer's position. Upon return, the buffer's position will be
     * updated to its limit; its limit will not have been changed.
     *
     * @since 1.8
     */
    public void update(ByteBuffer buffer) {
        crc32.update(buffer);
    }

    /**
     * Resets CRC-32 to initial value.
     */
    public void reset() {
        crc32.reset();
    }

    /**
     * Returns CRC-32 value.
     */
    public String getValue() {
        if (returnCrc32==null || returnCrc32.isEmpty()) {
            return Long.toString(crc32.getValue());
        } else {
            return returnCrc32;
        }
    }

    /**
     * Updates the current checksum with the specified array of bytes.
     *
     * @implSpec This default implementation is equal to calling
     * {@code update(b, 0, b.length)}.
     *
     * @param b the array of bytes to update the checksum with
     *
     * @throws NullPointerException
     *         if {@code b} is {@code null}
     *
     * @since 9
     */
    public void update(byte[] b) {
        crc32.update(b);
    }
}
