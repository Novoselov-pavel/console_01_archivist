package controller.interfaces;

import controller.GetCrc32;
import gui.ExitProgramInterface;
import gui.GetInfoInterface;
import model.Settings;

public interface FabricControllerInterface {
    ExitProgramInterface getExitProgramInterface();
    ProcessInterface getProcessInterfaceFromSettings(Settings settings);
    GetInfoInterface getInfoFromSettings(Settings settings);
    FileInterface getFileInterface();
    String getCRCValueForDir();
    GetCrc32 getCRC32Class();
    Settings getSettings(String[] args);
    void workFormSetting(Settings settings);



}
