package gui;

public interface ExitProgramInterface {
    /** WWrite message, error and exit program with {@code status}
     *
     * @param status exit code for program
     * @param e  {@link Exception}, can be null
     * @param outMessage can be null
     */
    void exitProgram(int status, Exception e, String outMessage);
}
