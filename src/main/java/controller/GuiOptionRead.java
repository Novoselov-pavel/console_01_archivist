package controller;

import exception.InvalidBashOption;
import gui.myswinggui.GuiMainFrameAdapter;
import model.BashOption;
import model.Settings;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

/**Get {@link model.Settings} from {@link GuiMainFrameAdapter}
 *
 */
public class GuiOptionRead {
    private final GuiMainFrameAdapter gui;

    public GuiOptionRead(GuiMainFrameAdapter gui) {
        this.gui = gui;
    }
    /**Get settings
     *
     */
    public Settings getSettings() throws UnsupportedEncodingException, InvalidBashOption {
        LinkedList<BashOption> list = new LinkedList<>();
        list.add(gui.getOption());
        Settings settings = new Settings(list,gui.getFirstPath(), gui.getSecondPath());
        return settings;
    }
}
