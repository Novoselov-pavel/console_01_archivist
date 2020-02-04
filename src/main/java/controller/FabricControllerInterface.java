package controller;

import gui.ExitProgramInterface;
import gui.GetInfoInterface;
import model.Settings;

public interface FabricControllerInterface {
    public ExitProgramInterface getExitProgramInterface();
    public ProcessInterface getProcessInterfaceFromSettings(Settings settings);
    public GetInfoInterface getInfoFromSettings(Settings settings);
}
