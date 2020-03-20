package gui.myswinggui;

import gui.interfaces.ExitProgramInterface;

import java.awt.*;

public class ExitForm extends InfoForm implements ExitProgramInterface {

    private final GuiMainFrameAdapter gui;
    private int exitStatus = 0;

    public ExitForm(GuiMainFrameAdapter gui) {
        this.gui = gui;
    }

    @Override
    public void onOK() {
        super.onOK();
        System.exit(exitStatus);
    }

    /**
     * WWrite message, error and exit program with {@code status}
     *
     * @param status     exit code for program
     * @param e          {@link Exception}, can be null
     * @param outMessage can be null
     */
    @Override
    public void exitProgram(int status, Exception e, String outMessage) {
        this.exitStatus = status;
        StringBuilder builder = new StringBuilder();
        builder.append(outMessage).append("\n");
        for (StackTraceElement element : e.getStackTrace()) {
            builder.append(element.toString()).append("\n");
        }
        textArea1.setEditable(true);
        textArea1.setText(builder.toString());
        textArea1.setEditable(false);
        this.pack();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width / 2) - (getWidth() / 2);
        int y = (screenSize.height / 2) - (getHeight() / 2);
        setLocation(x, y);
        this.setVisible(true);
//        this.update(this.getGraphics());
    }
}
