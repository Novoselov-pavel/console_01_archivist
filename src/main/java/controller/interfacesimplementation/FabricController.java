package controller.interfacesimplementation;

import controller.BashOptionRead;
import controller.drivers.Crc32Driver;
import controller.interfaces.Crc32Interface;
import controller.interfaces.FabricControllerInterface;
import controller.interfaces.ProcessInterface;
import exception.InvalidBashOption;
import gui.*;
import gui.interfaces.ExitProgramInterface;
import gui.interfaces.GetInfoInterface;
import gui.interfaces.LoggerInterface;
import model.BashOption;
import model.Help;
import model.SettingInterface;

import java.io.UnsupportedEncodingException;

public class FabricController implements FabricControllerInterface {
    private static ExitProgramInterface exitProgramInterface = null;
    private static LoggerInterface loggerInterface = null;
    private static SettingInterface settings = null;

    public FabricController(String[] args) {
        settings = getSettingsFromArgs(args);
    }

    /**
     * Create instance (if needed) and return {@link LoggerInterface}
     *
     * @param
     * @return
     */
    @Override
    public LoggerInterface getLoggerInterface() {
        if (loggerInterface == null) {
            loggerInterface = new Message(getSettings());
        }
        return loggerInterface;
    }

    /**
     * Create instance (if needed) and return {@link ExitProgramInterface}
     *
     * @param
     * @return
     */
    @Override
    public ExitProgramInterface getExitProgramInterface() {
        if (exitProgramInterface == null) {
            exitProgramInterface = new Message(getSettings());
        }
        return exitProgramInterface;
    }

    @Override
    public String getCRCValueForDir() {
        return Crc32Driver.DIR_CRC;
    }

    @Override
    public Crc32Interface getCRC32Class() {
        return new Crc32Driver();
    }

    @Override
    public SettingInterface getSettings() {
        return settings;
    }

    @Override
    public void workFormSetting() {
        ProcessInterface process = getProcessInterface();
        if (process!=null)
            process.write();
        else
            getExitProgramInterface().exitProgram(2,null,"Error");
    }

    private SettingInterface getSettingsFromArgs(String[] args) {
        try {
            return new BashOptionRead(args).getSettings();
        } catch (InvalidBashOption invalidBashOption) {
            getExitProgramInterface().exitProgram(2,null,
                    String.format("Invalid command line option, use %s or %s", BashOption.HELP.getStringOption()[0], BashOption.HELP.getStringOption()[1]));
            return null;
        } catch (UnsupportedEncodingException e) {
            getExitProgramInterface().exitProgram(2,e,"Unsupported Encoding");
            return null;
        }
    }

    private ProcessInterface getProcessInterface() {
        if (settings.getOptions().contains(BashOption.HELP) || settings.getOptions().contains(BashOption.VERSION))
            return new InfoOutput(getInfoInterface());
        else if (settings.getOptions().contains(BashOption.DEARCHIVE))
            return new DeArchiveProcess(this);
        else if (settings.getOptions().contains(BashOption.ARCHIVE))
            return new ArchiveProcess(this);
        return null;
    }

    private GetInfoInterface getInfoInterface() {
        if (settings.getOptions().contains(BashOption.HELP))
            return new Help();
        else if (settings.getOptions().contains(BashOption.VERSION))
            return new Archivist();
        return null;
    }


}
