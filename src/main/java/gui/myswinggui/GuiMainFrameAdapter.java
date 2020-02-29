package gui.myswinggui;

import model.BashOption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class GuiMainFrameAdapter {
    private final GuiMainFrame mainFrame;
    private final ResourceBundle inputBundle;

    public GuiMainFrameAdapter(ResourceBundle inputBundle) {
        this.inputBundle = inputBundle;
        mainFrame = new GuiMainFrame();
        mainFrame.setTitle("Archivist");
        mainFrame.setSize(1000,250);
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
        setLocalization(mainFrame);
        mainFrame.getFirstPathArea().setLineWrap(true);
        mainFrame.getSecondPathArea().setLineWrap(true);
        mainFrame.getFirstPathArea().setEditable(false);
        mainFrame.getSecondPathArea().setEditable(false);
    }

    /**Set localization from {@link ResourceBundle}
     *
     * @param input component from each (down hierarchy) localization has set
     */
    public void setLocalization(Component input) {
        if (input instanceof Container) {
            for (Component component : ((Container) input).getComponents()) {
                if (component instanceof AbstractButton) {
                    try {
                        ((AbstractButton) component).setText(inputBundle.getString(((AbstractButton) component).getText()));
                    } catch (Exception ex) { }
                }
                setLocalization(component);
            }
        }
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
