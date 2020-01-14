package model;

import exception.InvalidBashOption;

import java.util.LinkedList;
import java.util.Objects;

public class Settings {
    private LinkedList<BashOption> options;
    private  String inputPath;
    private  String outputPath;

    public Settings(LinkedList<BashOption> options, String inputPath, String outputPath) throws InvalidBashOption {
        if (isCorrect(options,inputPath,outputPath)) {
            this.options = options;
            this.inputPath = inputPath;
            this.outputPath = outputPath;
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

    private boolean isCorrect (LinkedList<BashOption> options, String inputPath, String outputPath) {
        ////TODO
        if (options==null) return false;
        if (options.contains(BashOption.ARCHIVE) || options.contains(BashOption.DEARCHIVE)) {
            return inputPath!=null && outputPath !=null;
        }
        else if (options.contains(BashOption.HELP) || options.contains(BashOption.VERSION)) {
            return true;
        }
        return false;
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
}
