package controller.myguicontroller;

import controller.GuiOptionRead;
import controller.drivers.ResourceBundleDriver;
import controller.interfaces.FabricControllerInterface;
import controller.interfacesimplementation.FabricGuiController;
import controller.listeners.ActionButtonListener;
import controller.listeners.PathButtonListener;
import exception.InvalidBashOption;
import gui.interfaces.ExitProgramInterface;
import gui.myswinggui.ExitForm;
import gui.myswinggui.GuiMainFrameAdapter;
import gui.myswinggui.InfoForm;
import model.Settings;

import javax.swing.*;
import java.io.UnsupportedEncodingException;

public class GuiController {
    private GuiMainFrameAdapter guiMainFrame;
    private ResourceBundleDriver resourceDriver;
    private FabricControllerInterface fabricController;

    public GuiController() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        this.resourceDriver = new ResourceBundleDriver("GuiMainFrame");
        SwingUtilities.invokeLater(()->{
            this.guiMainFrame = new GuiMainFrameAdapter(resourceDriver);
            init();
        });
    }

    public void init() {
        guiMainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        guiMainFrame.initComponent();
        guiMainFrame.setVisible(true);
        PathButtonListener pathButtonListener = new PathButtonListener(guiMainFrame);
        guiMainFrame.addFirstPathActionListener(pathButtonListener);
        guiMainFrame.addSecondPathActionListener(pathButtonListener);
        ActionButtonListener actionButtonListener = new ActionButtonListener(this);
        guiMainFrame.addActionButtonActionListener(actionButtonListener);

    }



    /**Action from gui form
     *
     */
    public void action() {
        GuiOptionRead read = new GuiOptionRead(guiMainFrame);
        Settings settings = null;
        try {
            settings = read.getSettings();
            fabricController = new FabricGuiController(settings, this);
            fabricController.workFormSetting();
        } catch (UnsupportedEncodingException e) {
            ExitProgramInterface exit = new ExitForm(guiMainFrame);
        } catch (InvalidBashOption invalidBashOption) {
            InfoForm form = new InfoForm();
            form.showInfo("error"); //TODO will add error description + localisation
        }
    }

    public GuiMainFrameAdapter getGuiMainFrame() {
        return guiMainFrame;
    }


}
