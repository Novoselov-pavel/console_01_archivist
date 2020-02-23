package model;

public enum LoggerMessages {
    BEGIN_PACK("Begin packing of %s"),
    END_PACK("End packing of file %s"),
    WRITE_INI_FILE("Writing ini file %s"),
    BEGIN_UNPACK("Begin unpacking of %s"),
    END_UNPACK("End unpacking of file %s"),
    READ_INI_FILE("Reading ini file %s"),
    END_ALL_PACK_PROCESS("End all pack process %s");



    private String formatString;
    private LoggerMessages(String formatString) {
        this.formatString = formatString;
    }

    /**return String to use in {@link String#format(String, Object...)}
     *
     * @return
     */
    public String getFormatter() {
        return  this.formatString;
    }


}
