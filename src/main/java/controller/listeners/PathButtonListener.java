package controller.listeners;

import gui.myswinggui.GuiMainFrame;
import gui.myswinggui.GuiMainFrameAdapter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PathButtonListener implements ActionListener {

    private final GuiMainFrameAdapter guiMainFrameAdapter;

    public PathButtonListener(GuiMainFrameAdapter guiMainFrameAdapter) {
        this.guiMainFrameAdapter = guiMainFrameAdapter;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        GuiMainFrame main = guiMainFrameAdapter.getMainFrame();
        int returnVal = chooser.showOpenDialog(main);
        chooser.setSize(1200,500);

        if(returnVal == JFileChooser.APPROVE_OPTION) {
            if (e.getSource() instanceof JButton) {
                JButton button = (JButton) e.getSource();
                if (button.getText().equals(main.getFirstPathButton().getText())){
                    main.getFirstPathArea().setEditable(true);
                    main.getFirstPathArea().setText(chooser.getSelectedFile().getPath());
                    main.getFirstPathArea().setEditable(false);
                    main.update(main.getGraphics());
                } else if (button.getText().equals(main.getSecondPathButton().getText())) {
                    main.getSecondPathArea().setEditable(false);
                    main.getSecondPathArea().setText(chooser.getSelectedFile().getPath());
                    main.getSecondPathArea().setEditable(true);
                    main.update(main.getGraphics());
                } else  {
                    throw new Error("ActionListener was used not from allowed button");
                }
            } else {
                throw new Error("ActionListener was used not from JButton");
            }
        }
    }
}
