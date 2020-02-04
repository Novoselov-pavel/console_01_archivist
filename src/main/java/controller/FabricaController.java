package controller;

import gui.Archivist;
import gui.ExitProgramInterface;
import gui.GetInfoInterface;
import model.Settings;

public class FabricaController implements FabricControllerInterface {
    @Override
    public ExitProgramInterface getExitProgramInterface() {
        return new Archivist();
    }

    @Override
    public ProcessInterface getProcessInterfaceFromSettings(Settings settings) {
        //TODO
        return null;
    }

    @Override
    public GetInfoInterface getInfoFromSettings(Settings settings) {
        //TODO
        return null;
    }

}
