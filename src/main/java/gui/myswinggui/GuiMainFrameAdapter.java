package gui.myswinggui;

import controller.drivers.ResourceBundleDriver;
import model.BashOption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GuiMainFrameAdapter {
    private final GuiMainFrame mainFrame;
    private final ResourceBundleDriver resourceDriver;

    public GuiMainFrameAdapter(ResourceBundleDriver resourceDriver) {
        this.resourceDriver = resourceDriver;
        mainFrame = new GuiMainFrame();
        mainFrame.setTitle("Archivist");
        mainFrame.setSize(1000,250);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width / 2) - (mainFrame.getWidth() / 2);
        int y = (screenSize.height / 2) - (mainFrame.getHeight() / 2);
        mainFrame.setLocation(x, y);
    }

    public void addFirstPathActionListener(ActionListener actionListener) {
        mainFrame.getFirstPathButton().addActionListener(actionListener);
    }

    public void addSecondPathActionListener(ActionListener actionListener) {
        mainFrame.getSecondPathButton().addActionListener(actionListener);
    }

    public void addActionButtonActionListener(ActionListener actionListener) {
        mainFrame.getActionButton().addActionListener(actionListener);
    }

    public BashOption getOption() {
        if (mainFrame.getVersionButton().isSelected())
            return BashOption.VERSION;
        if (mainFrame.getHelpButton().isSelected())
            return BashOption.HELP;
        if (mainFrame.getPackButton().isSelected())
            return BashOption.ARCHIVE;
        if (mainFrame.getUnpackButton().isSelected())
            return BashOption.DEARCHIVE;
        return null;
    }

    public String getFirstPath() {
        return mainFrame.getFirstPathArea().getText();
    }

    public String getSecondPath() {
        return mainFrame.getSecondPathArea().getText();
    }

    /**initialized Main Frame
     *
     */
    public void initComponent() {
        resourceDriver.setLocalization(mainFrame);
        mainFrame.getFirstPathArea().setLineWrap(true);
        mainFrame.getSecondPathArea().setLineWrap(true);
        mainFrame.getFirstPathArea().setEditable(false);
        mainFrame.getSecondPathArea().setEditable(false);
    }



    /**Sets the operation that will happen by default when the user initiates a "close" on Main frame.     *
     *
     * @param operation {@link WindowConstants}
     */
    public void setDefaultCloseOperation(int operation) {
        mainFrame.setDefaultCloseOperation(operation);
    }

    /**Shows or hides Main Window depending on the value of parameter b.
     *
     * @param b if true, makes the Window visible, otherwise hides the Window.
     */
    public void setVisible(boolean b) {
        mainFrame.setVisible(b);
    }

    /**Get GuiMainFrame
     *
     * @return GuiMainFrame
     */
    public GuiMainFrame getMainFrame() {
        return mainFrame;
    }
}
