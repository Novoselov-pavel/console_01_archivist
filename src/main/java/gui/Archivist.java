package gui;

import model.Settings;

/**Main class
 *
 */
public class Archivist {

    private static String versionID = "Archivist version 0.1\n Copyright(c) 2020 by Novoselov Pavel.\n"+
                                      "License GPLv3+: GNU GPL version 3 or later\n"+
                                      "This is free software: you are free to change and redistribute it.\n"+
                                      "There is NO WARRANTY, to the extent permitted by law.";

    private static Settings settings= null;

    public static void main(String[] args) {

    }


    public static String getVersionID() {
        return versionID;
    }

    public static void exitProgramm(int status, String message) {
        if (message!=null)
            System.out.println(message);
        System.exit(status);
    }

    public static Settings getSettings() {
        return settings;
    }
}
