package gui;

import controller.interfaces.FabricControllerInterface;
import controller.interfacesimplementation.FabricController;
import controller.myguicontroller.GuiController;
import gui.interfaces.GetInfoInterface;

import javax.swing.*;

/**Main class
 *
 */
public class Archivist implements GetInfoInterface {

    private static final String versionID = "Archivist version 1.2\n Copyright(c) 2020 by Novoselov Pavel.\n"+
                                      "License GPLv3+: GNU GPL version 3 or later\n"+
                                      "This is free software: you are free to change and redistribute it.\n"+
                                      "There is NO WARRANTY, to the extent permitted by law.";


    public static void main(String[] args) {
        if (args!= null && args.length>0) {
        FabricControllerInterface controllerInterface = new FabricController(args);
        controllerInterface.workFormSetting();
        } else {
            try {
                GuiController guicontroller = new GuiController();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public String[] getInfo() {
        String[] strings = new String[1];
        strings[0] = versionID;
        return strings;
    }
}
