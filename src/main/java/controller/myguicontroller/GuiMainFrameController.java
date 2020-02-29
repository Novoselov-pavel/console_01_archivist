package controller.myguicontroller;

import controller.listeners.PathButtonListener;
import gui.myswinggui.GuiMainFrame;
import gui.myswinggui.GuiMainFrameAdapter;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class GuiMainFrameController {
    private GuiMainFrameAdapter guiMainFrame;

    public GuiMainFrameController() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(()->{
            this.guiMainFrame = new GuiMainFrameAdapter(getResourceBundle());
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

    }

    public static ResourceBundle getResourceBundle() {
        Locale currentLocale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("GuiMainFrame", currentLocale);
        return rb;
    }


    ///TODO temp
    public static void main(String[] args) throws Exception {
        GuiMainFrameController controller = new GuiMainFrameController();
    }

}
