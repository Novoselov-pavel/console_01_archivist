package gui;

import controller.interfaces.FabricControllerInterface;
import controller.interfacesImplementation.FabricController;

/**Main class
 *
 */
public class Archivist implements GetInfoInterface {

    private static String versionID = "Archivist version 0.2\n Copyright(c) 2020 by Novoselov Pavel.\n"+
                                      "License GPLv3+: GNU GPL version 3 or later\n"+
                                      "This is free software: you are free to change and redistribute it.\n"+
                                      "There is NO WARRANTY, to the extent permitted by law.";


    public static void main(String[] args) {
        FabricControllerInterface controllerInterface = new FabricController(args);
        controllerInterface.workFormSetting();
    }

    @Override
    public String[] getInfo() {
        String[] strings = new String[1];
        strings[0] = versionID;
        return strings;
    }
}
