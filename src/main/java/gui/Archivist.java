package gui;

import controller.interfaces.FabricControllerInterface;
import controller.interfacesImplementation.FabricController;
import model.Settings;

/**Main class
 *
 */
public class Archivist implements ExitProgramInterface, GetInfoInterface {

    private static String versionID = "Archivist version 0.1\n Copyright(c) 2020 by Novoselov Pavel.\n"+
                                      "License GPLv3+: GNU GPL version 3 or later\n"+
                                      "This is free software: you are free to change and redistribute it.\n"+
                                      "There is NO WARRANTY, to the extent permitted by law.";

    private static Settings settings= null;

    public static void main(String[] args) {
        FabricControllerInterface controllerInterface = new FabricController();
        settings = controllerInterface.getSettings(args);
        controllerInterface.workFormSetting(settings);
    }

    public static void exitProgramm(int status, String message) {
        if (message!=null)
            System.out.println(message);
        System.exit(status);
    }

    public static Settings getSettings() {
        return settings;
    }

    @Override
    public void exitProgram(int status, Exception e, String outMessage) {
        if (outMessage!=null) {
            System.out.println(outMessage);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) { }
        }
        if (e!=null)
            e.printStackTrace(System.err);
        System.exit(status);
    }


    @Override
    public String[] getInfo() {
        String[] strings = new String[1];
        strings[0] = versionID;
        return strings;
    }
}
