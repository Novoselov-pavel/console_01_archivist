package model;

import java.nio.file.Path;
import java.util.LinkedList;

public interface SettingInterface {
    LinkedList<BashOption> getOptions();
    String getConsoleEncode();
    String getFileEncode();
    Path getInputPath();
    Path getOutputPath();

    String getOUTPUT_FILE_NAME_FORMAT();
    String getINI_FILE_NAME_FORMAT();
    int getMAX_ITERATION();
    int getMAX_TIMEOUT();
}
