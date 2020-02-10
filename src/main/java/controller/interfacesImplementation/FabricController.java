package controller.interfacesImplementation;

import controller.BashOptionRead;
import controller.GetCrc32;
import controller.interfaces.FabricControllerInterface;
import controller.interfaces.FileInterface;
import controller.interfaces.ProcessInterface;
import exception.InvalidBashOption;
import gui.Archivist;
import gui.ExitProgramInterface;
import gui.GetInfoInterface;
import model.BashOption;
import model.Help;
import model.Settings;

import java.io.UnsupportedEncodingException;

public class FabricController implements FabricControllerInterface {
    @Override
    public ExitProgramInterface getExitProgramInterface() {
        return new Archivist();
    }

    @Override
    public ProcessInterface getProcessInterfaceFromSettings(Settings settings) {
        if (settings.getOptions().contains(BashOption.HELP) || settings.getOptions().contains(BashOption.VERSION))
            return new InfoOutput(getInfoFromSettings(settings));
        else if (settings.getOptions().contains(BashOption.DEARCHIVE))
            return new DeArchiveProcess();
        else if (settings.getOptions().contains(BashOption.ARCHIVE))
            return new ArchiveProcess();
        return null;
    }

    @Override
    public GetInfoInterface getInfoFromSettings(Settings settings) {
        if (settings.getOptions().contains(BashOption.HELP))
            return new Help();
        else if (settings.getOptions().contains(BashOption.VERSION))
            return new Archivist();
        return null;
    }

    @Override
    public FileInterface getFileInterface() {
        return new FileProcess();
    }

    @Override
    public String getCRCValueForDir() {
        return GetCrc32.DIR_CRC;
    }

    @Override
    public GetCrc32 getCRC32Class() {
        return new GetCrc32();
    }

    @Override
    public Settings getSettings(String[] args) {
        try {
            return new BashOptionRead(args).getSettings();
        } catch (InvalidBashOption invalidBashOption) {
            getExitProgramInterface().exitProgram(2,invalidBashOption,"Invalid command line option");
            return null;
        } catch (UnsupportedEncodingException e) {
            getExitProgramInterface().exitProgram(2,e,"Unsupported Encoding");
            return null;
        }
    }

    @Override
    public void workFormSetting(Settings settings) {
        ProcessInterface process = getProcessInterfaceFromSettings(settings);
        if (process!=null)
            process.write();
        else
            getExitProgramInterface().exitProgram(2,null,"Error");
    }


}
