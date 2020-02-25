package model;

import exception.InvalidBashOption;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Objects;

/**Class for storage and check Bash option
 *
 */
public class Settings implements SettingInterface {
    private LinkedList<BashOption> options;
    private Path inputPath;
    private Path outputPath;
    private String consoleEncode;
    private String fileEncode;
    public final String OUTPUT_FILE_NAME_FORMAT = "%s_%d.zip";
    public final String INI_FILE_NAME_FORMAT = "%s_%d_prop.ini";
    public final int MAX_ITERATION = 10;
    public final int MAX_TIMEOUT = 30_000;

    /**Constructor. Throw InvalidBashOption if input Bash option is incorrect
     *
     * @param options
     * @param inputPath
     * @param outputPath
     * @throws InvalidBashOption
     */
    public Settings(LinkedList<BashOption> options, String inputPath, String outputPath) throws InvalidBashOption, UnsupportedEncodingException {
        if (isCorrect(options,inputPath,outputPath)) {
            this.options = options;
            String conEnd = System.getProperty("consoleEncoding");
            if (conEnd==null)
                    conEnd = System.getProperty("sun.jnu.encoding");
            if (conEnd == null)
                    conEnd ="UTF-8";
            this.consoleEncode = conEnd;
            String fileEnc = System.getProperty("file.encoding");
            if (fileEnc==null)
                    fileEnc = "UTF-8";
            this.fileEncode = fileEnc;
            if (inputPath!=null)
            this.inputPath = Paths.get(inputPath);
            if (outputPath!=null)
            this.outputPath = Paths.get(outputPath);
        } else {
            throw new InvalidBashOption();
        }
    }

    public LinkedList<BashOption> getOptions() {
        return options;
    }

    public Path getInputPath() {
        return inputPath;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public String getConsoleEncode() {
        return consoleEncode;
    }

    public String getFileEncode() {
        return fileEncode;
    }


    public String getOUTPUT_FILE_NAME_FORMAT() {
        return OUTPUT_FILE_NAME_FORMAT;
    }

    public String getINI_FILE_NAME_FORMAT() {
        return INI_FILE_NAME_FORMAT;
    }

    public int getMAX_ITERATION() {
        return MAX_ITERATION;
    }

    public int getMAX_TIMEOUT() {
        return MAX_TIMEOUT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return Objects.equals(options, settings.options) &&
                Objects.equals(inputPath, settings.inputPath) &&
                Objects.equals(outputPath, settings.outputPath);
    }

    @Override
    public int hashCode() {
        return 1012+31*Objects.hash(options, inputPath, outputPath);
    }



    /**Check input parameter, true if correct and false if incorrect.
     *
     * @param options
     * @param inputPath
     * @param outputPath
     * @return
     */
    private boolean isCorrect (LinkedList<BashOption> options, String inputPath, String outputPath) {
        if (options==null) return false;
        if (options.contains(BashOption.ARCHIVE) || options.contains(BashOption.DEARCHIVE)) {
            return inputPath!=null && outputPath !=null;
        }
        else if (options.contains(BashOption.HELP) || options.contains(BashOption.VERSION)) {
            return true;
        }
        return false;
    }





}
