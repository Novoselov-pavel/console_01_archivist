package gui;

public interface LoggerInterface {
    /** Write message to System.out and Stack Trace to System.Err.
     *
     * @param e Exception, can be null
     * @param message message, can be null
     */
    void writeErrorMessage (Exception e, String message);

    /** Write message to System.out if log is enable
     *
     * @param message message, can be null
     */
    void writeLogger(String message);
}
