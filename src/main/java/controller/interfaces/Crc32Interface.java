package controller.interfaces;

import java.io.File;
import java.io.IOException;

public interface Crc32Interface {

    /**
     * Returns CRC-32 value.
     */
    String getValue();

    /**
     * Updates the current checksum with the specified array of bytes.
     *
     * @implSpec This default implementation is equal to calling
     * {@code update(b, 0, b.length)}.
     *
     * @param b the array of bytes to update the checksum with
     *
     * @throws NullPointerException
     *         if {@code b} is {@code null}     *
     */
    void update(byte[] b);

    /**Updates the CRC-32 checksum with the specified file.
     *
     * @param fileName
     * @throws IOException
     */
     void update (String fileName) throws IOException;

    /**
     * Resets CRC-32 to initial value.
     */
     void reset();

    /**Updates the CRC-32 checksum with the specified file.
     *
     * @param file
     * @throws IOException
     */
    void update (File file) throws IOException;
}
