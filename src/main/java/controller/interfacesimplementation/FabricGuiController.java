package controller.interfacesimplementation;

import controller.drivers.Crc32Driver;
import controller.interfaces.Crc32Interface;
import controller.interfaces.FabricControllerInterface;
import controller.interfaces.ProcessInterface;
import controller.myguicontroller.GuiController;
import gui.Archivist;
import gui.Message;
import gui.interfaces.ExitProgramInterface;
import gui.interfaces.GetInfoInterface;
import gui.interfaces.LoggerInterface;
import gui.myswinggui.ExitForm;
import gui.myswinggui.HelpForm;
import gui.myswinggui.InfoForm;
import model.BashOption;
import model.Help;
import model.SettingInterface;

public class FabricGuiController implements FabricControllerInterface {
    private final SettingInterface settings;
    private LoggerInterface loggerInterface = null;
    private ExitProgramInterface exitProgramInterface = null;
    private final GuiController guiController;


    public FabricGuiController(SettingInterface settings, GuiController guiController) {
        this.settings = settings;
        this.guiController = guiController;
    }

    /**
     * Create instance (if needed) and return {@link ExitProgramInterface}
     *
     * @return
     */
    @Override
    public ExitProgramInterface getExitProgramInterface() {
        if (exitProgramInterface == null) {
            exitProgramInterface = new ExitForm(guiController.getGuiMainFrame());
        }
        return exitProgramInterface;
    }

    /**
     * Create instance (if needed) and return {@link LoggerInterface}
     *
     * @return
     */
    @Override
    public LoggerInterface getLoggerInterface() {
        if (loggerInterface == null) {
            loggerInterface = new Message(getSettings());
        }
        return loggerInterface;
    }

    @Override
    public String getCRCValueForDir() {
        return Crc32Driver.DIR_CRC;
    }

    /**
     * Create instance and return needed implementation {@link Crc32Interface}
     *
     * @return
     */
    @Override
    public Crc32Interface getCRC32Class() {
        return new Crc32Driver();
    }

    /**
     * Create instance and return needed implementation {@link SettingInterface}
     *
     * @return
     */
    @Override
    public SettingInterface getSettings() {
        return settings;
    }

    /**
     * Main work function with all logic
     */
    @Override
    public void workFormSetting() {
        ProcessInterface process = getProcessInterface();
        if (process!=null) {
            process.write();
            if (process instanceof DeArchiveProcess || process instanceof ArchiveProcess) {
                HelpForm endMessage = new HelpForm(()->{
                    String[] a = new String[] {"Ready"};
                    return a;
                });
                endMessage.write();
            }
        }
        else
            getExitProgramInterface().exitProgram(2,null,"Error");
        ///TODO this
    }

    private ProcessInterface getProcessInterface() {
        if (settings.getOptions().contains(BashOption.HELP) || settings.getOptions().contains(BashOption.VERSION))
            return new HelpForm(getInfoInterface());
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
