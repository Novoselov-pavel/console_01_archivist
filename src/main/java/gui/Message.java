package gui;


import model.SettingInterface;

import java.io.*;

/**The Message class for retirement messages to System.out and System.Err.
 * Can exit program.
 *
 */
public class Message implements ExitProgramInterface, LoggerInterface {

    private final SettingInterface settings;
    private final PrintStream outWriter;
    private final PrintStream errWriter;


    /**Create new instance of Message class. Message is sending to System.out. Error to System.err.
     *
     * @param  settings class implements {@link SettingInterface}
     */
    public Message(SettingInterface settings) {
        this.settings = settings;
        this.outWriter = System.out;
        this.errWriter = System.err;
    }

    /** Create new instance of Message class. Message is sending to {@code outStream}. Error to {@code errStream}.
     *
     * @param outStream PrintStream for output messages for user
     * @param errStream PrintStream for output error message
     * @param settings class implements {@link SettingInterface}
     */
    public Message(PrintStream outStream, PrintStream errStream, SettingInterface settings) {
        this.settings = settings;
        this.outWriter = outStream;
        this.errWriter = errStream;

    }

    /** Write message, error and exit program with {@code status}
     *
     * @param status exit code for program
     * @param e  {@link Exception}, can be null
     * @param outMessage can be null
     */
    @Override
    public void exitProgram(int status, Exception e, String outMessage) {
        if (outMessage!=null) {
            outWriter.println(returnConsoleMessage(outMessage));
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) { }
        }
        if (e!=null)
            e.printStackTrace(errWriter);

        outWriter.close();
        errWriter.close();
        System.exit(status);
    }

    /**
     * Write message to System.out and Stack Trace to System.Err.
     *
     * @param e       Exception, can be null
     * @param message message, can be null
     */
    @Override
    public void writeErrorMessage(Exception e, String message) {
        if (message!=null) {
            outWriter.println(returnConsoleMessage(message));
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) { }
        }
        if (e!=null)
            e.printStackTrace(errWriter);
    }

    /**
     * Write message to System.out if log is enable
     *
     * @param message message, can be null
     */
    @Override
    public void writeLogger(String message) {
        if (false) { ///TODO add command line option for enabled and disabled log
            outWriter.println(returnConsoleMessage(message));
        }
    }

    /** Return message encode in current console encode, on error return original message
     *
     * @param message
     * @return
     */
    private String returnConsoleMessage(String message) {
        if (message == null) return null;
        if (settings == null) return message;
        try {
            return new String(message.getBytes("UTF-8"),settings.getConsoleEncode());
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }


}
