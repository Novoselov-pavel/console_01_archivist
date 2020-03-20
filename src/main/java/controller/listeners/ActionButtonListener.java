package controller.listeners;

import controller.myguicontroller.GuiController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionButtonListener  implements ActionListener {

    private final GuiController controller;

    public ActionButtonListener(GuiController controller) {
        this.controller = controller;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        controller.action();
    }
}
