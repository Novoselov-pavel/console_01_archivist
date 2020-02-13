package model;

import exception.InvalidBashOption;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Objects;

/**Class for storage and check Bash option
 *
 */
public class Settings implements SettingInterface {
    private LinkedList<BashOption> options;
    private String inputPath;
    private String outputPath;
    private String systemCode;

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
            this.systemCode = getsystemCode();
            this.inputPath = new String(inputPath.getBytes("UTF-8"),systemCode);
            this.outputPath = new String(outputPath.getBytes("UTF-8"),systemCode);
        } else {
            throw new InvalidBashOption();
        }
    }

    public LinkedList<BashOption> getOptions() {
        return options;
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getOutputPath() {
        return outputPath;
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


    private String getsystemCode() {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("win")>=0) {
            return "Cp866";
        } else {
            return "UTF-8";
        }
    }



}
