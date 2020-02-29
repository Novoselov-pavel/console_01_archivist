package gui.myswinggui;

import model.BashOption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class GuiMainFrame extends JFrame {


    private JPanel mainPanel;
    private JButton modeButton;
    private JButton firstPathButton;
    private JButton secondPathButton;
    private JProgressBar progressBar1;
    private JRadioButton versionButton;
    private JRadioButton helpButton;
    private JRadioButton packButton;
    private JRadioButton unpackButton;
    private JTextArea firstPathArea;
    private JTextArea secondPathArea;
    private JButton actionButton;

    /**
     * Constructs a new frame that is initially invisible.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *                           returns true.
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     */
    public GuiMainFrame() throws HeadlessException {
        add(mainPanel); //constructor's calling for initialization all subcomponents
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JButton getModeButton() {
        return modeButton;
    }

    public JButton getFirstPathButton() {
        return firstPathButton;
    }

    public JButton getSecondPathButton() {
        return secondPathButton;
    }

    public JProgressBar getProgressBar1() {
        return progressBar1;
    }

    public JRadioButton getVersionButton() {
        return versionButton;
    }

    public JRadioButton getHelpButton() {
        return helpButton;
    }

    public JRadioButton getPackButton() {
        return packButton;
    }

    public JRadioButton getUnpackButton() {
        return unpackButton;
    }

    public JTextArea getFirstPathArea() {
        return firstPathArea;
    }

    public JTextArea getSecondPathArea() {
        return secondPathArea;
    }

    public JButton getActionButton() {
        return actionButton;
    }
}
