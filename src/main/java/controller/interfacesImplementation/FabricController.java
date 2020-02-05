package controller.interfacesImplementation;

import controller.interfaces.FabricControllerInterface;
import controller.interfaces.FileInterface;
import controller.interfaces.ProcessInterface;
import gui.Archivist;
import gui.ExitProgramInterface;
import gui.GetInfoInterface;
import model.Settings;

public class FabricController implements FabricControllerInterface {
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

    @Override
    public FileInterface getFileInterface() {
        //TODO
        return null;
    }

}
